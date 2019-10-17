package com.ldtteam.blockout.element.advanced;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.ldtteam.blockout.binding.dependency.injection.IDependencyDataProvider;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.data.BlockOutGuiConstructionData;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.drawable.IChildDrawableUIElement;
import com.ldtteam.blockout.element.simple.Region;
import com.ldtteam.blockout.element.values.*;
import com.ldtteam.blockout.event.injector.EventHandlerInjector;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Clamp;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;

import static com.ldtteam.blockout.util.Constants.Controls.List.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

/**
 * Special element type that is capable of instantiating several child controls from a template depending on the data context.
 * It is also capable of allowing special logic that controls how its child elements are layed-out by overriding {@link #updateScrollOffset()}
 */
public abstract class AbstractChildInstantiatingAndLayoutControllableUIElement extends AbstractChildrenContainingUIElement implements IChildDrawableUIElement
{
    //Bindable resource binding.
    public IDependencyObject<ResourceLocation>                  templateResource;
    public IDependencyObject<IBlockOutGuiConstructionData> templateConstructionData;
    public IDependencyObject<Object>                       source;
    public IDependencyObject<Orientation>                  orientation;

    //The current scroll state is not bindable. It is exclusively controlled by the control it self.
    protected double  scrollOffset;
    protected boolean dataBoundMode;

    public AbstractChildInstantiatingAndLayoutControllableUIElement(
      final String type,
      final IDependencyObject<ResourceLocation> style,
      final String id,
      final IUIElementHost parent,
      final IDependencyObject<EnumSet<Alignment>> alignments,
      final IDependencyObject<Dock> dock,
      final IDependencyObject<AxisDistance> margin,
      final IDependencyObject<Vector2d> elementSize,
      final IDependencyObject<AxisDistance> padding,
      final IDependencyObject<Object> dataContext,
      final IDependencyObject<Boolean> visible,
      final IDependencyObject<Boolean> enabled,
      @NotNull final double scrollOffset,
      @NotNull final IDependencyObject<ResourceLocation> templateResource,
      @NotNull final IDependencyObject<Object> source,
      @NotNull final boolean dataBoundMode,
      @NotNull final IDependencyObject<Orientation> orientation)
    {
        super(type, style, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
        this.scrollOffset = scrollOffset;
        this.templateResource = templateResource;
        this.templateConstructionData = DependencyObjectHelper.createFromValue(new BlockOutGuiConstructionData());
        this.source = source;
        this.dataBoundMode = dataBoundMode;
        this.orientation = orientation;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (!dataBoundMode)
        {
            return;
        }

        updateChildrenInDataBoundMode(updateManager);
    }

    /**
     * Called by the rendering manager before the drawing of the background of our children starts.
     *
     * @param manager The manager.
     */
    @Override
    public void preBackgroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        manager.getRenderingController().getScissoringController().push(getAbsoluteInternalBoundingBox());
    }

