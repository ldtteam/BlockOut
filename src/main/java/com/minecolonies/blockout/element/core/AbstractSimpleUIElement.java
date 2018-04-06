package com.minecolonies.blockout.element.core;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class AbstractSimpleUIElement implements IUIElement
{
    @NotNull
    private final String         id;
    @NotNull
    private       IUIElementHost parent;

    @NotNull
    private EnumSet<Alignment> alignments;
    @NotNull
    private Dock               dock;
    @NotNull
    private AxisDistance       margin;
    @NotNull
    private Vector2d           elementSize;

    @NotNull
    private BoundingBox localBoundingBox;
    @NotNull
    private BoundingBox absoluteBoundingBox;

    @Nullable
    private Object dataContext;

    private boolean visible;
    private boolean enabled;

    public AbstractSimpleUIElement(
      @NotNull final String id,
      @NotNull final EnumSet<Alignment> alignments,
      @NotNull final Dock dock,
      @NotNull final AxisDistance margin,
      @NotNull final Vector2d elementSize,
      @NotNull final IUIElementHost parent,
      final boolean visible,
      final boolean enabled)
    {
        this.id = id;
        this.alignments = alignments;
        this.dock = dock;
        this.margin = margin;
        this.elementSize = elementSize;
        this.parent = parent;
        this.visible = visible;
        this.enabled = enabled;

        updateBoundingBoxes();
    }

    private void updateLocalBoundingBox()
    {
        //If we have no parent we see our default elementSize as parent.
        //Else grab the elementSize from the parent.
        final Vector2d parentSize = getParent() != this ? getParent().getLocalInternalBoundingBox().getSize() : getElementSize();

        final double marginLeft = Alignment.LEFT.isActive(this) ? getMargin().getLeft().orElse(0d) : 0d;
        final double marginTop = Alignment.TOP.isActive(this) ? getMargin().getTop().orElse(0d) : 0d;
        final double marginRight = Alignment.RIGHT.isActive(this) ? getMargin().getRight().orElse(0d) : 0d;
        final double marginBottom = Alignment.BOTTOM.isActive(this) ? getMargin().getBottom().orElse(0d) : 0d;

        final Vector2d origin = new Vector2d(marginLeft, marginTop);

        final Vector2d size = new Vector2d(parentSize.getX() - (marginLeft + marginRight), parentSize.getY() - (marginTop + marginBottom));

        this.localBoundingBox = new BoundingBox(origin, size);
        this.localBoundingBox = getDock().apply(this, this.localBoundingBox);
    }

    private void updateAbsoluteBoundingBox()
    {
        if (getParent() == this)
        {
            this.absoluteBoundingBox = getLocalBoundingBox();
        }

        final BoundingBox parentAbsoluteBindingBox = getParent().getAbsoluteInternalBoundingBox();
        this.absoluteBoundingBox = new BoundingBox(parentAbsoluteBindingBox.getLocalOrigin().move(getLocalBoundingBox().getLocalOrigin()), getLocalBoundingBox().getSize());
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public EnumSet<Alignment> getAlignment()
    {
        return alignments;
    }

    @Override
    public void setAlignment(@NotNull final EnumSet<Alignment> alignment)
    {
        this.alignments = alignment;
        updateBoundingBoxes();
    }

    @Override
    public Dock getDock()
    {
        return dock;
    }

    @Override
    public void setDock(@NotNull final Dock dock)
    {
        this.dock = dock;
        updateBoundingBoxes();
    }

    @Override
    public AxisDistance getMargin()
    {
        return margin;
    }

    @Override
    public void setMargin(@NotNull final AxisDistance margin)
    {
        this.margin = margin;
        updateBoundingBoxes();
    }

    @Override
    public Vector2d getElementSize()
    {
        return elementSize;
    }

    @Override
    public void setElementSize(@NotNull final Vector2d elementSize)
    {
        this.elementSize = elementSize;
        updateBoundingBoxes();
    }

    @Override
    public void updateBoundingBoxes()
    {
        updateLocalBoundingBox();
        updateAbsoluteBoundingBox();
    }

    @NotNull
    @Override
    public BoundingBox getLocalBoundingBox()
    {
        return localBoundingBox;
    }

    @NotNull
    @Override
    public BoundingBox getAbsoluteBoundingBox()
    {
        return absoluteBoundingBox;
    }

    @NotNull
    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }

    @Override
    public void setParent(@NotNull final IUIElementHost parent)
    {
        this.parent = parent;
        updateBoundingBoxes();
    }

    @Override
    public boolean isVisible()
    {
        return visible;
    }

    @Override
    public void setVisible(final boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        return dataContext;
    }

    @Override
    public void setDataContext(@Nullable final Object dataContext)
    {
        this.dataContext = dataContext;
    }
}
