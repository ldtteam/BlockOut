package com.ldtteam.blockout.inventory.slot;

import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.jvoxelizer.core.logic.TypedPipelineElementContext;
import com.ldtteam.jvoxelizer.inventory.slot.ISlot;
import com.ldtteam.jvoxelizer.inventory.slot.ISlotItemHandler;
import com.ldtteam.jvoxelizer.inventory.slot.logic.builder.ISlotItemHandlerBuilder;
import com.ldtteam.jvoxelizer.inventory.slot.logic.builder.contexts.CanTakeStackContext;

public final class BlockOutSlotLogic
{

    public static ISlotItemHandler<BlockOutSlotData> create(Slot uiSlotInstance)
    {
        final ISlotItemHandlerBuilder<?, BlockOutSlotData, ISlotItemHandler<BlockOutSlotData>> builder = ISlotItemHandlerBuilder.create();

        return builder.CanTakeStack(BlockOutSlotLogic::canTakeStack).build(new BlockOutSlotData(uiSlotInstance));
    }

    public static boolean canTakeStack(final TypedPipelineElementContext<CanTakeStackContext, Boolean, ISlotItemHandler<BlockOutSlotData>, BlockOutSlotData> context)
    {
        return context.getInstanceData().getUiSlotInstance().isEnabled() && context.callSuper();
    }
}
