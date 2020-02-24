package com.ldtteam.blockout.element.advanced.list;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.advanced.AbstractChildInstantiatingAndLayoutControllableUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.input.IClickAcceptingUIElement;
import com.ldtteam.blockout.element.input.IScrollAcceptingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

import static com.ldtteam.blockout.util.Constants.Controls.List.KEY_LIST;

public class List extends AbstractChildInstantiatingAndLayoutControllableUIElement implements IScrollAcceptingUIElement, IClickAcceptingUIElement, IDrawableUIElement
{
    private static final int CONST_SCROLLBAR_SIZE    = 5;
    private static final int CONST_SCROLLBAR_PADDING = 8;

    private static final long serialVersionUID = -930118618487870715L;

    public IDependencyObject<ResourceLocation> scrollBarBackgroundResource;
    public IDependencyObject<ResourceLocation> scrollBarForegroundResource;
    public IDependencyObject<Boolean>     showScrollbar;

    public List(
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
      @NotNull final boolean dataBoundMode,
      @NotNull final IDependencyObject<ResourceLocation> templateResource,
      @NotNull final IDependencyObject<ResourceLocation> scrollBarBackgroundResource,
      @NotNull final IDependencyObject<ResourceLocation> scrollBarForegroundResource,
      @NotNull final double scrollOffset,
      @NotNull final IDependencyObject<Orientation> orientation,
      @NotNull final IDependencyObject<Boolean> showScrollbar,
      @NotNull final IDependencyObject<Object> source)
    {
        super(KEY_LIST, styleId, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled,
          scrollOffset,
          templateResource,
          source,
          dataBoundMode,
          orientation);
        this.scrollBarBackgroundResource = scrollBarBackgroundResource;
        this.scrollBarForegroundResource = scrollBarForegroundResource;
        this.showScrollbar = showScrollbar;
    }

    @Override
    public AxisDistance getPadding()
    {
        final AxisDistance parentDistance = super.getPadding();

        if (getLocalBoundingBox() == null || values().stream().anyMatch(u -> u.getLocalBoundingBox() == null))
        {
            return parentDistance;
        }


        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            final double localHeight = getLocalBoundingBox().getSize().getY();
            final double barLength = getScrollBarLength();
            final double maxOffset = localHeight - barLength;

            if (maxOffset < 1)
            {
                return parentDistance;
            }

            if (parentDistance.getRight().orElse(0d) < List.CONST_SCROLLBAR_PADDING)
            {
                return new AxisDistance(parentDistance.getLeft(), parentDistance.getTop(), Optional.of((double) List.CONST_SCROLLBAR_PADDING), parentDistance.getBottom());
            }

            return parentDistance;
        }


        final double localWidth = getLocalBoundingBox().getSize().getX();
        final double barLength = getScrollBarLength();
        final double maxOffset = localWidth - barLength;

        if (maxOffset < 1)
        {
            return parentDistance;
        }

        if (parentDistance.getBottom().orElse(0d) < List.CONST_SCROLLBAR_PADDING)
        {
            return new AxisDistance(parentDistance.getLeft(), parentDistance.getTop(), parentDistance.getRight(), Optional.of((double) List.CONST_SCROLLBAR_PADDING));
        }

