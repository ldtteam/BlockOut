package com.ldtteam.blockout.inventory.slot;

import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.jvoxelizer.core.logic.TypedPipelineElementContext;
import com.ldtteam.jvoxelizer.inventory.slot.ISlotItemHandler;
import com.ldtteam.jvoxelizer.inventory.slot.logic.builder.ISlotItemHandlerBuilder;
import com.ldtteam.jvoxelizer.inventory.slot.logic.builder.contexts.CanTakeStackContext;
import net.minecraftforge.items.IItemHandler;

public final class BlockOutSlotLogic
{

    public static ISlotItemHandler<BlockOutSlotData> create(final IItemHandler itemHandler, Slot uiSlotInstance)
    {
        final BlockOutSlotData data = new BlockOutSlotData(uiSlotInstance);
        final ISlotItemHandlerBuilder<?, BlockOutSlotData, ISlotItemHandler<BlockOutSlotData>> builder = ISlotItemHandlerBuilder.create(itemHandler,
          uiSlotInstance.getInventoryIndex(),
          (int) (uiSlotInstance.getAbsoluteBoundingBox().getLocalOrigin().getX() + 1 - uiSlotInstance.getParent()
                                                                                         .getUiManager()
                                                                                         .getHost()
                                                                                         .getLocalBoundingBox()
                                                                                         .getLocalOrigin()
                                                                                         .getX()),
          (int) (uiSlotInstance.getAbsoluteBoundingBox().getLocalOrigin().getY() + 1 - uiSlotInstance.getParent()
                                                                                         .getUiManager()
                                                                                         .getHost()
                                                                                         .getLocalBoundingBox()
                                                                                         .getLocalOrigin()
                                                                                         .getY()),
          data);

        return builder.CanTakeStack(BlockOutSlotLogic::canTakeStack).build(data);
    }

    public static boolean canTakeStack(final TypedPipelineElementContext<CanTakeStackContext, Boolean, ISlotItemHandler<BlockOutSlotData>, BlockOutSlotData> context)
    {
        return context.getInstanceData().getUiSlotInstance().isEnabled() && context.callSuper();
    }
}
