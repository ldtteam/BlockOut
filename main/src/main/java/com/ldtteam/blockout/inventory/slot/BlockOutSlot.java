package com.ldtteam.blockout.inventory.slot;

import com.ldtteam.blockout.element.simple.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlockOutSlot extends SlotItemHandler
{

    private final Slot uiSlotInstance;

    public BlockOutSlot(final IItemHandler itemHandler, final Slot uiSlotInstance)
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
    }

    public Slot getUiSlotInstance()
    {
        return uiSlotInstance;
    }
}