        return parentDistance;
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        final BoundingBox localBox = getLocalBoundingBox();
        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            final double offset = localBox.getSize().getX() - localX;
            return offset <= CONST_SCROLLBAR_SIZE;
        }
        else
        {
            final double offset = localBox.getSize().getY() - localY;
            return offset <= CONST_SCROLLBAR_SIZE;
        }
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        onScrollBarClick(localX, localY);
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        onScrollBarClick(localX, localY);
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        onScrollBarClick(localX, localY);
    }

    /**
     * Called when a click is made on the scrollbar.
     *
     * @param localY The localY of set of the click.
     */
    private void onScrollBarClick(int localX, int localY)
    {
        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            final double localHeight = getLocalBoundingBox().getSize().getY();
            final double barHeight = getScrollBarLength();
            final double maxOffset = localHeight - barHeight;

            scrollTo(localY / maxOffset);
        }
        else
        {
            final double localWidth = getLocalBoundingBox().getSize().getX();
            final double barWidth = getScrollBarLength();
            final double maxOffset = localWidth - barWidth;

            scrollTo(localX / maxOffset);
        }
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        if (!getShowScrollBar())
        {
            return;
        }

        RenderSystem.pushMatrix();

        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            final double localHeight = getLocalBoundingBox().getSize().getY();
            final double barLength = getScrollBarLength();
            final double maxOffset = localHeight - barLength;

            if (maxOffset < 1)
            {
                RenderSystem.popMatrix();
                return;
            }

            final Vector2d scrollBoxOrigin = new Vector2d(getLocalBoundingBox().getSize().getX() - CONST_SCROLLBAR_SIZE, 0).nullifyNegatives();
            final Vector2d scrollBoxSize = new Vector2d(CONST_SCROLLBAR_SIZE, getLocalBoundingBox().getSize().getY());

            final BoundingBox scrollBox = new BoundingBox(scrollBoxOrigin, scrollBoxSize);

            final Vector2d scrollBarOrigin = new Vector2d(getLocalBoundingBox().getSize().getX() - CONST_SCROLLBAR_SIZE, scrollOffset * maxOffset).nullifyNegatives();
            final Vector2d scrollBarSize = new Vector2d(CONST_SCROLLBAR_SIZE, barLength);

            final BoundingBox scrollBarBox = new BoundingBox(scrollBarOrigin, scrollBarSize);

            drawSrollbarBackground(controller, scrollBox);
            drawSrollbarForeground(controller, scrollBarBox);
        }
        else
        {
            final double localWidth = getLocalBoundingBox().getSize().getX();
            final double barLength = getScrollBarLength();
            final double maxOffset = localWidth - barLength;

            if (maxOffset < 1)
            {
                RenderSystem.popMatrix();
                return;
            }

            final Vector2d scrollBoxOrigin = new Vector2d(0, getLocalBoundingBox().getSize().getY() - CONST_SCROLLBAR_SIZE).nullifyNegatives();
            final Vector2d scrollBoxSize = new Vector2d(getLocalBoundingBox().getSize().getX(), CONST_SCROLLBAR_SIZE);

            final BoundingBox scrollBox = new BoundingBox(scrollBoxOrigin, scrollBoxSize);

            final Vector2d scrollBarOrigin = new Vector2d(scrollOffset * maxOffset, getLocalBoundingBox().getSize().getY() - CONST_SCROLLBAR_SIZE).nullifyNegatives();
            final Vector2d scrollBarSize = new Vector2d(barLength, CONST_SCROLLBAR_SIZE);

            final BoundingBox scrollBarBox = new BoundingBox(scrollBarOrigin, scrollBarSize);

            drawSrollbarBackground(controller, scrollBox);
            drawSrollbarForeground(controller, scrollBarBox);
        }

        RenderSystem.popMatrix();
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    @NotNull
    public Boolean getShowScrollBar()
    {
        return showScrollbar.get(this);
    }

    private void drawSrollbarBackground(@NotNull final IRenderingController controller, @NotNull final BoundingBox scrollBox)
    {
        final ImageResource resource = getScrollBarBackground();

        RenderSystem.pushMatrix();

        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        Color.resetOpenGLColoring();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(scrollBox.getLocalOrigin(),
          scrollBox.getSize(),
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();

        RenderSystem.popMatrix();
    }

    private void drawSrollbarForeground(@NotNull final IRenderingController controller, @NotNull final BoundingBox scrollBarBox)
    {
        final ImageResource resource = getScrollBarForeground();

        RenderSystem.pushMatrix();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        Color.resetOpenGLColoring();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(scrollBarBox.getLocalOrigin(),
          scrollBarBox.getSize(),
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.popMatrix();
    }

    @NotNull
    public ImageResource getScrollBarBackground()
    {
        return getResource(getScrollBarBackgroundResource());
    }

    @NotNull
    public ImageResource getScrollBarForeground()
    {
        return getResource(getScrollBarForegroundResource());
    }

    @NotNull
    public ResourceLocation getScrollBarBackgroundResource()
    {
        return scrollBarBackgroundResource.get(this);
    }

    public void setScrollBarBackgroundResource(@NotNull final ResourceLocation scrollBarBackground)
    {
        this.scrollBarBackgroundResource.set(this, scrollBarBackground);
    }

    @NotNull
    public ResourceLocation getScrollBarForegroundResource()
    {
        return scrollBarForegroundResource.get(this);
    }

    public void setScrollBarForegroundResource(@NotNull final ResourceLocation scrollBarForeground)
    {
        this.scrollBarForegroundResource.set(this, scrollBarForeground);
    }

    public void setShowScrollbar(@NotNull final Boolean showScrollbar)
    {
        this.showScrollbar.set(this, showScrollbar);
    }

    @Override
    public boolean canAcceptScrollInput(final int localX, final int localY, final int deltaWheel)
    {
        if (getLocalBoundingBox() == null || values().stream().anyMatch(u -> u.getLocalBoundingBox() == null))
        {
            return false;
        }


        if (getOrientation() == Orientation.TOP_BOTTOM)
        {
            final double localHeight = getLocalBoundingBox().getSize().getY();
            final double barLength = getScrollBarLength();
            final double maxOffset = localHeight - barLength;

            return !(maxOffset < 1);
        }


        final double localWidth = getLocalBoundingBox().getSize().getX();
        final double barLength = getScrollBarLength();
        final double maxOffset = localWidth - barLength;

        return !(maxOffset < 1);
    }

    @Override
    public void onMouseScroll(final int localX, final int localY, final int deltaWheel)
    {
        scroll(deltaWheel / getTotalContentLength() * -25);
    }
}
