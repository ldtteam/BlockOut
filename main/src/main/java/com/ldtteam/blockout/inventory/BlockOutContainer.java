package com.ldtteam.blockout.inventory;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.inventory.slot.BlockOutSlot;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.itemstack.ItemStackHelper;
import com.ldtteam.blockout.util.side.SideExecutor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockOutContainer extends Container implements IBlockOutContainer
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockOutContainerData containerData;

    public BlockOutContainer(@NotNull final IGuiKey key, @NotNull final IUIElementHost root, final int windowId)
    {
        super(ContainerTypes.BLOCK_OUT_CONTAINER, windowId);
        this.containerData = new BlockOutContainerData(key, root);
    }

    @NotNull
    @Override
    public Slot addSlot(final Slot slot)
    {
        return super.addSlot(slot);
    }

    @Override
    public boolean canInteractWith(@NotNull final PlayerEntity playerIn)
    {
        return true;
    }

    @NotNull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity playerEntity, final int index)
    {
        ItemStack newItemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot instanceof BlockOutSlot)
        {
            BlockOutSlot slotBlockOut = (BlockOutSlot) slot;
            if (slotBlockOut.getHasStack())
            {
                ItemStack itemStack = slotBlockOut.getStack();
                newItemStack = itemStack.copy();
                final Tuple<Integer, Integer> containerRange = findNextInventoryIndices(slotBlockOut);

                if (!mergeItemStack(itemStack, containerRange.getA(), containerRange.getB(), false))
                {
                    return ItemStack.EMPTY;
                }
                if (itemStack.getCount() == 0 || itemStack.isEmpty())
                {
                    slotBlockOut.putStack(ItemStack.EMPTY);
                }
                else
                {
                    slotBlockOut.onSlotChanged();
                }
            }
        }

        return newItemStack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
    {
        boolean slotFound = false;
        int currentSlotIndex = reverseDirection ? endIndex - 1 : startIndex;
        Slot slot;
        ItemStack stackInSlot;
        if (stack.isStackable())
        {
            while (stack.getCount() > 0 && (!reverseDirection && currentSlotIndex < endIndex
                                                                        || reverseDirection && currentSlotIndex >= startIndex))
            {
                slot = inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();
                if (slot.isItemValid(stack) && ItemStackHelper.equalsIgnoreStackSize(stack, stackInSlot))
                {
                    int combinedStackSize = stackInSlot.getCount() + stack.getCount();
                    int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());
                    if (combinedStackSize <= slotStackSizeLimit)
                    {
                        stack.setCount(0);
                        stackInSlot.setCount(combinedStackSize);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                    else if (stackInSlot.getCount() < slotStackSizeLimit)
                    {
                        stack.shrink(slotStackSizeLimit - stackInSlot.getCount());
                        stackInSlot.setCount(slotStackSizeLimit);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }
                if (reverseDirection && currentSlotIndex == inventorySlots.size() - 1)
                {
                    currentSlotIndex = 0;
                }
                else if (!reverseDirection && currentSlotIndex == 0)
                {
                    currentSlotIndex = inventorySlots.size() - 1;
                }
                else
                {
                    currentSlotIndex += reverseDirection ? -1 : 1;
                }
            }
        }
        if (stack.getCount() > 0)
        {
            currentSlotIndex = reverseDirection ? endIndex - 1 : startIndex;
            while (!reverseDirection && currentSlotIndex < endIndex
                     || reverseDirection && currentSlotIndex >= startIndex)
            {
                slot = inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();
                if (slot.isItemValid(stack) && stackInSlot.isEmpty())
                {
                    slot.putStack(ItemStackHelper.cloneItemStack(stack, Math.min(stack.getCount(), slot.getSlotStackLimit())));
                    slot.onSlotChanged();
                    if (!slot.getStack().isEmpty())
                    {
                        stack.shrink(slot.getStack().getCount());
                        slotFound = true;
                    }
                    break;
                }
                if (reverseDirection && currentSlotIndex == inventorySlots.size() - 1)
                {
                    currentSlotIndex = 0;
                }
                else if (!reverseDirection && currentSlotIndex == 0)
                {
                    currentSlotIndex = inventorySlots.size() - 1;
                }
                else
                {
                    currentSlotIndex += reverseDirection ? -1 : 1;
                }
            }
        }
        return slotFound;
    }

    @Override
    public BlockOutContainerData getInstanceData()
    {
        return containerData;
    }

    private Tuple<Integer, Integer> findNextInventoryIndices(final BlockOutSlot slotBlockOut)
    {
        final int startIndex = findStartIndexOfNextInventory(slotBlockOut);
        final BlockOutSlot startSlot = (BlockOutSlot) inventorySlots.get(startIndex);
        final int endIndex = findStartIndexOfNextInventory(startSlot);

        //Check if the entire container is populated with one inventory.
        if (startIndex == 0 && startIndex == endIndex)
        {
            return new Tuple<>(0, inventorySlots.size());
        }

        return new Tuple<>(startIndex, endIndex);
    }

    private Integer findStartIndexOfNextInventory(final BlockOutSlot slotBlockOut)
    {
        final ResourceLocation currentInventory = slotBlockOut.getUiSlotInstance().getInventoryId();
        ResourceLocation workingInventory = slotBlockOut.getUiSlotInstance().getInventoryId();
        BlockOutSlot workingSlot = slotBlockOut;

        while (currentInventory.equals(workingInventory))
        {
            workingSlot =
              ((workingSlot.slotNumber + 1) == inventorySlots.size() ? (BlockOutSlot) inventorySlots.get(0)
                 : (BlockOutSlot) inventorySlots.get(workingSlot.slotNumber + 1));

            if (workingSlot == slotBlockOut)
            {
                break;
            }

            workingInventory = workingSlot.getUiSlotInstance().getInventoryId();
        }

        if (workingSlot == slotBlockOut)
        {
            return 0;
        }

        return workingSlot.slotNumber;
    }

    public void reinitializeSlots()
    {
        inventorySlots.clear();
        inventoryItemStacks.clear();

        initializeSlots();

        SideExecutor.runWhenOn(LogicalSide.SERVER, () -> this::detectAndSendChanges);
    }

    private void initializeSlots()
    {
        int slotIndex = 0;
        final List<com.ldtteam.blockout.element.simple.Slot> slots =getInstanceData().
                                                              getRoot().getAllCombinedChildElements().values().stream()
                                   .filter(element -> element instanceof com.ldtteam.blockout.element.simple.Slot)
                                   .filter(IUIElement::isVisible)
                                   .map(element -> (com.ldtteam.blockout.element.simple.Slot) element).collect(Collectors.toCollection(LinkedList::new));

        for (com.ldtteam.blockout.element.simple.Slot slot :
          slots)
        {
            final IItemHandler itemHandler = getInstanceData().getKey().getItemHandlerManager().getItemHandlerFromId(slot.getInventoryId());
            if (itemHandler == null)
            {
                final StringBuilder errorMessageBuilder = new StringBuilder();
                errorMessageBuilder
                  .append("Failed to find IItemHandler for Slot with index: ")
                  .append(slot.getInventoryIndex())
                  .append(" with Inventory Id: ")
                  .append(slot.getInventoryId())
                  .append(".\n")
                  .append("Possible Inventory candidates are:")
                  .append("\n");

                getInstanceData().getKey().getItemHandlerManager().getAllItemHandlerIds().forEach(loc -> errorMessageBuilder
                                                                                                                     .append("  * ")
                                                                                                                     .append(loc)
                                                                                                                     .append("\n"));

                Log.getLogger().error(errorMessageBuilder.toString());
                throw new IllegalArgumentException("Failed to find IItemHandler for Slot.");
            }

            slot.setSlotIndex(slotIndex++);
            final BlockOutSlot slotItemHandler = new BlockOutSlot(itemHandler, slot);

            addSlot(slotItemHandler);
        }
    }
}
