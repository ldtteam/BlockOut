package com.ldtteam.blockout.inventory.slot;

import com.ldtteam.blockout.element.simple.Slot;

public class BlockOutSlotData
{
    private final Slot uiSlotInstance;

    public BlockOutSlotData(final Slot uiSlotInstance) {this.uiSlotInstance = uiSlotInstance;}

    /*public SlotBlockOut(@NotNull final IItemHandler itemHandler, @NotNull final Slot uiSlotInstance)
    {
        super(itemHandler,
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
                                                                                         .getY()));

        this.uiSlotInstance = uiSlotInstance;
    }*/

    /*@Override
    public boolean canTakeStack(final EntityPlayer playerIn)
    {
        return getUiSlotInstance().isEnabled() && super.canTakeStack(playerIn);
    }*/

    public Slot getUiSlotInstance()
    {
        return uiSlotInstance;
    }
}
