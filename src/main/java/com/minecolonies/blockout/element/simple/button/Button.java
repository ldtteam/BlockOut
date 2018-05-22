package com.minecolonies.blockout.element.simple.button;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.event.Event;
import com.minecolonies.blockout.event.IEventHandler;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.minecolonies.blockout.util.Constants.Controls.Button.*;
import static com.minecolonies.blockout.util.Constants.Controls.General.*;

public class Button extends AbstractChildrenContainingUIElement implements IDrawableUIElement, IClickAcceptingUIElement
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
    private Event<Button, ButtonClickedEventArgs> onClicked = new Event<>(Button.class, Button.ButtonClickedEventArgs.class);

    public Button(
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_BUTTON, id, parent);
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
      @NotNull final IDependencyObject<AxisDistance> padding,
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
        super(KEY_BUTTON, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

        this.normalBackgroundImage = normalBackgroundImage;
        this.normalImageData = normalImageData;

        this.clickedBackgroundImage = clickedBackgroundImage;
        this.clickedImageData = clickedImageData;

        this.disabledBackgroundImage = disabledBackgroundImage;
        this.disabledImageData = disabledImageData;

        this.clicked = clicked;
    }

    public static class ButtonConstructionDataBuilder extends AbstractChildrenContainingUIElement.SimpleControlConstructionDataBuilder<ButtonConstructionDataBuilder, Button>
    {

        protected ButtonConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, Button.class);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDependentNormalBackgroundImage(@NotNull final IDependencyObject<ResourceLocation> normalBackgroundImage)
        {
            return withDependency("normalBackgroundImage", normalBackgroundImage);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDependentNormalBackgroundImageData(@NotNull final IDependencyObject<BoundingBox> normalImageData)
        {
            return withDependency("normalImageData", normalImageData);
        }

        @NotNull
        public ButtonConstructionDataBuilder withNormalBackgroundImage(@NotNull final ResourceLocation normalBackgroundImage)
        {
            return withDependency("normalBackgroundImage", DependencyObjectHelper.createFromValue(normalBackgroundImage));
        }

        @NotNull
        public ButtonConstructionDataBuilder withNormalBackgroundImageData(@NotNull final BoundingBox normalImageData)
        {
            return withDependency("normalImageData", DependencyObjectHelper.createFromValue(normalImageData));
        }

        @NotNull
        public ButtonConstructionDataBuilder withDependentClickedBackgroundImage(@NotNull final IDependencyObject<ResourceLocation> clickedBackgroundImage)
        {
            return withDependency("clickedBackgroundImage", clickedBackgroundImage);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDependentClickedBackgroundImageData(@NotNull final IDependencyObject<BoundingBox> clickedImageData)
        {
            return withDependency("clickedImageData", clickedImageData);
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedBackgroundImage(@NotNull final ResourceLocation clickedBackgroundImage)
        {
            return withDependency("clickedBackgroundImage", DependencyObjectHelper.createFromValue(clickedBackgroundImage));
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedBackgroundImageData(@NotNull final BoundingBox clickedImageData)
        {
            return withDependency("clickedImageData", DependencyObjectHelper.createFromValue(clickedImageData));
        }

        @NotNull
        public ButtonConstructionDataBuilder withDependentDisabledBackgroundImage(@NotNull final IDependencyObject<ResourceLocation> disabledBackgroundImage)
        {
            return withDependency("disabledBackgroundImage", disabledBackgroundImage);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDependentDisabledBackgroundImageData(@NotNull final IDependencyObject<BoundingBox> disabledImageData)
        {
            return withDependency("disabledImageData", disabledImageData);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDisabledBackgroundImage(@NotNull final ResourceLocation disabledBackgroundImage)
        {
            return withDependency("disabledBackgroundImage", DependencyObjectHelper.createFromValue(disabledBackgroundImage));
        }

        @NotNull
        public ButtonConstructionDataBuilder withDisabledBackgroundImageData(@NotNull final BoundingBox disabledImageData)
        {
            return withDependency("disabledImageData", DependencyObjectHelper.createFromValue(disabledImageData));
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedEventHandler(@NotNull final IEventHandler<Button, ButtonClickedEventArgs> eventHandler)
        {
            return withEventHandler("onClicked", ButtonClickedEventArgs.class, eventHandler);
        }
    }

    public static class Factory implements IUIElementFactory<Button>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_BUTTON;
        }

        @NotNull
        @Override
        public Button readFromElementData(@NotNull final IUIElementData elementData)
        {
            final String id = elementData.getStringAttribute(CONST_ID);
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<AxisDistance> padding = elementData.getBoundAxisDistanceAttribute(CONST_PADDING);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDatacontext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> defaultBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_DEFAULT_BACKGROUND_IMAGE);
            final IDependencyObject<BoundingBox> defaultBackgroundImageData = elementData.getBoundBoundingBoxAttribute(CONST_DEFAULT_BACKGROUND_IMAGE_DATA);
            final IDependencyObject<ResourceLocation> clickedBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_CLICKED_BACKGROUND_IMAGE);
            final IDependencyObject<BoundingBox> clickedBackgroundImageData = elementData.getBoundBoundingBoxAttribute(CONST_CLICKED_BACKGROUND_IMAGE_DATA);
            final IDependencyObject<ResourceLocation> disabledBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_DISABLED_BACKGROUND_IMAGE);
            final IDependencyObject<BoundingBox> disabledBackgroundImageData = elementData.getBoundBoundingBoxAttribute(CONST_DISABLED_BACKGROUND_IMAGE_DATA);
            final IDependencyObject<Boolean> clicked = elementData.getBoundBooleanAttribute(CONST_INITIALLY_CLICKED);


            final Button button = new Button(
              id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              padding,
              dataContext,
              visible,
              enabled,
              defaultBackgroundImage,
              defaultBackgroundImageData,
              clickedBackgroundImage,
              clickedBackgroundImageData,
              disabledBackgroundImage,
              disabledBackgroundImageData,
              clicked);

            elementData.getChildren(button).forEach(childData -> {
                IUIElement child = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(childData);
                button.put(child.getId(), child);
            });

            return button;
        }

        @Override
        public void writeToElementData(@NotNull final Button element, @NotNull final IUIElementDataBuilder builder)
        {

            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addAxisDistance(CONST_PADDING, element.getPadding())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_DEFAULT_BACKGROUND_IMAGE, element.getNormalBackgroundImage())
              .addBoundingBox(CONST_DEFAULT_BACKGROUND_IMAGE_DATA, element.getNormalBackgroundImageData())
              .addResourceLocation(CONST_DISABLED_BACKGROUND_IMAGE, element.getDisabledBackgroundImage())
              .addBoundingBox(CONST_DISABLED_BACKGROUND_IMAGE_DATA, element.getDisabledBackgroundImageData())
              .addResourceLocation(CONST_CLICKED_BACKGROUND_IMAGE, element.getClickedBackgroundImage())
              .addBoundingBox(CONST_CLICKED_BACKGROUND_IMAGE_DATA, element.getClickedBackgroundImageData())
              .addBoolean(CONST_INITIALLY_CLICKED, element.isClicked());

            element.values().forEach(child -> {
                builder.addChild(BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(child));
            });
        }
    }

    @Override
    public IUIElement put(final String key, final IUIElement value)
    {
        if (value instanceof IClickAcceptingUIElement)
        {
            throw new IllegalArgumentException("Buttons cannot contain other clickable elements");
        }

        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends IUIElement> m)
    {
        if (m.values().stream().anyMatch(e -> e instanceof IClickAcceptingUIElement))
        {
            throw new IllegalArgumentException("Buttons cannot contain other clickable elements");
        }

        super.putAll(m);
    }

    @Override
    public IUIElement putIfAbsent(final String key, final IUIElement value)
    {
        if (!containsKey(key) && value instanceof IClickAcceptingUIElement)
        {
            throw new IllegalArgumentException("Buttons cannot contain other clickable elements");
        }

        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean replace(final String key, final IUIElement oldValue, final IUIElement newValue)
    {
        if (containsKey(key) && Objects.equals(get(key), oldValue) && newValue instanceof IClickAcceptingUIElement)
        {
            throw new IllegalArgumentException("Buttons cannot contain other clickable elements");
        }

        return super.replace(key, oldValue, newValue);
    }

    @Override
    public IUIElement replace(final String key, final IUIElement value)
    {
        if (containsKey(key) && value instanceof IClickAcceptingUIElement)
        {
            throw new IllegalArgumentException("Buttons cannot contain other clickable elements");
        }

        return super.replace(key, value);
    }

    @Override
    public IUIElement computeIfAbsent(final String key, final Function<? super String, ? extends IUIElement> mappingFunction)
    {
        if (get(key) == null)
        {
            final IUIElement target = mappingFunction.apply(key);
            put(key, target);
        }

        return get(key);
    }

    @Override
    public IUIElement computeIfPresent(
      final String key, final BiFunction<? super String, ? super IUIElement, ? extends IUIElement> remappingFunction)
    {
        if (get(key) != null)
        {
            final IUIElement current = get(key);
            final IUIElement target = remappingFunction.apply(key, current);
            put(key, target);
        }

        return get(key);
    }

    @Override
    public IUIElement compute(
      final String key, final BiFunction<? super String, ? super IUIElement, ? extends IUIElement> remappingFunction)
    {
        final IUIElement current = get(key);
        final IUIElement target = remappingFunction.apply(key, current);

        put(key, target);

        return get(key);
    }

    @Override
    public IUIElement merge(
      final String key, final IUIElement value, final BiFunction<? super IUIElement, ? super IUIElement, ? extends IUIElement> remappingFunction)
    {
        final IUIElement oldValue = get(key);
        final IUIElement newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);

        put(key, newValue);

        return get(key);
    }

    @Override
    public void replaceAll(final BiFunction<? super String, ? super IUIElement, ? extends IUIElement> function)
    {
        try
        {
            for (Map.Entry<String, IUIElement> childEntry : entrySet())
            {
                final IUIElement newElement = function.apply(childEntry.getKey(), childEntry.getValue());
                if (newElement instanceof IClickAcceptingUIElement)
                {
                    throw new IllegalArgumentException("Buttons cannot contain other clickable elements");
                }

                childEntry.setValue(newElement);
            }
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Failed to replace some elements.", ex);
        }
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




}
