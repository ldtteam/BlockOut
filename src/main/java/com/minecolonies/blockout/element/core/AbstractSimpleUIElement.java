package com.minecolonies.blockout.element.core;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.builder.core.builder.IBlockOutUIElementConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.event.IEventHandler;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class AbstractSimpleUIElement implements IUIElement
{
    @NotNull
    private final ResourceLocation type;
    @NotNull
    private final String           id;
    @NotNull
    private       IUIElementHost   parent;

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

    public AbstractSimpleUIElement(@NotNull final ResourceLocation type, @NotNull final String id, @NotNull final IUIElementHost parent)
    {
        this.type = type;
        this.id = id;
        this.parent = parent;
    }

    public AbstractSimpleUIElement(
      @NotNull final ResourceLocation type,
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
        this.type = type;
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

    @Override
    public void update(@NotNull final IUpdateManager manager)
    {
        if (updateBoundingBoxes())
        {
            manager.markDirty();
        }
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
    public ResourceLocation getType()
    {
        return type;
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

    private boolean updateBoundingBoxes()
    {
        boolean updated = false;

        if (updateLocalBoundingBox())
        {
            updated = true;
        }
        updateAbsoluteBoundingBox();

        return updated;
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

    private boolean updateLocalBoundingBox()
    {
        final BoundingBox currentBoundingBox = this.localBoundingBox;

        //If we have no parent we see our default size as parent.
        //Else grab the size from the parent.
        final Vector2d parentSize = getParent() != this ? getParent().getLocalBoundingBox().getSize() : new Vector2d(Double.MAX_VALUE, Double.MAX_VALUE);

        double marginLeft = getMargin().getLeft().orElse(0d);
        double marginTop = getMargin().getTop().orElse(0d);
        double marginRight = getMargin().getRight().orElse(0d);
        double marginBottom = getMargin().getBottom().orElse(0d);

        double width = getElementSize().getX();
        double height = getElementSize().getY();

        if (Alignment.LEFT.isActive(this) && Alignment.RIGHT.isActive(this))
        {
            width = parentSize.getX() - marginLeft - marginRight;
        }
        else if (Alignment.RIGHT.isActive(this))
        {
            marginLeft = parentSize.getX() - width - marginRight;
        }
        else
        {
            marginRight = parentSize.getX() - width - marginLeft;
        }

        if (Alignment.TOP.isActive(this) && Alignment.BOTTOM.isActive(this))
        {
            height = parentSize.getY() - marginTop - marginBottom;
        }
        else if (Alignment.BOTTOM.isActive(this))
        {
            marginTop = parentSize.getY() - height - marginBottom;
        }
        else
        {
            marginBottom = parentSize.getY() - height - marginTop;
        }

        final Vector2d origin = new Vector2d(marginLeft, marginTop).nullifyNegatives();

        final Vector2d size =
          new Vector2d(width, height).nullifyNegatives();

        this.localBoundingBox = new BoundingBox(origin, size);
        this.localBoundingBox = getDock().apply(this, this.localBoundingBox);

        return currentBoundingBox == null || !currentBoundingBox.equals(this.localBoundingBox);
    }

    public static abstract class SimpleControlConstructionDataBuilder<B extends SimpleControlConstructionDataBuilder<B, S>, S extends AbstractSimpleUIElement>
      implements IBlockOutUIElementConstructionDataBuilder<B, S>
    {

        private final String                              controlId;
        private final IBlockOutGuiConstructionDataBuilder data;
        private final Class<S>                            controlClass;

        protected SimpleControlConstructionDataBuilder(final String controlId, final IBlockOutGuiConstructionDataBuilder data, final Class<S> controlClass)
        {
            this.controlId = controlId;
            this.data = data;
            this.controlClass = controlClass;
        }

        @NotNull
        @Override
        public B withDependency(@NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency)
        {
            data.withDependency(controlId, fieldName, dependency);
            return (B) this;
        }

        @NotNull
        @Override
        public <A> B withEventHandler(
          @NotNull final String eventName, @NotNull final Class<A> argumentTypeClass, @NotNull final IEventHandler<S, A> eventHandler)
        {
            data.withEventHandler(controlId, eventName, controlClass, argumentTypeClass, eventHandler);
            return (B) this;
        }

        @NotNull
        @Override
        public IBlockOutGuiConstructionDataBuilder done()
        {
            return data;
        }
    }
}
