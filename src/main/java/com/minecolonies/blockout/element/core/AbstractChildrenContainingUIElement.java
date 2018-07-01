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
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;

public abstract class AbstractChildrenContainingUIElement extends HashMap<String, IUIElement> implements IUIElementHost
{
    @NotNull
    protected final ResourceLocation type;

    @NotNull
    protected IDependencyObject<ResourceLocation> style = DependencyObjectHelper.createFromValue(new ResourceLocation(Constants.MOD_ID, Constants.Styles.CONST_MINECRAFT));

    @NotNull
    protected final String           id;
    @NotNull
    protected       IUIElementHost   parent;

    @NotNull
    protected IDependencyObject<EnumSet<Alignment>> alignments  = DependencyObjectHelper.createFromValue(EnumSet.of(Alignment.NONE));
    @NotNull
    protected IDependencyObject<Dock>               dock        = DependencyObjectHelper.createFromValue(Dock.NONE);
    @NotNull
    protected IDependencyObject<AxisDistance>       margin      = DependencyObjectHelper.createFromValue(new AxisDistance());
    @NotNull
    protected IDependencyObject<Vector2d>           elementSize = DependencyObjectHelper.createFromValue(new Vector2d());
    @NotNull
    protected IDependencyObject<AxisDistance>       padding     = DependencyObjectHelper.createFromValue(new AxisDistance());

    @NotNull
    protected BoundingBox localBoundingBox;
    @NotNull
    protected BoundingBox absoluteBoundingBox;
    @NotNull
    protected BoundingBox localInternalBoundingBox;
    @NotNull
    protected BoundingBox absoluteInternalBoundingBox;

    @NotNull
    protected IDependencyObject<Object> dataContext = DependencyObjectHelper.createFromValue(new Object());

    @NotNull
    protected IDependencyObject<Boolean> visible = DependencyObjectHelper.createFromValue(true);
    @NotNull
    protected IDependencyObject<Boolean> enabled = DependencyObjectHelper.createFromValue(true);

    public AbstractChildrenContainingUIElement(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled
    )
    {
        this.type = type;
        this.style = style;
        this.id = id;
        this.parent = parent;
        this.alignments = alignments;
        this.dock = dock;
        this.margin = margin;
        this.elementSize = elementSize;
        this.padding = padding;
        this.dataContext = dataContext;
        this.visible = visible;
        this.enabled = enabled;
    }

    public AbstractChildrenContainingUIElement(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent)
    {
        this.type = type;
        this.style = style;
        this.id = id;
        this.parent = parent;
    }

    @NotNull
    @Override
    public ResourceLocation getType()
    {
        return type;
    }

    /**
     * Returns the style of this IUIElement.
     *
     * @return The style of the element.
     */
    @NotNull
    @Override
    public ResourceLocation getStyleId()
    {
        return style.get(getDataContext());
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        if (updateBoundingBoxes())
        {
            updateManager.markDirty();
        }
    }

    @Override
    public EnumSet<Alignment> getAlignment()
    {
        return alignments.get(getDataContext());
    }

    private void updateAbsoluteBoundingBox()
    {
        if (getParent() == this)
        {
            this.absoluteBoundingBox = getLocalBoundingBox();
            return;
        }

        final BoundingBox parentAbsoluteBindingBox = getParent().getAbsoluteInternalBoundingBox();
        this.absoluteBoundingBox = new BoundingBox(parentAbsoluteBindingBox.getLocalOrigin().move(getLocalBoundingBox().getLocalOrigin()), getLocalBoundingBox().getSize());
    }

    @Override
    public void setAlignment(@NotNull final EnumSet<Alignment> alignment)
    {
        this.alignments = DependencyObjectHelper.createFromValue(alignment);
    }

    @Override
    public AxisDistance getPadding()
    {
        return padding.get(getDataContext());
    }

    @Override
    public Dock getDock()
    {
        return dock.get(getDataContext());
    }

    @Override
    public void setPadding(@NotNull final AxisDistance padding)
    {
        this.padding = DependencyObjectHelper.createFromValue(padding);
    }

    @Override
    public void setDock(@NotNull final Dock dock)
    {
        this.dock = DependencyObjectHelper.createFromValue(dock);
    }

    private boolean updateBoundingBoxes()
    {
        boolean updated = false;

        if (updateLocalBoundingBox())
        {
            updated = true;
        }
        updateAbsoluteBoundingBox();
        updateLocalInternalBoundingBox();
        updateAbsoluteInternalBoundingBox();

        return updated;
    }

    @Override
    public AxisDistance getMargin()
    {
        return margin.get(getDataContext());
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        return dataContext.get(getParent().getDataContext());
    }

