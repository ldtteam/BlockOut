package com.minecolonies.blockout.element.advanced;

import com.google.common.collect.Lists;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IChildDrawableUIElement;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.input.IScrollAcceptingUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.event.injector.EventHandlerInjector;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Clamp;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;

public class List extends AbstractChildrenContainingUIElement implements IScrollAcceptingUIElement, IClickAcceptingUIElement, IDrawableUIElement, IChildDrawableUIElement
{
    private static final int CONST_SCROLLBAR_WIDTH = 5;

    //Indicates weither or not we need to watch for changes in the data list.
    private final boolean dataBoundMode = true;

    //The current scroll state is not bindable. It is exclusively controlled by the control it self.
    private       double  scrollOffset;

    //Used to cache the contents of the databound list.
    private Collection<?>                resolvedDataContext              = Lists.newArrayList();
    private ResourceLocation             resolvedTemplateLocation         = new ResourceLocation("");
    private IBlockOutGuiConstructionData resolvedTemplateConstructionData = null;

    //Bindable resource binding.
    private IDependencyObject<ResourceLocation>             templateResource;
    private IDependencyObject<IBlockOutGuiConstructionData> templateConstructionData;

    public List(
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
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final boolean dataBoundMode,
      @NotNull final IDependencyObject<ResourceLocation> templateResource)
    {
        super(type, style, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
    }

    public List(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style, @NotNull final String id, @Nullable final IUIElementHost parent)
    {
        super(type, style, id, parent);
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final int deltaWheel)
    {
        //We accept all mouse input that is in our box.
        return true;
    }

    /**
     * Returns the total content height based on all children contained in this list.
     *
     * @return The total content height.
     */
    private double getTotalContentHeight()
    {
        return this.values().stream().mapToDouble(u -> u.getLocalBoundingBox().getSize().getY()).sum();
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

    @Override
    public void onMouseScroll(final int localX, final int localY, final int deltaWheel)
    {
        scroll(deltaWheel / getTotalContentHeight());
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

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        final BoundingBox localBox = getLocalBoundingBox();
        if (!localBox.includes(new Vector2d(localX, localY)))
        {
            return false;
        }

        final double offset = localBox.getSize().getX() - localX;
        return offset <= CONST_SCROLLBAR_WIDTH;
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        onScrollBarClick(localY);
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        onScrollBarClick(localY);
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        onScrollBarClick(localY);
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

    private void onScrollBarClick(int localY)
    {
        final double localHeight = getLocalBoundingBox().getSize().getY();
        final double barHeight = getScrollBarHeight();
        final double maxOffset = localHeight - barHeight;

        scrollTo(localY / maxOffset);
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

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
    }

    /**
     * Updates the children when the control is in databound mode via its datacontext.
     *
     * @param updateManager The update manager to mark the ui dirty when the control changed.
     */
    private void updateChildrenInDataBoundMode(@NotNull final IUpdateManager updateManager)
    {
        final Object context = getDataContext();
        if (!(context instanceof Collection))
        {
            updateChildrenInDataBoundMoTo(updateManager, Lists.newArrayList());
        }
        else
        {
            updateChildrenInDataBoundMoTo(updateManager, (Collection<?>) context);
        }
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();

        final double localHeight = getLocalBoundingBox().getSize().getY();
        final double barHeight = getScrollBarHeight();
        final double maxOffset = localHeight - barHeight;

        final Vector2d scrollBoxOrigin = getLocalBoundingBox().getLowerRightCoordinate().move(-CONST_SCROLLBAR_WIDTH, 0).nullifyNegatives();
        final Vector2d scrollBoxSize = new Vector2d(CONST_SCROLLBAR_WIDTH, getLocalBoundingBox().getSize().getY());

        final BoundingBox scrollBox = new BoundingBox(scrollBoxOrigin, scrollBoxSize);

        final Vector2d scrollBarOrigin = getLocalBoundingBox().getLowerRightCoordinate().move(-CONST_SCROLLBAR_WIDTH, scrollOffset * maxOffset).nullifyNegatives();
        final Vector2d scrollBarSize = new Vector2d(CONST_SCROLLBAR_WIDTH, barHeight);

        final BoundingBox scrollBarBox = new BoundingBox(scrollBarOrigin, scrollBarSize);

        controller.drawColoredRect(scrollBox, 0, new Color(Color.DARK_GRAY));
        controller.drawColoredRect(scrollBarBox, 0, new Color(Color.LIGHT_GRAY));

        GlStateManager.popMatrix();
    }

    public IBlockOutGuiConstructionData getTemplateConstructionData()
    {
        return templateConstructionData.get(getDataContext());
    }

    public void setTemplateConstructionData(final @NotNull IBlockOutGuiConstructionData templateConstructionData)
    {
        this.templateConstructionData = DependencyObjectHelper.createFromValue(templateConstructionData);
    }

    /**
     * Returns the template resource directly from the context.
     *
     * @return The template resource.
     */
    public ResourceLocation getTemplateResource()
    {
        return templateResource.get(getDataContext());
    }

    /**
     * Sets the template resource.
     *
     * @param templateResource The new template resource.
     */
    public void setTemplateResource(@NotNull final ResourceLocation templateResource)
    {
        this.templateResource = DependencyObjectHelper.createFromValue(templateResource);
    }

    private void updateChildrenInDataBoundMoTo(@NotNull final IUpdateManager updateManager, @NotNull final Collection<?> newData)
    {
        if (newData.equals(resolvedDataContext)
              && Objects.equals(getTemplateResource(), resolvedTemplateLocation)
              && Objects.equals(getTemplateConstructionData(), resolvedTemplateConstructionData))
        {
            //Noop needed the lists are equal so no modifications are needed.
            return;
        }

        //Context has changed.
        //Mark the ui as dirty.
        updateManager.markDirty();

        //Clear all children.
        clear();

        //Set the template location
        resolvedTemplateLocation = getTemplateResource();
        resolvedDataContext = newData;
        resolvedTemplateConstructionData = getTemplateConstructionData();

        //Create control instances from the template.
        int index = 0;
        for (final Object context :
          newData)
        {
            final IUIElement element = BlockOut.getBlockOut().getProxy().getTemplateEngine().generateFromTemplate(this, context, resolvedTemplateLocation,
              String.format("%s_%d", getId(), index++));

            if (resolvedTemplateConstructionData != null)
            {
                DependencyObjectInjector.inject(element, resolvedTemplateConstructionData);
                EventHandlerInjector.inject(element, resolvedTemplateConstructionData);
            }

            put(element.getId(), element);
        }
    }

    private int getScrollBarHeight()
    {
        final BoundingBox localBox = getLocalBoundingBox();
        final double contentHeight = getTotalContentHeight();

        return (int) Math.min(localBox.getSize().getY(), localBox.getSize().getY() / (contentHeight / localBox.getSize().getY()));
    }
}
