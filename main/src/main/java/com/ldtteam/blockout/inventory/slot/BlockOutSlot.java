package com.ldtteam.blockout.inventory.slot;

import com.ldtteam.blockout.element.simple.IInventorySlotUIElement;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlockOutSlot extends SlotItemHandler
{

    private final IInventorySlotUIElement uiSlotInstance;

    public BlockOutSlot(final IItemHandler itemHandler, final IInventorySlotUIElement uiSlotInstance)
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

    public IInventorySlotUIElement getUiSlotInstance()
    {
        return uiSlotInstance;
    }
}
