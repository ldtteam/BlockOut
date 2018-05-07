package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.builder.core.builder.IBlockOutUIElementConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.event.Event;
import com.minecolonies.blockout.event.IEventHandler;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.minecolonies.blockout.util.Constants.Controls.Image.KEY_IMAGE;

public class Button extends AbstractSimpleUIElement implements IDrawableUIElement, IClickAcceptingUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> normalBackgroundImage;
    @NotNull
    private IDependencyObject<ResourceLocation> clickedBackgroundImage;
    @NotNull
    private IDependencyObject<ResourceLocation> disabledBackgroundImage;
    @NotNull
    private IDependencyObject<BoundingBox>      normalImageData;
    @NotNull
    private IDependencyObject<BoundingBox>      clickedImageData;
    @NotNull
    private IDependencyObject<BoundingBox>      disabledImageData;
    @NotNull
    private IDependencyObject<Boolean>          clicked;

    @NotNull
    private Event<Button, ButtonClickedEventArgs> onClicked;

    public Button(
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_IMAGE, id, parent);
        this.normalBackgroundImage = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.normalImageData = DependencyObjectHelper.createFromValue(new BoundingBox(new Vector2d(0, 0), new Vector2d(16, 16)));

        this.clickedBackgroundImage = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.clickedImageData = DependencyObjectHelper.createFromValue(new BoundingBox(new Vector2d(0, 0), new Vector2d(16, 16)));

        this.disabledBackgroundImage = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.disabledImageData = DependencyObjectHelper.createFromValue(new BoundingBox(new Vector2d(0, 0), new Vector2d(16, 16)));

        this.clicked = DependencyObjectHelper.createFromValue(false);
    }

    public Button(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> normalBackgroundImage,
      @NotNull final IDependencyObject<BoundingBox> normalImageData,
      @NotNull final IDependencyObject<ResourceLocation> clickedBackgroundImage,
      @NotNull final IDependencyObject<BoundingBox> clickedImageData,
      @NotNull final IDependencyObject<ResourceLocation> disabledBackgroundImage,
      @NotNull final IDependencyObject<BoundingBox> disabledImageData,
      @NotNull final IDependencyObject<Boolean> clicked)
    {
        super(KEY_IMAGE, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);

        this.normalBackgroundImage = normalBackgroundImage;
        this.normalImageData = normalImageData;

        this.clickedBackgroundImage = clickedBackgroundImage;
        this.clickedImageData = clickedImageData;

        this.disabledBackgroundImage = disabledBackgroundImage;
        this.disabledImageData = disabledImageData;

        this.clicked = clicked;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        if (!isVisible())
        {
            return;
        }

        if (!isEnabled())
        {
            drawBackground(controller,
              this::getDisabledBackgroundImageScalingFactor,
              this::getDisabledBackgroundImage,
              this::getDisabledBackgroundImageData,
              this::getDisabledBackgroundImageSize);
        }
        else if (isClicked())
        {
            drawBackground(controller,
              this::getClickedBackgroundImageScalingFactor,
              this::getClickedBackgroundImage,
              this::getClickedBackgroundImageData,
              this::getClickedBackgroundImageScalingFactor);
        }
        else
        {
            drawBackground(controller,
              this::getNormalBackgroundImageScalingFactor,
              this::getNormalBackgroundImage,
              this::getNormalBackgroundImageData,
              this::getNormalBackgroundImageScalingFactor);
        }
    }

    private void drawBackground(
      @NotNull final IRenderingController controller,
      Supplier<Vector2d> scalingFactorSupplier,
      Supplier<ResourceLocation> imageSupplier,
      Supplier<BoundingBox> imageDataSupplier,
      Supplier<Vector2d> imageSizeSupplier)
    {
        final Vector2d scalingFactor = scalingFactorSupplier.get();

        GlStateManager.pushMatrix();
        GlStateManager.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);

        final BoundingBox imageData = imageDataSupplier.get();

        controller.bindTexture(imageSupplier.get());
        controller.drawTexturedModalRect(getLocalBoundingBox().getLocalOrigin(),
          getLocalBoundingBox().getSize(),
          imageData.getLocalOrigin(),
          imageData.getSize(),
          imageSizeSupplier.get());

        GlStateManager.popMatrix();
    }

    public boolean isClicked()
    {
        return clicked.get(getDataContext());
    }

    public void setClicked(@NotNull final boolean clicked)
    {
        this.clicked = DependencyObjectHelper.createFromValue(clicked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
        //Noop
    }

    @NotNull
    public Vector2d getNormalBackgroundImageSize()
    {
        return BlockOut.getBlockOut().getProxy().getImageSize(getNormalBackgroundImage());
    }

    @NotNull
    public ResourceLocation getNormalBackgroundImage()
    {
        return normalBackgroundImage.get(getDataContext());
    }

    public void setNormalBackgroundImage(@NotNull final ResourceLocation normalBackgroundImage)
    {
        this.normalBackgroundImage = DependencyObjectHelper.createFromValue(normalBackgroundImage);
    }

    @NotNull
    public Vector2d getNormalBackgroundImageScalingFactor()
    {
        final Vector2d imageSize = getNormalBackgroundImageData().getSize();
        final Vector2d elementSize = getElementSize();

        return new Vector2d(imageSize.getX() / elementSize.getX(), imageSize.getY() / elementSize.getY());
    }

    @NotNull
    public BoundingBox getNormalBackgroundImageData()
    {
        return normalImageData.get(getDataContext());
    }

    public void setNormalBackgroundImageData(@NotNull final BoundingBox box)
    {
        this.normalImageData = DependencyObjectHelper.createFromValue(box);
    }

    @NotNull
    public Vector2d getClickedBackgroundImageSize()
    {
        return BlockOut.getBlockOut().getProxy().getImageSize(getClickedBackgroundImage());
    }

    @NotNull
    public ResourceLocation getClickedBackgroundImage()
    {
        return clickedBackgroundImage.get(getDataContext());
    }

    public void setClickedBackgroundImage(@NotNull final ResourceLocation clickedBackgroundImage)
    {
        this.clickedBackgroundImage = DependencyObjectHelper.createFromValue(clickedBackgroundImage);
    }

    @NotNull
    public Vector2d getClickedBackgroundImageScalingFactor()
    {
        final Vector2d imageSize = getClickedBackgroundImageData().getSize();
        final Vector2d elementSize = getElementSize();

        return new Vector2d(imageSize.getX() / elementSize.getX(), imageSize.getY() / elementSize.getY());
    }

    @NotNull
    public BoundingBox getClickedBackgroundImageData()
    {
        return clickedImageData.get(getDataContext());
    }

    public void setClickedBackgroundImageData(@NotNull final BoundingBox box)
    {
        this.clickedImageData = DependencyObjectHelper.createFromValue(box);
    }

    @NotNull
    public Vector2d getDisabledBackgroundImageSize()
    {
        return BlockOut.getBlockOut().getProxy().getImageSize(getDisabledBackgroundImage());
    }

    @NotNull
    public ResourceLocation getDisabledBackgroundImage()
    {
        return disabledBackgroundImage.get(getDataContext());
    }

    public void setDisabledBackgroundImage(@NotNull final ResourceLocation disabledBackgroundImage)
    {
        this.disabledBackgroundImage = DependencyObjectHelper.createFromValue(disabledBackgroundImage);
    }

    @NotNull
    public Vector2d getDisabledBackgroundImageScalingFactor()
    {
        final Vector2d imageSize = getDisabledBackgroundImageData().getSize();
        final Vector2d elementSize = getElementSize();

        return new Vector2d(imageSize.getX() / elementSize.getX(), imageSize.getY() / elementSize.getY());
    }

    @NotNull
    public BoundingBox getDisabledBackgroundImageData()
    {
        return disabledImageData.get(getDataContext());
    }

    public void setDisabledBackgroundImageData(@NotNull final BoundingBox box)
    {
        this.disabledImageData = DependencyObjectHelper.createFromValue(box);
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        return isEnabled() && isVisible();
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        setClicked(true);
        onClicked.raise(this, new ButtonClickedEventArgs(true, localX, localY, button));
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        setClicked(false);
        onClicked.raise(this, new ButtonClickedEventArgs(false, localX, localY, button));
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        setClicked(true);
        onClicked.raise(this, new ButtonClickedEventArgs(true, localX, localY, button, timeElapsed));
    }

    public static class ButtonClickedEventArgs
    {
        private final boolean start;
        private final int localX;
        private final int localY;
        private final MouseButton button;
        private final float timeDelta;

        public ButtonClickedEventArgs(final boolean start, final int localX, final int localY, final MouseButton button)
        {
            this.start = start;
            this.localX = localX;
            this.localY = localY;
            this.button = button;
            this.timeDelta = 0f;
        }

        public ButtonClickedEventArgs(final boolean start, final int localX, final int localY, final MouseButton button, final float timeDelta)
        {
            this.start = start;
            this.localX = localX;
            this.localY = localY;
            this.button = button;
            this.timeDelta = timeDelta;
        }

        public boolean isStart()
        {
            return start;
        }

        public int getLocalX()
        {
            return localX;
        }

        public int getLocalY()
        {
            return localY;
        }

        public MouseButton getButton()
        {
            return button;
        }

        public float getTimeDelta()
        {
            return timeDelta;
        }
    }

    public static class ButtonConstructionDataBuilder implements IBlockOutUIElementConstructionDataBuilder<ButtonConstructionDataBuilder, Button>
    {

        @NotNull
        @Override
        public ButtonConstructionDataBuilder withDependency(@NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency)
        {
            return null;
        }

        @NotNull
        @Override
        public IBlockOutGuiConstructionDataBuilder done()
        {
            return null;
        }

        @NotNull
        @Override
        public <S, A> ButtonConstructionDataBuilder withEventHandler(
          @NotNull final String eventName, @NotNull final Class<A> argumentTypeClass, @NotNull final IEventHandler<S, A> eventHandler)
        {
            return null;
        }
    }
}
