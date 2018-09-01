package com.minecolonies.blockout.inventory;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.element.simple.Slot;
import com.minecolonies.blockout.util.Log;
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

    public BlockOutContainer(@NotNull final IGuiKey key, @NotNull final IUIElementHost root, @NotNull final int windowId)
    {
        this.key = key;
        this.root = root;
        this.windowId = windowId;

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
            final BoundingBox absoluteBoundingBox = slot.getAbsoluteBoundingBox();
            final SlotItemHandler slotItemHandler = new SlotItemHandler(
              itemHandler,
              slot.getInventoryIndex(),
              (int) absoluteBoundingBox.getLocalOrigin().getX() + 1,
              (int) absoluteBoundingBox.getLocalOrigin().getY() + 1)
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
