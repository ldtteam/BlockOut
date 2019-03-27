package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.ldtteam.blockout.util.Constants.Controls.CheckBox.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class CheckBox extends AbstractSimpleUIElement implements IDrawableUIElement, IClickAcceptingUIElement
{
    @NotNull
    public IDependencyObject<IIdentifier> normalBackgroundImageResource;
    @NotNull
    public IDependencyObject<IIdentifier> checkedBackgroundImageResource;
    @NotNull
    public IDependencyObject<IIdentifier> disabledBackgroundImageResource;
    @NotNull
    public IDependencyObject<Boolean>     checked;

    @NotNull
    public Event<CheckBox, CheckBoxCheckChangedEventArgs> onCheckedChanged = new Event<>(CheckBox.class, CheckBoxCheckChangedEventArgs.class);

    public CheckBox(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<IIdentifier> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<IIdentifier> normalBackgroundImageResource,
      @NotNull final IDependencyObject<IIdentifier> checkedBackgroundImageResource,
      @NotNull final IDependencyObject<IIdentifier> disabledBackgroundImageResource,
      @NotNull final IDependencyObject<Boolean> checked)
    {
        super(KEY_CHECKBOX, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);

        this.normalBackgroundImageResource = normalBackgroundImageResource;
        this.checkedBackgroundImageResource = checkedBackgroundImageResource;
        this.disabledBackgroundImageResource = disabledBackgroundImageResource;

        this.checked = checked;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (normalBackgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (checkedBackgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (disabledBackgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (checked.hasChanged(getDataContext()))
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
        else if (isChecked())
        {
            drawBackground(controller,
              this::getCheckedBackgroundImage);
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

    public boolean isChecked()
    {
        return checked.get(this);
    }

    public void setChecked(@NotNull final boolean checked)
    {
        final boolean currentClickState = isChecked();
        this.checked.set(this, checked);

        if (currentClickState != checked)
        {
            getParent().getUiManager().getUpdateManager().markDirty();
        }
    }

    @NotNull
    public ImageResource getCheckedBackgroundImage()
    {
        return getResource(getCheckedBackgroundImageResource());
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
    public IIdentifier getCheckedBackgroundImageResource()
    {
        return checkedBackgroundImageResource.get(this);
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

    public void setCheckedBackgroundImageResource(@NotNull final IIdentifier checkedBackgroundImage)
    {
        this.checkedBackgroundImageResource.set(this, checkedBackgroundImage);
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
        setChecked(!isChecked());
        onCheckedChanged.raise(this, new CheckBoxCheckChangedEventArgs(isChecked(), localX, localY, button));
    }

    public static class ButtonConstructionDataBuilder extends SimpleControlConstructionDataBuilder<ButtonConstructionDataBuilder, CheckBox>
    {

        public ButtonConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, CheckBox.class);
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
        public ButtonConstructionDataBuilder withCheckedBackgroundImageResource(@NotNull final IDependencyObject<IIdentifier> checkedBackgroundImageResource)
        {
            return withDependency("checkedBackgroundImageResource", checkedBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withCheckedBackgroundImageResource(@NotNull final IIdentifier checkedBackgroundImageResource)
        {
            return withDependency("checkedBackgroundImageResource", DependencyObjectHelper.createFromValue(checkedBackgroundImageResource));
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
        public ButtonConstructionDataBuilder withCheckedChangedEventHandler(@NotNull final IEventHandler<CheckBox, CheckBoxCheckChangedEventArgs> eventHandler)
        {
            return withEventHandler("onCheckedChanged", CheckBoxCheckChangedEventArgs.class, eventHandler);
        }
    }

    public static class Factory extends AbstractSimpleUIElementFactory<CheckBox>
    {

        public Factory()
        {
            super(CheckBox.class, KEY_CHECKBOX, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<IIdentifier> defaultBackgroundImage = elementData.getFromRawDataWithDefault(CONST_DEFAULT_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING));
                final IDependencyObject<IIdentifier> checkedBackgroundImage = elementData.getFromRawDataWithDefault(CONST_CHECKED_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING));
                final IDependencyObject<IIdentifier> disabledBackgroundImage = elementData.getFromRawDataWithDefault(CONST_DISABLED_BACKGROUND_IMAGE, engine, IIdentifier.create(MISSING));
                final IDependencyObject<Boolean> checked = elementData.getFromRawDataWithDefault(CONST_INITIALLY_CHECKED, engine, false);

                final CheckBox element = new CheckBox(
                  id,
                  parent,
                  styleId,
                  alignments,
                  dock,
                  margin,
                  elementSize,
                  dataContext,
                  visible,
                  enabled,
                  defaultBackgroundImage,
                  checkedBackgroundImage,
                  disabledBackgroundImage,
                  checked);

                return element;
            }, (element, builder) -> builder
                                       .addComponent(CONST_DEFAULT_BACKGROUND_IMAGE, element.getNormalBackgroundImageResource())
                                       .addComponent(CONST_DISABLED_BACKGROUND_IMAGE, element.getDisabledBackgroundImageResource())
                                       .addComponent(CONST_CHECKED_BACKGROUND_IMAGE, element.getCheckedBackgroundImageResource())
                                       .addComponent(CONST_INITIALLY_CHECKED, element.isChecked()));
        }
    }

    public static class CheckBoxCheckChangedEventArgs
    {
        private final boolean     isChecked;
        private final int         localX;
        private final int         localY;
        private final MouseButton button;
        private final float       timeDelta;

        public CheckBoxCheckChangedEventArgs(final boolean isChecked, final int localX, final int localY, final MouseButton button)
        {
            this.isChecked = isChecked;
            this.localX = localX;
            this.localY = localY;
            this.button = button;
            this.timeDelta = 0f;
        }

        public CheckBoxCheckChangedEventArgs(final boolean isChecked, final int localX, final int localY, final MouseButton button, final float timeDelta)
        {
            this.isChecked = isChecked;
            this.localX = localX;
            this.localY = localY;
            this.button = button;
            this.timeDelta = timeDelta;
        }

        public boolean isChecked()
        {
            return isChecked;
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