    /**
     * Called by the rendering manager after the drawing of the background of our children ended.
     *
     * @param manager The manager.
     */
    @Override
    public void postBackgroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        manager.getRenderingController().getScissoringController().pop();
    }

    /**
     * Called by the rendering manager before the drawing of the foreground of our children starts.
     *
     * @param manager The manager.
     */
    @Override
    public void preForegroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        manager.getRenderingController().getScissoringController().push(getAbsoluteInternalBoundingBox());
    }

    /**
     * Called by the rendering manager after the drawing of the foreground of our children ended.
     *
     * @param manager The manager.
     */
    @Override
    public void postForegroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        manager.getRenderingController().getScissoringController().pop();
    }

    public IBlockOutGuiConstructionData getTemplateConstructionData()
    {
        return templateConstructionData.get(this);
    }

    public void setTemplateConstructionData(final @NotNull IBlockOutGuiConstructionData templateConstructionData)
    {
        this.templateConstructionData.set(this, templateConstructionData);
    }

    /**
     * Updates the children when the control is in databound mode via its datacontext.
     *
     * @param updateManager The update manager to mark the ui dirty when the control changed.
     */
    private void updateChildrenInDataBoundMode(@NotNull final IUpdateManager updateManager)
    {
        if (!source.hasChanged(getDataContext())
              && !templateResource.hasChanged(getDataContext())
              && !templateConstructionData.hasChanged(getDataContext()))
        {
            //Noop needed the lists are equal so no modifications are needed.
            return;
        }

        final Object newDataContext = source.get(this);
        final Collection<?> newData;
        if (newDataContext instanceof Collection)
        {
            newData = (Collection<?>) newDataContext;
        }
        else
        {
            newData = Lists.newArrayList();
        }

        //Context has changed.
        //Mark the ui as dirty.
        updateManager.markDirty();

        //Clear all children.
        clear();

        //Create control instances from the template.
        int index = 0;
        for (final Object context :
          newData)
        {
            final IUIElement element = ProxyHolder.getInstance().getTemplateEngine().generateFromTemplate(
              this,
              DependencyObjectHelper.createFromValue(context)
              ,
              getTemplateResource(),
              String.format("%s_%d", getId(), index++));

            if (getTemplateConstructionData() != null)
            {
                @NotNull final IBlockOutGuiConstructionData data = getTemplateConstructionData();
                DependencyObjectInjector.inject(element, data);
                EventHandlerInjector.inject(element, data);

                if (element instanceof IUIElementHost)
                {
                    IUIElementHost iuiElementHost = (IUIElementHost) element;
                    iuiElementHost.getAllCombinedChildElements().values().forEach(c -> {
                        DependencyObjectInjector.inject(c, data);
                        EventHandlerInjector.inject(c, data);
                    });
                }
            }

            wrapNewElementAndRegister(element);
        }

        updateScrollOffset();
    }

    /**
     * Returns the template resource directly from the context.
     *
     * @return The template resource.
     */
    public IIdentifier getTemplateResource()
    {
        return templateResource.get(this);
    }

    /**
     * Sets the template resource.
     *
     * @param templateResource The new template resource.
     */
    public void setTemplateResource(@NotNull final IIdentifier templateResource)
    {
        this.templateResource.set(this, templateResource);
        this.dataBoundMode = true;
        updateChildrenInDataBoundMode(getUiManager().getUpdateManager());
    }

    public boolean isDataBoundMode()
    {
        return dataBoundMode;
    }

    public void setDataBoundMode(final boolean dataBoundMode)
    {
        this.dataBoundMode = dataBoundMode;
    }

    /**
     * Updates the scroll position with a given delta.
     *
     * @param delta The delta to scroll.
     */
    public void scroll(double delta)
    {
        final double newScrollOffset = scrollOffset + delta;
        scrollTo(newScrollOffset);
    }

    /**
     * Updates the scroll position by scrolling to a given target.
     * Given value is clamped to the minimal and maximal scroll value.
     *
     * @param target The target to scroll to.
     */
    public void scrollTo(double target)
    {
        this.scrollOffset = Clamp.Clamp(0, target, 1);
        updateScrollOffset();
        getParent().getUiManager().getUpdateManager().markDirty();
    }

    protected boolean updateScrollOffset()
    {
        final Orientation currentOrientation = getOrientation();
        boolean requiresUpdate = false;
        double maxOffset = currentOrientation == Orientation.TOP_BOTTOM
                             ? (getTotalContentLength() - getLocalInternalBoundingBox().getSize().getY()) * scrollOffset
                             : (getTotalContentLength() - getLocalInternalBoundingBox().getSize().getX()) * scrollOffset;

        if (maxOffset <= 0)
        {
            maxOffset = 0;
        }

        double currentUsedOffset = 0d;
        for (IUIElement wrapper : values())
        {
            final AxisDistance newMargin;
            if (currentOrientation == Orientation.TOP_BOTTOM)
            {
                newMargin = new AxisDistanceBuilder().setLeft(Optional.of(0d)).setRight(Optional.of(0d)).setTop(Optional.of(-maxOffset + currentUsedOffset)).build();
            }
            else
            {
                newMargin =
                  new AxisDistanceBuilder().setLeft(Optional.of(-maxOffset + currentUsedOffset)).setTop(Optional.of(0d)).setBottom(Optional.of(0d)).build();
            }

            if (!newMargin.equals(wrapper.getMargin()))
            {
                requiresUpdate = true;
            }

            wrapper.setMargin(newMargin);
            wrapper.update(getParent().getUiManager().getUpdateManager());
            currentUsedOffset += (currentOrientation == Orientation.TOP_BOTTOM ? wrapper.getElementSize().getY() : wrapper.getElementSize().getX());
        }

        if (requiresUpdate && isVisible())
        {
            values().stream().forEach(e -> e.setVisible(
              this.getAbsoluteInternalBoundingBox().intersects(e.getAbsoluteBoundingBox())
              )
            );
        }

        return requiresUpdate;
    }

    @NotNull
    public Orientation getOrientation()
    {
        final Orientation orientation = this.orientation.get(this);
        if (orientation != Orientation.TOP_BOTTOM && orientation != Orientation.LEFT_RIGHT)
        {
            Log.getLogger().error("Failed to get valid Orientation for list: " + getId() + " current: " + orientation + " Valid are: TOP_BOTTOM, LEFT_RIGHT.");
            setOrientation(Orientation.TOP_BOTTOM);

            return Orientation.TOP_BOTTOM;
        }

        return orientation;
    }

    public void setOrientation(@NotNull final Orientation orientation)
    {
        this.orientation.set(this, orientation);
    }

    /**
     * Returns the total content height based on all children contained in this list.
     *
     * @return The total content height.
     */
    protected double getTotalContentLength()
    {
        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            return this.values().stream().mapToDouble(u -> u.getLocalBoundingBox().getSize().getY()).sum();
        }

        return this.values().stream().mapToDouble(u -> u.getLocalBoundingBox().getSize().getX()).sum();
    }

    /**
     * Returns the height of the scrolling element of the scrollbar.
     *
     * @return The height.
     */
    protected int getScrollBarLength()
    {
        final BoundingBox localBox = getLocalBoundingBox();
        final double contentLength = getTotalContentLength();

        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            return (int) Math.min(localBox.getSize().getY(), (localBox.getSize().getY() / contentLength) * localBox.getSize().getY());
        }

        return (int) Math.min(localBox.getSize().getX(), (localBox.getSize().getX() / contentLength) * localBox.getSize().getX());
    }

    protected boolean stretchElementDuringWrapping()
    {
        return true;
    }

    protected void wrapNewElementAndRegister(@NotNull final IUIElement element)
    {
        final Region wrappingRegion = new Region(
          DependencyObjectHelper.createFromValue(getStyleId()),
          String.format("%s_wrapper", element.getId()),
          this
        );

        if (stretchElementDuringWrapping())
        {
            DependencyObjectInjector.inject(wrappingRegion, new IDependencyDataProvider()
            {
                @Override
                public boolean hasDependencyData(@NotNull final String name)
                {
                    return name.contains("elementSize");
                }

                @NotNull
                @Override
                public <T> IDependencyObject<T> get(@NotNull final String name)
                {
                    return (IDependencyObject<T>) DependencyObjectHelper.createFromProperty(
                      PropertyCreationHelper.createFromOptional(context -> new Vector2d(
                        getOrientation() == Orientation.TOP_BOTTOM ? getLocalInternalBoundingBox().getSize().getX() : element.getMinimalInternalSizeOfParent().getX(),
                        getOrientation() == Orientation.LEFT_RIGHT ? getLocalInternalBoundingBox().getSize().getY() : element.getMinimalInternalSizeOfParent().getY()
                      ), (context, value) -> {
                          //Noop
                      }, false), new Vector2d());
                }
            });
        }

        element.setParent(wrappingRegion);

        wrappingRegion.put(element.getId(), element);
        wrappingRegion.setParent(this);

        wrappingRegion.update(getUiManager().getUpdateManager());
        element.update(getParent().getUiManager().getUpdateManager());
        put(wrappingRegion.getId(), wrappingRegion);
    }

    public static abstract class AbstractChildInstantiatingAndLayoutControllableUIElementFactory<U extends AbstractChildInstantiatingAndLayoutControllableUIElement>
      extends AbstractChildrenContainingUIElementFactory<U>
    {

        @NotNull
        private final IChildInstantiatingAndLayoutControllableUIElementConstructor<U> constructor;
        @NotNull
        private final ISimpleUIElementWriter<U>                                       writer;

        public AbstractChildInstantiatingAndLayoutControllableUIElementFactory(
          @NotNull final Class<U> clz,
          @NotNull final String type,
          @NotNull final IChildInstantiatingAndLayoutControllableUIElementConstructor<U> constructor,
          @NotNull final ISimpleUIElementWriter<U> writer)
        {
            super(clz, type, (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<ResourceLocation> templateResource =
                  elementData.getFromRawDataWithDefault(CONST_TEMPLATE, engine, IIdentifier.create(MISSING), IIdentifier.class);
                final IDependencyObject<Object> source = elementData.getFromRawDataWithDefault(CONST_SOURCE, engine, Lists.newArrayList(), IIdentifier.class);
                final Double scrollOffset = elementData.getRawWithoutBinding(CONST_SCROLLOFFSET, 0d, Double.class);
                final IDependencyObject<Orientation> orientation = elementData.getFromRawDataWithDefault(CONST_ORIENTATION, engine, Orientation.TOP_BOTTOM, Orientation.class);

                final U instance = constructor.constructUsing(
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
                  enabled,
                  templateResource,
                  source,
                  orientation,
                  !elementData.getMetaData().hasChildren(),
                  scrollOffset
                );

                return instance;
            }, (element, builder) ->  {
                builder
                  .addComponent(CONST_SCROLLOFFSET, element.scrollOffset, Double.class)
                  .addComponent(CONST_ORIENTATION, element.getOrientation(), Orientation.class);

                writer.write(element, builder);
            });

            this.constructor = constructor;
            this.writer = writer;
        }

        @FunctionalInterface
        public interface IChildInstantiatingAndLayoutControllableUIElementConstructor<U extends AbstractChildInstantiatingAndLayoutControllableUIElement>
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
              @NotNull final IDependencyObject<Boolean> enabled,
              @NotNull final IDependencyObject<ResourceLocation> templateResource,
              @NotNull final IDependencyObject<Object> source,
              @NotNull final IDependencyObject<Orientation> orientation,
              @NotNull final boolean dataBoundMode,
              @NotNull final Double scrollOffset
            );
        }
    }
}
