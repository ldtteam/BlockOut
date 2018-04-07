package com.minecolonies.blockout.inventory;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import org.jetbrains.annotations.NotNull;

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


}
