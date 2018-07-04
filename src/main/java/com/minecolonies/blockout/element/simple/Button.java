package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.element.core.AbstractFilteringChildrenContainingUIElement;
import com.minecolonies.blockout.event.Event;
import com.minecolonies.blockout.event.IEventHandler;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.style.resources.ImageResource;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.minecolonies.blockout.util.Constants.Controls.Button.*;
import static com.minecolonies.blockout.util.Constants.Controls.General.*;

public class Button extends AbstractFilteringChildrenContainingUIElement implements IDrawableUIElement, IClickAcceptingUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> normalBackgroundImageResource;
    @NotNull
    private IDependencyObject<ResourceLocation> clickedBackgroundImageResource;
    @NotNull
    private IDependencyObject<ResourceLocation> disabledBackgroundImageResource;
    @NotNull
    private IDependencyObject<Boolean>          clicked;

    @NotNull
    private Event<Button, ButtonClickedEventArgs> onClicked = new Event<>(Button.class, Button.ButtonClickedEventArgs.class);

    public Button(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_BUTTON, style, id, parent);

        this.normalBackgroundImageResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.clickedBackgroundImageResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.disabledBackgroundImageResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));

        this.clicked = DependencyObjectHelper.createFromValue(false);
    }

    public Button(
      @NotNull final IDependencyObject<ResourceLocation> style,
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
      @NotNull final IDependencyObject<ResourceLocation> normalBackgroundImageResource,
      @NotNull final IDependencyObject<ResourceLocation> clickedBackgroundImageResource,
      @NotNull final IDependencyObject<ResourceLocation> disabledBackgroundImageResource,
      @NotNull final IDependencyObject<Boolean> clicked)
    {
        super(KEY_BUTTON, style, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

        this.normalBackgroundImageResource = normalBackgroundImageResource;
        this.clickedBackgroundImageResource = clickedBackgroundImageResource;
        this.disabledBackgroundImageResource = disabledBackgroundImageResource;

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
              this::getDisabledBackgroundImage);
        }
        else if (isClicked())
        {
            drawBackground(controller,
              this::getClickedBackgroundImage);
        }
        else
        {
            drawBackground(controller,
              this::getNormalBackgroundImage);
        }
    }

    private void drawBackground(
      @NotNull final IRenderingController controller,
      Supplier<ImageResource> resourceSupplier)
    {
        final ImageResource resource = resourceSupplier.get();
        final Vector2d size = getLocalBoundingBox().getSize();
        final Vector2d scalingFactor = resource.getScalingFactor(size);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(getLocalBoundingBox().getLocalOrigin(),
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        GlStateManager.popMatrix();
    }

    public boolean isClicked()
    {
        return clicked.get(getDataContext());
    }

    public void setClicked(@NotNull final boolean clicked)
    {
        final boolean currentClickState = isClicked();
        this.clicked = DependencyObjectHelper.createFromValue(clicked);

        if (currentClickState != clicked)
        {
            getUiManager().getUpdateManager().markDirty();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
        //Noop
    }

    @NotNull
    public ImageResource getNormalBackgroundImage()
    {
        return getResource(getNormalBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getNormalBackgroundImageResource()
    {
        return normalBackgroundImageResource.get(getDataContext());
    }

    public void setNormalBackgroundImageResource(@NotNull final ResourceLocation normalBackgroundImage)
    {
        this.normalBackgroundImageResource = DependencyObjectHelper.createFromValue(normalBackgroundImage);
    }

    @NotNull
    public ImageResource getClickedBackgroundImage()
    {
        return getResource(getClickedBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getClickedBackgroundImageResource()
    {
        return clickedBackgroundImageResource.get(getDataContext());
    }

    public void setClickedBackgroundImageResource(@NotNull final ResourceLocation clickedBackgroundImage)
    {
        this.clickedBackgroundImageResource = DependencyObjectHelper.createFromValue(clickedBackgroundImage);
    }

    @NotNull
    public ImageResource getDisabledBackgroundImage()
    {
        return getResource(getDisabledBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getDisabledBackgroundImageResource()
    {
        return disabledBackgroundImageResource.get(getDataContext());
    }

    public void setDisabledBackgroundImageResource(@NotNull final ResourceLocation disabledBackgroundImage)
    {
        this.disabledBackgroundImageResource = DependencyObjectHelper.createFromValue(disabledBackgroundImage);
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

    @Override
    public Predicate<IUIElement> IsValidChildPredicate()
    {
        return iuiElement -> !(iuiElement instanceof IClickAcceptingUIElement);
    }

    public static class ButtonConstructionDataBuilder extends AbstractChildrenContainingUIElement.SimpleControlConstructionDataBuilder<ButtonConstructionDataBuilder, Button>
    {

        public ButtonConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, Button.class);
        }

        @NotNull
        public ButtonConstructionDataBuilder withNormalBackgroundImageResource(@NotNull final IDependencyObject<ResourceLocation> normalBackgroundImageResource)
        {
            return withDependency("normalBackgroundImageResource", normalBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withNormalBackgroundImageResource(@NotNull final ResourceLocation normalBackgroundImageResource)
        {
            return withDependency("normalBackgroundImageResource", DependencyObjectHelper.createFromValue(normalBackgroundImageResource));
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedBackgroundImageResource(@NotNull final IDependencyObject<ResourceLocation> clickedBackgroundImageResource)
        {
            return withDependency("clickedBackgroundImageResource", clickedBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedBackgroundImageResource(@NotNull final ResourceLocation clickedBackgroundImageResource)
        {
            return withDependency("clickedBackgroundImageResource", DependencyObjectHelper.createFromValue(clickedBackgroundImageResource));
        }

        @NotNull
        public ButtonConstructionDataBuilder withDisabledBackgroundImageResource(@NotNull final IDependencyObject<ResourceLocation> disabledBackgroundImageResource)
        {
            return withDependency("disabledBackgroundImageResource", disabledBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDisabledBackgroundImageResource(@NotNull final ResourceLocation disabledBackgroundImageResource)
        {
            return withDependency("disabledBackgroundImageResource", DependencyObjectHelper.createFromValue(disabledBackgroundImageResource));
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
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getStringAttribute(CONST_ID);
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<AxisDistance> padding = elementData.getBoundAxisDistanceAttribute(CONST_PADDING);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> defaultBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_DEFAULT_BACKGROUND_IMAGE);
            final IDependencyObject<ResourceLocation> clickedBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_CLICKED_BACKGROUND_IMAGE);
            final IDependencyObject<ResourceLocation> disabledBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_DISABLED_BACKGROUND_IMAGE);
            final IDependencyObject<Boolean> clicked = elementData.getBoundBooleanAttribute(CONST_INITIALLY_CLICKED);


            final Button button = new Button(
              style,
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
              clickedBackgroundImage,
              disabledBackgroundImage,
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
              .addResourceLocation(CONST_DEFAULT_BACKGROUND_IMAGE, element.getNormalBackgroundImageResource())
              .addResourceLocation(CONST_DISABLED_BACKGROUND_IMAGE, element.getDisabledBackgroundImageResource())
              .addResourceLocation(CONST_CLICKED_BACKGROUND_IMAGE, element.getClickedBackgroundImageResource())
              .addBoolean(CONST_INITIALLY_CLICKED, element.isClicked());

            element.values().forEach(child -> {
                builder.addChild(BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(child));
            });
        }
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
