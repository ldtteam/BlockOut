package com.minecolonies.blockout.inventory;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.element.simple.Slot;
import com.minecolonies.blockout.util.math.BoundingBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class BlockOutContainer extends Container
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private final IUIElementHost root;

    public BlockOutContainer(@NotNull final IGuiKey key, @NotNull final IUIElementHost root)
    {
        this.key = key;
        this.root = root;

        initializeSlots();
    }

    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }

    @NotNull
    public IUIElementHost getRoot()
    {
        return root;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer playerIn)
    {
        return true;
    }

    private void initializeSlots()
    {
        int slotIndex = 0;
        final List<Slot> slots =
          getRoot().getAllCombinedChildElements().values().stream()
            .filter(element -> element instanceof Slot)
            .map(element -> (Slot) element).collect(Collectors.toList());

        for (Slot slot :
          slots)
        {
            final IItemHandler itemHandler = getKey().getItemHandlerManager().getItemHandlerFromId(slot.getInventoryId());
            if (itemHandler == null)
            {
                throw new IllegalStateException("The slot does not have a known item handler");
            }

            slot.setSlotIndex(slotIndex);
            final BoundingBox absoluteBoundingBox = slot.getAbsoluteBoundingBox();
            final SlotItemHandler slotItemHandler = new SlotItemHandler(itemHandler,
              slotIndex++,
              (int) absoluteBoundingBox.getLocalOrigin().getX(),
              (int) absoluteBoundingBox.getLocalOrigin().getY())
            {
                @Override
                public boolean canTakeStack(final EntityPlayer playerIn)
                {
                    return slot.isEnabled() && super.canTakeStack(playerIn);
                }
            };

            addSlotToContainer(slotItemHandler);
        }
    }

    public void reinitializeSlots()
    {
        this.inventorySlots.clear();
        initializeSlots();

        detectAndSendChanges();
    }
}