    @Override
    public void setMargin(@NotNull final AxisDistance margin)
    {
        this.margin = DependencyObjectHelper.createFromValue(margin);
    }

    @Override
    public void setDataContext(@Nullable final Object dataContext)
    {
        this.dataContext = DependencyObjectHelper.createFromValue(dataContext);
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

    @NotNull
    @Override
    public BoundingBox getAbsoluteBoundingBox()
    {
        return absoluteBoundingBox;
    }

    @Override
    public void setParent(@NotNull final IUIElementHost parent)
    {
        this.parent = parent;
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

    private void updateLocalInternalBoundingBox()
    {
        final Vector2d origin = localBoundingBox
                                  .getLocalOrigin()
                                  .move(getPadding().getLeft().orElse(0d),
                                    getPadding().getTop().orElse(0d));
        final Vector2d size = localBoundingBox
                                .getSize()
                                .move(-1 * (getPadding().getLeft().orElse(0d) + getPadding().getRight().orElse(0d)),
                                  -1 * (getPadding().getTop().orElse(0d) + getPadding().getBottom().orElse(0d))).nullifyNegatives();

        this.localInternalBoundingBox = new BoundingBox(origin, size);
    }

    private void updateAbsoluteInternalBoundingBox()
    {
        final Vector2d origin = absoluteBoundingBox
                                  .getLocalOrigin()
                                  .move(getPadding().getLeft().orElse(0d),
                                    getPadding().getTop().orElse(0d));
        final Vector2d size = absoluteBoundingBox
                                .getSize()
                                .move(-1 * (getPadding().getLeft().orElse(0d) + getPadding().getRight().orElse(0d)),
                                  -1 * (getPadding().getTop().orElse(0d) + getPadding().getBottom().orElse(0d))).nullifyNegatives();

        this.absoluteInternalBoundingBox = new BoundingBox(origin, size);
    }

    @NotNull
    @Override
    public BoundingBox getLocalBoundingBox()
    {
        return localBoundingBox;
    }

    @NotNull
    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }

    @Override
    public BoundingBox getLocalInternalBoundingBox()
    {
        return localInternalBoundingBox;
    }

    @Override
    public BoundingBox getAbsoluteInternalBoundingBox()
    {
        return absoluteInternalBoundingBox;
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

    public static abstract class SimpleControlConstructionDataBuilder<B extends SimpleControlConstructionDataBuilder<B, S>, S extends AbstractChildrenContainingUIElement>
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
        public B withDependentAllignments(@NotNull final IDependencyObject<EnumSet<Alignment>> alignments)
        {
            return withDependency("alignments", alignments);
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

        @NotNull
        public B withDependentDock(@NotNull final IDependencyObject<Dock> dock)
        {
            return withDependency("dock", dock);
        }

        @NotNull
        public B withDependentMargin(@NotNull final IDependencyObject<AxisDistance> margin)
        {
            return withDependency("margin", margin);
        }

        @NotNull
        public B withDependentSize(@NotNull final IDependencyObject<Vector2d> elementSize)
        {
            return withDependency("elementSize", elementSize);
        }

        @NotNull
        public B withDependentDataContext(@NotNull final IDependencyObject<Object> dataContext)
        {
            return withDependency("dataContext", dataContext);
        }

        @NotNull
        public B withDependentVisibility(@NotNull final IDependencyObject<Boolean> visible)
        {
            return withDependency("visible", visible);
        }

        @NotNull
        public B withDependentEnablement(@NotNull final IDependencyObject<Boolean> enabled)
        {
            return withDependency("enabled", enabled);
        }

        @NotNull
        public B withAllignments(@NotNull final EnumSet<Alignment> alignments)
        {
            return withDependency("alignments", DependencyObjectHelper.createFromValue(alignments));
        }

        @NotNull
        public B withDock(@NotNull final Dock dock)
        {
            return withDependency("dock", DependencyObjectHelper.createFromValue(dock));
        }

        @NotNull
        public B withMargin(@NotNull final AxisDistance margin)
        {
            return withDependency("margin", DependencyObjectHelper.createFromValue(margin));
        }

        @NotNull
        public B withSize(@NotNull final Vector2d elementSize)
        {
            return withDependency("elementSize", DependencyObjectHelper.createFromValue(elementSize));
        }

        @NotNull
        public B withDataContext(@NotNull final Object dataContext)
        {
            return withDependency("dataContext", DependencyObjectHelper.createFromValue(dataContext));
        }

        @NotNull
        public B withVisibility(@NotNull final Boolean visible)
        {
            return withDependency("visible", DependencyObjectHelper.createFromValue(visible));
        }

        @NotNull
        public B withEnablement(@NotNull final Boolean enabled)
        {
            return withDependency("enabled", DependencyObjectHelper.createFromValue(enabled));
        }
    }
}
