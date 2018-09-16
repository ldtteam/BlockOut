package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.core.element.IUIElementHost;
import com.ldtteam.blockout.core.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.core.element.input.IClickAcceptingUIElement;
import com.ldtteam.blockout.core.element.values.Alignment;
import com.ldtteam.blockout.core.element.values.AxisDistance;
import com.ldtteam.blockout.core.element.values.Dock;
import com.ldtteam.blockout.core.factory.IUIElementFactory;
import com.ldtteam.blockout.core.management.update.IUpdateManager;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.core.AbstractFilteringChildrenContainingUIElement;
import com.ldtteam.blockout.event.Event;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.loader.IUIElementData;
import com.ldtteam.blockout.loader.IUIElementDataBuilder;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.ldtteam.blockout.util.Constants.Controls.Button.*;
import static com.ldtteam.blockout.util.Constants.Controls.General.*;

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

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (normalBackgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (clickedBackgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (disabledBackgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (clicked.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
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

        GlStateManager.pushMatrix();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        GlStateManager.popMatrix();
    }

    public boolean isClicked()
    {
        return clicked.get(this);
    }

    public void setClicked(@NotNull final boolean clicked)
    {
        final boolean currentClickState = isClicked();
        this.clicked.set(this, clicked);

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
        return normalBackgroundImageResource.get(this);
    }

    public void setNormalBackgroundImageResource(@NotNull final ResourceLocation normalBackgroundImage)
    {
        this.normalBackgroundImageResource.set(this, normalBackgroundImage);
    }

    @NotNull
    public ImageResource getClickedBackgroundImage()
    {
        return getResource(getClickedBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getClickedBackgroundImageResource()
    {
        return clickedBackgroundImageResource.get(this);
    }

    public void setClickedBackgroundImageResource(@NotNull final ResourceLocation clickedBackgroundImage)
    {
        this.clickedBackgroundImageResource.set(this, clickedBackgroundImage);
    }

    @NotNull
    public ImageResource getDisabledBackgroundImage()
    {
        return getResource(getDisabledBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getDisabledBackgroundImageResource()
    {
        return disabledBackgroundImageResource.get(this);
    }

    public void setDisabledBackgroundImageResource(@NotNull final ResourceLocation disabledBackgroundImage)
    {
        this.disabledBackgroundImageResource.set(this, disabledBackgroundImage);
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
            final String id = elementData.getElementId();
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
            final IDependencyObject<Boolean> clicked = elementData.getBoundBooleanAttribute(CONST_INITIALLY_CLICKED, false);


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
        private final boolean     start;
        private final int         localX;
        private final int         localY;
        private final MouseButton button;
        private final float       timeDelta;

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
