package com.ldtteam.blockout.inventory;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.blockout.inventory.slot.BlockOutSlotData;
import com.ldtteam.blockout.inventory.slot.BlockOutSlotLogic;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.itemstack.ItemStackHelper;
import com.ldtteam.jvoxelizer.core.logic.TypedPipelineElementContext;
import com.ldtteam.jvoxelizer.entity.living.player.IPlayerEntity;
import com.ldtteam.jvoxelizer.inventory.IContainer;
import com.ldtteam.jvoxelizer.inventory.logic.builder.IContainerBuilder;
import com.ldtteam.jvoxelizer.inventory.logic.builder.contexts.MergeItemStackContext;
import com.ldtteam.jvoxelizer.inventory.logic.builder.contexts.TransferStackInSlotContext;
import com.ldtteam.jvoxelizer.inventory.slot.ISlot;
import com.ldtteam.jvoxelizer.inventory.slot.ISlotItemHandler;
import com.ldtteam.jvoxelizer.item.IItemStack;
import com.ldtteam.jvoxelizer.item.handling.IItemHandler;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.jvoxelizer.util.tuple.ITuple;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class BlockOutContainerLogic
{
    public static IContainer<BlockOutContainerData> create(@NotNull final IGuiKey key, @NotNull final IUIElementHost root, @NotNull final int windowId)
    {
        final IContainerBuilder<?, BlockOutContainerData, IContainer<BlockOutContainerData>> builder = IContainerBuilder.create();

        final IContainer<BlockOutContainerData> container = builder
          .TransferStackInSlot(BlockOutContainerLogic::TransferStackInSlot)
          .MergeItemStack(BlockOutContainerLogic::MergeItemStack)
          .build(new BlockOutContainerData(key, root));

        initializeSlots(container);

        return container;
    }

    private static void initializeSlots(final IContainer<BlockOutContainerData> container)
    {
        int slotIndex = 0;
        final List<Slot> slots = container.getInstanceData().
          getRoot().getAllCombinedChildElements().values().stream()
            .filter(element -> element instanceof Slot)
            .map(element -> (Slot) element).collect(Collectors.toCollection(LinkedList::new));

        for (Slot slot :
          slots)
        {
            final IItemHandler itemHandler = container.getInstanceData().getKey().getItemHandlerManager().getItemHandlerFromId(slot.getInventoryId());
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

                container.getInstanceData().getKey().getItemHandlerManager().getAllItemHandlerIds().forEach(loc -> errorMessageBuilder
                                                                                         .append("  * ")
                                                                                         .append(loc)
                                                                                         .append("\n"));

                Log.getLogger().error(errorMessageBuilder.toString());
                throw new IllegalArgumentException("Failed to find IItemHandler for Slot.");
            }

            slot.setSlotIndex(slotIndex++);
            final ISlotItemHandler<BlockOutSlotData> slotItemHandler = BlockOutSlotLogic.create(slot);

            container.addSlotToContainer(slotItemHandler);
        }
    }

    public static void reinitializeSlots(final IContainer<BlockOutContainerData> container)
    {
        container.getInventorySlots().clear();
        container.getInventoryItemStacks().clear();
        initializeSlots(container);

        container.detectAndSendChanges();
    }

    private static ITuple<Integer, Integer> findNextInventoryIndices(final IContainer<BlockOutContainerData> container, final ISlotItemHandler<BlockOutSlotData> slotBlockOut)
    {
        final Integer startIndex = findStartIndexOfNextInventory(container, slotBlockOut);
        final ISlotItemHandler<BlockOutSlotData> startSlot = (ISlotItemHandler<BlockOutSlotData>) container.getInventorySlots().get(0);
        final Integer endIndex = findStartIndexOfNextInventory(container, startSlot);

        //Check if the entire container is populated with one inventory.
        if (startIndex == 0 && startIndex == endIndex)
        {
            return ITuple.create(0, container.getInventorySlots().size());
        }

        return ITuple.create(startIndex, endIndex);
    }

    private static Integer findStartIndexOfNextInventory(final IContainer<BlockOutContainerData> container, final ISlotItemHandler<BlockOutSlotData> slotBlockOut)
    {
        final IIdentifier currentInventory = slotBlockOut.getInstanceData().getUiSlotInstance().getInventoryId();
        IIdentifier workingInventory = slotBlockOut.getInstanceData().getUiSlotInstance().getInventoryId();
        ISlotItemHandler<BlockOutSlotData> workingSlot = slotBlockOut;

        while (currentInventory.equals(workingInventory))
        {
            workingSlot =
               ((workingSlot.getSlotNumber() + 1) == container.getInventorySlots().size() ? (ISlotItemHandler<BlockOutSlotData>) container.getInventorySlots().get(0)
                  : (ISlotItemHandler<BlockOutSlotData>) container.getInventorySlots().get(workingSlot.getSlotNumber() + 1));

            if (workingSlot == slotBlockOut)
            {
                break;
            }

            workingInventory = workingSlot.getInstanceData().getUiSlotInstance().getInventoryId();
        }

        if (workingSlot == slotBlockOut)
        {
            return 0;
        }

        return workingSlot.getSlotNumber();
    }

    private static IItemStack TransferStackInSlot(final TypedPipelineElementContext<TransferStackInSlotContext, IItemStack, IContainer<BlockOutContainerData>, BlockOutContainerData> context)
    {
        IItemStack newItemStack = IItemStack.create();
        ISlot<?> slot = context.getInstance().getInventorySlots().get(context.getContext().getIndex());
        if (slot instanceof ISlotItemHandler && slot.getInstanceData() instanceof BlockOutSlotData)
        {
            ISlotItemHandler<BlockOutSlotData> slotBlockOut = (ISlotItemHandler<BlockOutSlotData>) slot;
            if (slotBlockOut.getHasStack())
            {
                IItemStack itemStack = slotBlockOut.getStack();
                newItemStack = itemStack.copy();
                final ITuple<Integer, Integer> containerRange = findNextInventoryIndices(context.getInstance(), slotBlockOut);

                if (!context.getInstance().mergeItemStack(itemStack, containerRange.getFirst(), containerRange.getSecond(), false))
                {
                    return IItemStack.create();
                }
                if (itemStack.getCount() == 0 || itemStack.isEmpty())
                {
                    slotBlockOut.putStack(IItemStack.create());
                }
                else
                {
                    slotBlockOut.onSlotChanged();
                }
            }
        }

        return newItemStack;
    }

    private static boolean MergeItemStack(final TypedPipelineElementContext<MergeItemStackContext, Boolean, IContainer<BlockOutContainerData>, BlockOutContainerData> context)
    {
        boolean slotFound = false;
        int currentSlotIndex = context.getContext().getReverseDirection() ? context.getContext().getEndIndex() - 1 : context.getContext().getStartIndex();
        ISlot<?> slot;
        IItemStack stackInSlot;
        if (context.getContext().getStack().isStackable())
        {
            while (context.getContext().getStack().getCount() > 0 && (!context.getContext().getReverseDirection() && currentSlotIndex < context.getContext().getEndIndex() || context.getContext().getReverseDirection() && currentSlotIndex >= context.getContext().getStartIndex()))
            {
                slot = context.getInstance().getInventorySlots().get(currentSlotIndex);
                stackInSlot = slot.getStack();
                if (slot.isItemValid(context.getContext().getStack()) && ItemStackHelper.equalsIgnoreStackSize(context.getContext().getStack(), stackInSlot))
                {
                    int combinedStackSize = stackInSlot.getCount() + context.getContext().getStack().getCount();
                    int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());
                    if (combinedStackSize <= slotStackSizeLimit)
                    {
                        context.getContext().getStack().setCount(0);
                        stackInSlot.setCount(combinedStackSize);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                    else if (stackInSlot.getCount() < slotStackSizeLimit)
                    {
                        context.getContext().getStack().shrink(slotStackSizeLimit - stackInSlot.getCount());
                        stackInSlot.setCount(slotStackSizeLimit);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }
                if (context.getContext().getReverseDirection() && currentSlotIndex == context.getInstance().getInventorySlots().size() - 1)
                {
                    currentSlotIndex = 0;
                }
                else if (!context.getContext().getReverseDirection() && currentSlotIndex == 0)
                {
                    currentSlotIndex = context.getInstance().getInventorySlots().size() - 1;
                }
                else
                {
                    currentSlotIndex += context.getContext().getReverseDirection() ? -1 : 1;
                }
            }
        }
        if (context.getContext().getStack().getCount() > 0)
        {
            currentSlotIndex = context.getContext().getReverseDirection() ? context.getContext().getEndIndex() - 1 : context.getContext().getStartIndex();
            while (!context.getContext().getReverseDirection() && currentSlotIndex < context.getContext().getEndIndex() || context.getContext().getReverseDirection() && currentSlotIndex >= context.getContext().getStartIndex())
            {
                slot = context.getInstance().getInventorySlots().get(currentSlotIndex);
                stackInSlot = slot.getStack();
                if (slot.isItemValid(context.getContext().getStack()) && stackInSlot.isEmpty())
                {
                    slot.putStack(ItemStackHelper.cloneItemStack(context.getContext().getStack(), Math.min(context.getContext().getStack().getCount(), slot.getSlotStackLimit())));
                    slot.onSlotChanged();
                    if (!slot.getStack().isEmpty())
                    {
                        context.getContext().getStack().shrink(slot.getStack().getCount());
                        slotFound = true;
                    }
                    break;
                }
                if (context.getContext().getReverseDirection() && currentSlotIndex == context.getInstance().getInventorySlots().size() - 1)
                {
                    currentSlotIndex = 0;
                }
                else if (!context.getContext().getReverseDirection() && currentSlotIndex == 0)
                {
                    currentSlotIndex = context.getInstance().getInventorySlots().size() - 1;
                }
                else
                {
                    currentSlotIndex += context.getContext().getReverseDirection() ? -1 : 1;
                }
            }
        }
        return slotFound;
    }

}
