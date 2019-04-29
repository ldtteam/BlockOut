package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractFilteringChildrenContainingUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.input.IClickAcceptingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.event.Event;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.ldtteam.jvoxelizer.client.renderer.opengl.IOpenGl;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.ldtteam.blockout.util.Constants.Controls.Button.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class Button extends AbstractFilteringChildrenContainingUIElement implements IDrawableUIElement, IClickAcceptingUIElement
{
    @NotNull
    public IDependencyObject<IIdentifier> normalBackgroundImageResource;
    @NotNull
    public IDependencyObject<IIdentifier> clickedBackgroundImageResource;
    @NotNull
    public IDependencyObject<IIdentifier> disabledBackgroundImageResource;
    @NotNull
    public IDependencyObject<Boolean>     clicked;

    @NotNull
    public Event<Button, ButtonClickedEventArgs> onClicked = new Event<>(Button.class, Button.ButtonClickedEventArgs.class);

    public Button(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<IIdentifier> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<IIdentifier> normalBackgroundImageResource,
      @NotNull final IDependencyObject<IIdentifier> clickedBackgroundImageResource,
      @NotNull final IDependencyObject<IIdentifier> disabledBackgroundImageResource,
      @NotNull final IDependencyObject<Boolean> clicked)
    {
        super(KEY_BUTTON, styleId, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

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

        IOpenGl.pushMatrix();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        IOpenGl.popMatrix();
    }

    @NotNull
    public ImageResource getDisabledBackgroundImage()
    {
        return getResource(getDisabledBackgroundImageResource());
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

    @NotNull
    public ImageResource getClickedBackgroundImage()
    {
        return getResource(getClickedBackgroundImageResource());
    }

    @NotNull
    public ImageResource getNormalBackgroundImage()
    {
        return getResource(getNormalBackgroundImageResource());
    }

    @NotNull
    public IIdentifier getDisabledBackgroundImageResource()
    {
        return disabledBackgroundImageResource.get(this);
    }

    @NotNull
    public IIdentifier getClickedBackgroundImageResource()
    {
        return clickedBackgroundImageResource.get(this);
    }

    @NotNull
    public IIdentifier getNormalBackgroundImageResource()
    {
        return normalBackgroundImageResource.get(this);
    }

    public void setNormalBackgroundImageResource(@NotNull final IIdentifier normalBackgroundImage)
    {
        this.normalBackgroundImageResource.set(this, normalBackgroundImage);
    }

    public void setClickedBackgroundImageResource(@NotNull final IIdentifier clickedBackgroundImage)
    {
        this.clickedBackgroundImageResource.set(this, clickedBackgroundImage);
    }

    public void setDisabledBackgroundImageResource(@NotNull final IIdentifier disabledBackgroundImage)
    {
        this.disabledBackgroundImageResource.set(this, disabledBackgroundImage);
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
        //Noop
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
        public ButtonConstructionDataBuilder withNormalBackgroundImageResource(@NotNull final IDependencyObject<IIdentifier> normalBackgroundImageResource)
        {
            return withDependency("normalBackgroundImageResource", normalBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withNormalBackgroundImageResource(@NotNull final IIdentifier normalBackgroundImageResource)
        {
            return withDependency("normalBackgroundImageResource", DependencyObjectHelper.createFromValue(normalBackgroundImageResource));
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedBackgroundImageResource(@NotNull final IDependencyObject<IIdentifier> clickedBackgroundImageResource)
        {
            return withDependency("clickedBackgroundImageResource", clickedBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedBackgroundImageResource(@NotNull final IIdentifier clickedBackgroundImageResource)
        {
            return withDependency("clickedBackgroundImageResource", DependencyObjectHelper.createFromValue(clickedBackgroundImageResource));
        }

        @NotNull
        public ButtonConstructionDataBuilder withDisabledBackgroundImageResource(@NotNull final IDependencyObject<IIdentifier> disabledBackgroundImageResource)
        {
            return withDependency("disabledBackgroundImageResource", disabledBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withDisabledBackgroundImageResource(@NotNull final IIdentifier disabledBackgroundImageResource)
        {
            return withDependency("disabledBackgroundImageResource", DependencyObjectHelper.createFromValue(disabledBackgroundImageResource));
        }

        @NotNull
        public ButtonConstructionDataBuilder withClickedEventHandler(@NotNull final IEventHandler<Button, ButtonClickedEventArgs> eventHandler)
        {
            return withEventHandler("onClicked", ButtonClickedEventArgs.class, eventHandler);
        }
    }

    public static class Factory extends AbstractChildrenContainingUIElementFactory<Button>
    {

        public Factory()
        {
            super(Button.class, KEY_BUTTON, (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<IIdentifier> defaultBackgroundImage =
                  elementData.getFromRawDataWithDefault(CONST_DEFAULT_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING), IIdentifier.class);
                final IDependencyObject<IIdentifier> clickedBackgroundImage =
                  elementData.getFromRawDataWithDefault(CONST_CLICKED_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING), IIdentifier.class);
                final IDependencyObject<IIdentifier> disabledBackgroundImage =
                  elementData.getFromRawDataWithDefault(CONST_DISABLED_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING), IIdentifier.class);
                final IDependencyObject<Boolean> clicked = elementData.getFromRawDataWithDefault(CONST_INITIALLY_CLICKED, engine, false, Boolean.class);

                final Button element = new Button(
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
                  defaultBackgroundImage,
                  clickedBackgroundImage,
                  disabledBackgroundImage,
                  clicked);

                return element;
            }, (element, builder) -> builder
                                       .addComponent(CONST_DEFAULT_BACKGROUND_IMAGE, element.getNormalBackgroundImageResource(), IIdentifier.class)
                                       .addComponent(CONST_DISABLED_BACKGROUND_IMAGE, element.getDisabledBackgroundImageResource(), IIdentifier.class)
                                       .addComponent(CONST_CLICKED_BACKGROUND_IMAGE, element.getClickedBackgroundImageResource(), IIdentifier.class)
                                       .addComponent(CONST_INITIALLY_CLICKED, element.isClicked(), Boolean.class));
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
