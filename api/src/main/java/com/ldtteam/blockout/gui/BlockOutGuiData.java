package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

public final class BlockOutGuiData
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private       IUIElementHost root;

    @NotNull
    private Vector2d scaleFactor = new Vector2d(1, 1);

    private boolean isDrawing = false;

    public BlockOutGuiData(@NotNull final IGuiKey key, @NotNull final IUIElementHost root)
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

    @NotNull
    public Vector2d getScaleFactor()
    {
        return scaleFactor;
    }

    public void setScaleFactor(@NotNull final Vector2d scaleFactor)
    {
        this.scaleFactor = scaleFactor;
    }

    public boolean isDrawing()
    {
        return isDrawing;
    }

    public void setDrawing(final boolean drawing)
    {
        isDrawing = drawing;
    }
}
