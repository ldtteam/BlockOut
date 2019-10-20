package com.ldtteam.blockout.utils.controlconstruction.element.core;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.core.builder.IBlockOutUIElementConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.ConverterTypes.CHILDREN_LIST_TYPE;

public abstract class AbstractChildrenContainingUIElement extends LinkedHashMap<String, IUIElement> implements IUIElementHost
{
    @NotNull
    private final UUID uniqueIdentifier = UUID.randomUUID();

    @NotNull
    protected final String                              type;
    @NotNull
    protected final String                              id;
    @NotNull
    public          IDependencyObject<ResourceLocation> style;
    @NotNull
    protected       IUIElementHost                      parent;

    @NotNull
    public IDependencyObject<EnumSet<Alignment>> alignments  = DependencyObjectHelper.createFromValue(EnumSet.of(Alignment.NONE));
    @NotNull
    public IDependencyObject<Dock>               dock        = DependencyObjectHelper.createFromValue(Dock.NONE);
    @NotNull
    public IDependencyObject<AxisDistance>       margin      = DependencyObjectHelper.createFromValue(new AxisDistance());
    @NotNull
    public IDependencyObject<Vector2d>           elementSize = DependencyObjectHelper.createFromValue(new Vector2d());
    @NotNull
    public IDependencyObject<AxisDistance>       padding     = DependencyObjectHelper.createFromValue(new AxisDistance());

    @NotNull
    protected BoundingBox localBoundingBox;
    @NotNull
    protected BoundingBox absoluteBoundingBox;
    @NotNull
    protected BoundingBox localInternalBoundingBox;
    @NotNull
    protected BoundingBox absoluteInternalBoundingBox;

    @NotNull
    public IDependencyObject<Object> dataContext = DependencyObjectHelper.createFromValue(new Object());

    @NotNull
    public IDependencyObject<Boolean> visible = DependencyObjectHelper.createFromValue(true);
    @NotNull
    public IDependencyObject<Boolean> enabled = DependencyObjectHelper.createFromValue(true);

    public AbstractChildrenContainingUIElement(
      @NotNull final String type,
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
      @NotNull final String type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent)
    {
        this.type = type;
        this.style = style;
        this.id = id;
        this.parent = parent;
    }

    @Override
    public UUID getUniqueIdentifier()
    {
        return uniqueIdentifier;
    }

    @NotNull
    @Override
    public String getType()
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
        return style.get(this);
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        getParent().getUiManager().getProfiler().startSection("Update: " + getId());
        if (updateBoundingBoxes())
        {
            updateManager.markDirty();
        }

