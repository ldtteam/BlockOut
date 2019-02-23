package com.ldtteam.blockout.inventory;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import org.jetbrains.annotations.NotNull;

public class BlockOutContainerData
{
    @NotNull
    private final IGuiKey        key;

    @NotNull
    private       IUIElementHost root;

    public BlockOutContainerData(@NotNull final IGuiKey key, @NotNull final IUIElementHost root)
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

    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
    }
}
