package com.minecolonies.blockout.element.core;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
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
    private IUIElementHost parent;

    @NotNull
    private IDependencyObject<EnumSet<Alignment>> alignments  = DependencyObjectHelper.createFromValue(EnumSet.of(Alignment.NONE));
    @NotNull
    private IDependencyObject<Dock>             dock        = DependencyObjectHelper.createFromValue(Dock.NONE);
    @NotNull
    private IDependencyObject<AxisDistance>       margin      = DependencyObjectHelper.createFromValue(new AxisDistance());
    @NotNull
    private IDependencyObject<Vector2d>           elementSize = DependencyObjectHelper.createFromValue(new Vector2d());

    @NotNull
    private BoundingBox localBoundingBox;
    @NotNull
    private BoundingBox absoluteBoundingBox;

    @NotNull
    private IDependencyObject<Object> dataContext = DependencyObjectHelper.createFromValue(new Object());

    @NotNull
    private IDependencyObject<Boolean> visible = DependencyObjectHelper.createFromValue(true);
    @NotNull
    private IDependencyObject<Boolean> enabled = DependencyObjectHelper.createFromValue(true);

    public AbstractSimpleUIElement(@NotNull final String id, @NotNull final IUIElementHost parent)
    {
        this.id = id;
        this.parent = parent;
    }

    public AbstractSimpleUIElement(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        this.id = id;
        this.parent = parent;
        this.alignments = alignments;
        this.dock = dock;
        this.margin = margin;
        this.elementSize = elementSize;
        this.dataContext = dataContext;
        this.visible = visible;
        this.enabled = enabled;
        
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
        return alignments.get(getDataContext());
    }

    @Override
    public void setAlignment(@NotNull final EnumSet<Alignment> alignment)
    {
        this.alignments = DependencyObjectHelper.createFromValue(alignment);
    }

    @Override
    public Dock getDock()
    {
        return dock.get(getDataContext());
    }

    @Override
    public void setDock(@NotNull final Dock dock)
    {
        this.dock = DependencyObjectHelper.createFromValue(dock);
    }

    @Override
    public AxisDistance getMargin()
    {
        return margin.get(getDataContext());
    }

    @Override
    public void setMargin(@NotNull final AxisDistance margin)
    {
        this.margin = DependencyObjectHelper.createFromValue(margin);
    }

    @Override
    public Vector2d getElementSize()
    {
        return elementSize.get(getDataContext());
    }

    @Override
    public void setElementSize(@NotNull final Vector2d elementSize)
    {
        this.elementSize = DependencyObjectHelper.createFromValue(elementSize);
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
    }

    @Override
    public boolean isVisible()
    {
        return visible.get(getDataContext());
    }

    @Override
    public void setVisible(final boolean visible)
    {
        this.visible = DependencyObjectHelper.createFromValue(visible);
    }

    @Override
    public boolean isEnabled()
    {
        return enabled.get(getDataContext());
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
        this.enabled = DependencyObjectHelper.createFromValue(enabled);
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        return dataContext.get(parent.getDataContext());
    }

    @Override
    public void setDataContext(@Nullable final Object dataContext)
    {
        this.dataContext = DependencyObjectHelper.createFromValue(dataContext);
    }

    @Override
    public void update()
    {
        updateBoundingBoxes();
    }
}