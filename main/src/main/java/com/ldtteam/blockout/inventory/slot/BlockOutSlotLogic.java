package com.ldtteam.blockout.inventory.slot;

import com.ldtteam.blockout.element.simple.Slot;

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