        if (dataContext.hasChanged(parent.getDataContext()))
        {
            updateManager.markDirty();
        }
        if (alignments.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (style.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (dock.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (margin.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (enabled.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (visible.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (elementSize.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (padding.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        getParent().getUiManager().getProfiler().endSection();
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        if (getParent() == this)
        {
            return new Object();
        }

        if (dataContext.requiresDataContext())
        {
            return dataContext.get(getParent().getDataContext());
        }

        return dataContext.get(this);
    }

    @Override
    public void setDataContext(@Nullable final Object dataContext)
    {
        if (this.dataContext.requiresDataContext())
        {
            this.dataContext.set(getParent().getDataContext(), dataContext);
        }

        this.dataContext.set(this, dataContext);
    }

    @Override
    public EnumSet<Alignment> getAlignment()
    {
        return alignments.get(this);
    }

    @Override
    public void setAlignment(@NotNull final EnumSet<Alignment> alignment)
    {
        this.alignments.set(this, alignment);
    }

    @Override
    public Dock getDock()
    {
        return dock.get(this);
    }

    @Override
    public void setDock(@NotNull final Dock dock)
    {
        this.dock.set(this, dock);
    }

    @Nullable
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

    @Override
    public boolean isVisible()
    {
        return visible.get(this) && (getParent() == this || getParent().isVisible());
    }

    @Override
    public void setVisible(final boolean visible)
    {
        this.visible.set(this, visible);
    }

    @Override
    public boolean isEnabled()
    {
        return enabled.get(this) && isVisible() && (getParent() == this || getParent().isEnabled());
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
        this.enabled.set(this, enabled);
    }

    @NotNull
    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public AxisDistance getMargin()
    {
        return margin.get(this);
    }

    @Override
    public void setMargin(@NotNull final AxisDistance margin)
    {
        this.margin.set(this, margin);
    }

    @Override
    public Vector2d getElementSize()
    {
        return elementSize.get(this);
    }

    @Override
    public void setElementSize(@NotNull final Vector2d elementSize)
    {
        this.elementSize.set(this, elementSize);
    }

    @Override
    public void setParent(@NotNull final IUIElementHost parent)
    {
        this.parent = parent;
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

    private boolean updateLocalBoundingBox()
    {
        getParent().getUiManager().getProfiler().startSection("Size calculation of: " + getId());
        final BoundingBox currentBoundingBox = this.localBoundingBox;

        //If we have no parent we see our default size as parent.
        //Else grab the size from the parent.
        final Vector2d parentSize = getParent() != this ? getParent().getLocalInternalBoundingBox().getSize() : new Vector2d(Double.MAX_VALUE, Double.MAX_VALUE);

        Optional<Double> marginLeft = getMargin().getLeft();
        Optional<Double> marginTop = getMargin().getTop();
        Optional<Double> marginRight = getMargin().getRight();
        Optional<Double> marginBottom = getMargin().getBottom();

        double width = getElementSize().getX();
        double height = getElementSize().getY();

        if (Alignment.LEFT.isActive(this) && Alignment.RIGHT.isActive(this))
        {
            if (!marginLeft.isPresent() && !marginRight.isPresent())
            {
                marginLeft = Optional.of((parentSize.getX() - width) / 2);
            }
            else if (!marginLeft.isPresent())
            {
                marginLeft = Optional.of(0d);
                width = parentSize.getX() - marginRight.orElse(0d);
            }
            else if (!marginRight.isPresent())
            {
                width = parentSize.getX() - marginLeft.orElse(0d);
            }
            else
            {
                width = parentSize.getX() - marginLeft.get() - marginRight.get();
            }
        }
        else if (Alignment.RIGHT.isActive(this))
        {
            marginLeft = Optional.of(parentSize.getX() - width - marginRight.orElse(0d));
        }
        else if (!Alignment.LEFT.isActive(this) && !Alignment.RIGHT.isActive(this) && width == 0d)
        {
            width = getMinimalContentSize().getX();
        }

        if (Alignment.TOP.isActive(this) && Alignment.BOTTOM.isActive(this))
        {
            if (!marginTop.isPresent() && !marginBottom.isPresent())
            {
                marginTop = Optional.of((parentSize.getY() - height) / 2);
            }
            else if (!marginTop.isPresent())
            {
                marginTop = Optional.of(0d);
                height = parentSize.getY() - marginBottom.orElse(0d);
            }
            else if (!marginBottom.isPresent())
            {
                height = parentSize.getY() - marginTop.orElse(0d);
            }
            else
            {
                height = parentSize.getY() - marginTop.get() - marginBottom.get();
            }
        }
        else if (Alignment.BOTTOM.isActive(this))
        {
            marginTop = Optional.of(parentSize.getY() - height - marginBottom.orElse(0d));
        }
        else if (!Alignment.TOP.isActive(this) && !Alignment.BOTTOM.isActive(this) && height == 0d)
        {
            height = getMinimalContentSize().getY();
        }

        final Vector2d origin = new Vector2d(marginLeft.orElse(0d), marginTop.orElse(0d));


        final Vector2d size =
          new Vector2d(width, height).nullifyNegatives();

        this.localBoundingBox = new BoundingBox(origin, size);
        this.localBoundingBox = getDock().apply(this, this.localBoundingBox);

        getParent().getUiManager().getProfiler().endSection();

        return currentBoundingBox == null || !currentBoundingBox.equals(this.localBoundingBox);
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

    @Override
    public AxisDistance getPadding()
    {
        return padding.get(this);
    }

    @Override
    public void setPadding(@NotNull final AxisDistance padding)
    {
        this.padding.set(this, padding);
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

    public static abstract class AbstractChildrenContainingUIElementFactory<U extends IUIElementHost> extends AbstractSimpleUIElement.AbstractSimpleUIElementFactory<U>
    {

        protected AbstractChildrenContainingUIElementFactory(
          final Class<U> clz,
          final String type,
          @NotNull final IChildrenContainingUIElementConstructor<U> constructor,
          @NotNull final ISimpleUIElementWriter<U> writer)
        {
            super(clz, type, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) ->
            {
                final IDependencyObject<AxisDistance> padding = elementData.getFromRawDataWithDefault(CONST_PADDING, engine, AxisDistance.DEFAULT, AxisDistance.class);

                final U element = constructor.constructUsing(
                  elementData,
                  engine,
                  id,
                  parent,
                  styleId,
                  alignments,
                  dock,
                  margin,
                  padding,
                  elementSize,
                  dataContext,
                  visible,
                  enabled
                );

                elementData.getFromRawDataWithDefault(CONST_CHILDREN, engine, new ArrayList<IUIElementData<?>>(), CHILDREN_LIST_TYPE, element)
                  .get(element)
                  .forEach(childData -> {
                      IUIElement child = ProxyHolder.getInstance().getFactoryController().getElementFromData(childData);
                      element.put(child.getId(), child);
                  });

                return element;
            }, (element, builder) -> {
                writer.write(element, builder);

                builder
                  .addComponent(CONST_PADDING, element.getPadding(), AxisDistance.class);

                element.values().forEach(builder::addChild);
            });
        }

        @FunctionalInterface
        protected interface IChildrenContainingUIElementConstructor<U extends IUIElement>
        {
            U constructUsing(
              @NotNull final IUIElementData<?> elementData,
              @NotNull final IBindingEngine engine,
              @NotNull final String id,
              @Nullable final IUIElementHost parent,
              @NotNull final IDependencyObject<ResourceLocation> styleId,
              @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
              @NotNull final IDependencyObject<Dock> dock,
              @NotNull final IDependencyObject<AxisDistance> margin,
              @NotNull final IDependencyObject<AxisDistance> padding,
              @NotNull final IDependencyObject<Vector2d> elementSize,
              @NotNull final IDependencyObject<Object> dataContext,
              @NotNull final IDependencyObject<Boolean> visible,
              @NotNull final IDependencyObject<Boolean> enabled);
        }
    }
}
