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
    private Vector2d guiOffset = new Vector2d(0,0);
    @NotNull
    private Vector2d guiSize = new Vector2d(176, 166);

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

    public double getGuiTop()
    {
        return guiOffset.getY();
    }

    public double getGuiLeft()
    {
        return guiOffset.getX();
    }

    public void setGuiOffset(final Vector2d guiOffset)
    {
        this.guiOffset = guiOffset;
    }

    public void setGuiOffset(final double top, final double left)
    {
        this.guiOffset = new Vector2d(left, top);
    }

    public double getXSize()
    {
        return this.guiSize.getX();
    }

    public double getYSize()
    {
        return this.guiSize.getY();
    }

    public void setXSize(final double xSize)
    {
        this.setGuiSize(xSize, getYSize());
    }

    public void setYSize(final double ySize)
    {
        this.setGuiSize(getXSize(), ySize);
    }

    public void setGuiSize(@NotNull final Vector2d guiSize)
    {
        this.guiSize = guiSize;
    }

    public void setGuiSize(final double xSize, final double ySize)
    {
        this.guiSize = new Vector2d(xSize, ySize);
    }
}
