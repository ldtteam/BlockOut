package com.minecolonies.blockout.element.advanced;

import com.google.common.collect.Lists;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
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
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.math.Clamp;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;

public class List extends AbstractChildrenContainingUIElement implements IScrollAcceptingUIElement, IClickAcceptingUIElement, IDrawableUIElement, IChildDrawableUIElement
{

    //Indicates weither or not we need to watch for changes in the data list.
    private final boolean dataBoundMode;
    //The current scroll state is not bindable. It is exclusively controlled by the control it self.
    private       double  scrollOffset;
    //Used to cache the contents of the databound list.
    private Collection<?>    resolvedDataContext      = Lists.newArrayList();
    private ResourceLocation resolvedTemplateLocation = new ResourceLocation("");

    private IDependencyObject<ResourceLocation> templateResource;

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
      @NotNull final IDependencyObject<Boolean> enabled)
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

    @Override
    public void onMouseScroll(final int localX, final int localY, final int deltaWheel)
    {
        scroll(deltaWheel);
    }

    /**
     * Updates the scroll position with a given delta.
     *
     * @param delta The delta to scroll.
     */
    public void scroll(int delta)
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
        final double totalContentHeight = getTotalContentHeight();
        final double ownHeight = getLocalInternalBoundingBox().getSize().getY();
        final double maximalScrollOffset = Math.max(totalContentHeight - ownHeight, 0d);

        this.scrollOffset = Clamp.Clamp(0, target, maximalScrollOffset);
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
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        return false;
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {

    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {

    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {

    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (!dataBoundMode)
        {
            return;
        }
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

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {

    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    /**
     * Updates the scroll position by trying to move the child with the given id to the top.
     *
     * @param childId The child to scroll to.
     * @throws IllegalArgumentException thrown when no child with a given id is found.
     */
    public void scrollTo(@NotNull final ResourceLocation childId) throws IllegalArgumentException
    {
        if (!containsKey(childId))
        {
            throw new IllegalArgumentException(String.format("No child with Id: %s can be found to scroll to.", childId));
        }

        scrollTo(get(childId));
    }

    /**
     * Updates the scroll position by trying to move the given child to the top.
     * Passing a control that is not a child of this list will result in undefined behaviour.
     *
     * @param child The child to scroll to.
     */
    public void scrollTo(@NotNull final IUIElement child)
    {
        final double childOffset = child.getLocalBoundingBox().getLocalOrigin().getY();

        scrollTo(childOffset);
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

    private void updateChildrenInDataBoundMoTo(@NotNull final IUpdateManager updateManager, @NotNull final Collection<?> newData)
    {
        if (newData.equals(resolvedDataContext) && getTemplateResource() == resolvedTemplateLocation)
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

        //Create control instances from the template.
        int index = 0;
        for (final Object context :
          newData)
        {
            final IUIElement element = BlockOut.getBlockOut().getProxy().getTemplateEngine().generateFromTemplate(this, context, resolvedTemplateLocation,
              String.format("%s_%d", getId(), index++));

            put(element.getId(), element);
        }
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
}
