package com.minecolonies.blockout.inventory.slot;

import com.minecolonies.blockout.element.simple.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotBlockOut extends SlotItemHandler
{
    private final Slot uiSlotInstance;

    public SlotBlockOut(@NotNull final IItemHandler itemHandler, @NotNull final Slot uiSlotInstance)
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

    @Override
    public boolean canTakeStack(final EntityPlayer playerIn)
    {
        return getUiSlotInstance().isEnabled() && super.canTakeStack(playerIn);
    }

    public Slot getUiSlotInstance()
    {
        return uiSlotInstance;
    }
}
