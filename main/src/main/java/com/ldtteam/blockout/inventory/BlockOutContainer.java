package com.ldtteam.blockout.inventory;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.blockout.inventory.slot.SlotBlockOut;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.itemstack.ItemStackHelper;
import com.ldtteam.jvoxelizer.inventory.IInventoryContainer;
import com.ldtteam.jvoxelizer.item.handling.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockOutContainer implements IInventoryContainer
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private       IUIElementHost root;

    public BlockOutContainer(@NotNull final IGuiKey key, @NotNull final IUIElementHost root, @NotNull final int windowId)
    {
        this.key = key;
        this.root = root;
        this.setWindowId(windowId);

        initializeSlots();
    }

    private void initializeSlots()
    {
        int slotIndex = 0;
        final List<Slot> slots =
          getRoot().getAllCombinedChildElements().values().stream()
            .filter(element -> element instanceof Slot)
            .map(element -> (Slot) element).collect(Collectors.toCollection(LinkedList::new));

        for (Slot slot :
          slots)
        {
            final IItemHandler itemHandler = getKey().getItemHandlerManager().getItemHandlerFromId(slot.getInventoryId());
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

                getKey().getItemHandlerManager().getAllItemHandlerIds().forEach(loc -> errorMessageBuilder
                                                                                         .append("  * ")
                                                                                         .append(loc)
                                                                                         .append("\n"));

                Log.getLogger().error(errorMessageBuilder.toString());
                throw new IllegalArgumentException("Failed to find IItemHandler for Slot.");
            }

            slot.setSlotIndex(slotIndex++);
            final SlotItemHandler slotItemHandler = new SlotBlockOut(itemHandler, slot);

            addSlotToContainer(slotItemHandler);
        }
    }

    @NotNull
    public IUIElementHost getRoot()
    {
        return root;
    }

    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }

    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
        reinitializeSlots();
    }

    public void reinitializeSlots()
    {
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();
        initializeSlots();

        detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(@NotNull EntityPlayer entityPlayer, int slotIndex)
    {
        ItemStack newItemStack = ItemStack.EMPTY;
        net.minecraft.inventory.Slot slot = inventorySlots.get(slotIndex);
        if (slot instanceof SlotBlockOut)
        {
            SlotBlockOut slotBlockOut = (SlotBlockOut) slot;
            if (slotBlockOut.getHasStack())
            {
                ItemStack itemStack = slotBlockOut.getStack();
                newItemStack = itemStack.copy();
                final Tuple<Integer, Integer> containerRange = findNextInventoryIndices(slotBlockOut);

                if (!this.mergeItemStack(itemStack, containerRange.getFirst(), containerRange.getSecond(), false))
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

    private final Tuple<Integer, Integer> findNextInventoryIndices(@NotNull final SlotBlockOut slotBlockOut)
    {
        final Integer startIndex = findStartIndexOfNextInventory(slotBlockOut);
        final SlotBlockOut startSlot = (SlotBlockOut) this.inventorySlots.get(0);
        final Integer endIndex = findStartIndexOfNextInventory(startSlot);

        //Check if the entire container is populated with one inventory.
        if (startIndex == 0 && startIndex == endIndex)
        {
            return new Tuple<>(0, this.inventorySlots.size());
        }

        return new Tuple<>(startIndex, endIndex);
    }

    private final Integer findStartIndexOfNextInventory(@NotNull final SlotBlockOut slotBlockOut)
    {
        final ResourceLocation currentInventory = slotBlockOut.getUiSlotInstance().getInventoryId();
        ResourceLocation workingInventory = slotBlockOut.getUiSlotInstance().getInventoryId();
        SlotBlockOut workingSlot = slotBlockOut;

        while (currentInventory.equals(workingInventory))
        {
            workingSlot =
              (SlotBlockOut) ((workingSlot.slotNumber + 1) == this.inventorySlots.size() ? this.inventorySlots.get(0) : this.inventorySlots.get(workingSlot.slotNumber + 1));

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

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
    {
        return true;
    }

    @Override
    protected boolean mergeItemStack(@Nonnull ItemStack itemStack, int slotMin, int slotMax, boolean ascending)
    {
        boolean slotFound = false;
        int currentSlotIndex = ascending ? slotMax - 1 : slotMin;
        net.minecraft.inventory.Slot slot;
        ItemStack stackInSlot;
        if (itemStack.isStackable())
        {
            while (itemStack.getCount() > 0 && (!ascending && currentSlotIndex < slotMax || ascending && currentSlotIndex >= slotMin))
            {
                slot = this.inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();
                if (slot.isItemValid(itemStack) && ItemStackHelper.equalsIgnoreStackSize(itemStack, stackInSlot))
                {
                    int combinedStackSize = stackInSlot.getCount() + itemStack.getCount();
                    int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());
                    if (combinedStackSize <= slotStackSizeLimit)
                    {
                        itemStack.setCount(0);
                        stackInSlot.setCount(combinedStackSize);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                    else if (stackInSlot.getCount() < slotStackSizeLimit)
                    {
                        itemStack.shrink(slotStackSizeLimit - stackInSlot.getCount());
                        stackInSlot.setCount(slotStackSizeLimit);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }
                if (ascending && currentSlotIndex == this.inventorySlots.size() - 1)
                {
                    currentSlotIndex = 0;
                }
                else if (!ascending && currentSlotIndex == 0)
                {
                    currentSlotIndex = this.inventorySlots.size() - 1;
                }
                else
                {
                    currentSlotIndex += ascending ? -1 : 1;
                }
            }
        }
        if (itemStack.getCount() > 0)
        {
            currentSlotIndex = ascending ? slotMax - 1 : slotMin;
            while (!ascending && currentSlotIndex < slotMax || ascending && currentSlotIndex >= slotMin)
            {
                slot = this.inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();
                if (slot.isItemValid(itemStack) && stackInSlot.isEmpty())
                {
                    slot.putStack(ItemStackHelper.cloneItemStack(itemStack, Math.min(itemStack.getCount(), slot.getSlotStackLimit())));
                    slot.onSlotChanged();
                    if (!slot.getStack().isEmpty())
                    {
                        itemStack.shrink(slot.getStack().getCount());
                        slotFound = true;
                    }
                    break;
                }
                if (ascending && currentSlotIndex == this.inventorySlots.size() - 1)
                {
                    currentSlotIndex = 0;
                }
                else if (!ascending && currentSlotIndex == 0)
                {
                    currentSlotIndex = this.inventorySlots.size() - 1;
                }
                else
                {
                    currentSlotIndex += ascending ? -1 : 1;
                }
            }
        }
        return slotFound;
    }
}
