package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.input.IClickAcceptingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.event.Event;
import com.ldtteam.blockout.event.IEventHandler;
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
import java.util.function.Supplier;

import static com.ldtteam.blockout.util.Constants.Controls.CheckBox.*;
import static com.ldtteam.blockout.util.Constants.Controls.General.*;

public class CheckBox extends AbstractSimpleUIElement implements IDrawableUIElement, IClickAcceptingUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> normalBackgroundImageResource;
    @NotNull
    private IDependencyObject<ResourceLocation> checkedBackgroundImageResource;
    @NotNull
    private IDependencyObject<ResourceLocation> disabledBackgroundImageResource;
    @NotNull
    private IDependencyObject<Boolean>          checked;

    @NotNull
    private Event<CheckBox, CheckBoxCheckChangedEventArgs> onCheckedChanged = new Event<>(CheckBox.class, CheckBoxCheckChangedEventArgs.class);

    public CheckBox(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_CHECKBOX, style, id, parent);

        this.normalBackgroundImageResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.checkedBackgroundImageResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.disabledBackgroundImageResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));

        this.checked = DependencyObjectHelper.createFromValue(false);
    }

    public CheckBox(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> normalBackgroundImageResource,
      @NotNull final IDependencyObject<ResourceLocation> checkedBackgroundImageResource,
      @NotNull final IDependencyObject<ResourceLocation> disabledBackgroundImageResource,
      @NotNull final IDependencyObject<Boolean> checked)
    {
        super(KEY_CHECKBOX, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);

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

        GlStateManager.pushMatrix();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        GlStateManager.popMatrix();
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
    public ImageResource getCheckedBackgroundImage()
    {
        return getResource(getCheckedBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getCheckedBackgroundImageResource()
    {
        return checkedBackgroundImageResource.get(this);
    }

    public void setCheckedBackgroundImageResource(@NotNull final ResourceLocation checkedBackgroundImage)
    {
        this.checkedBackgroundImageResource.set(this, checkedBackgroundImage);
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
        public ButtonConstructionDataBuilder withCheckedBackgroundImageResource(@NotNull final IDependencyObject<ResourceLocation> checkedBackgroundImageResource)
        {
            return withDependency("checkedBackgroundImageResource", checkedBackgroundImageResource);
        }

        @NotNull
        public ButtonConstructionDataBuilder withCheckedBackgroundImageResource(@NotNull final ResourceLocation checkedBackgroundImageResource)
        {
            return withDependency("checkedBackgroundImageResource", DependencyObjectHelper.createFromValue(checkedBackgroundImageResource));
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
        public ButtonConstructionDataBuilder withCheckedChangedEventHandler(@NotNull final IEventHandler<CheckBox, CheckBoxCheckChangedEventArgs> eventHandler)
        {
            return withEventHandler("onCheckedChanged", CheckBoxCheckChangedEventArgs.class, eventHandler);
        }
    }

    public static class Factory implements IUIElementFactory<CheckBox>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_CHECKBOX;
        }

        @NotNull
        @Override
        public CheckBox readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getElementId();
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> defaultBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_DEFAULT_BACKGROUND_IMAGE);
            final IDependencyObject<ResourceLocation> checkedBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_CHECKED_BACKGROUND_IMAGE);
            final IDependencyObject<ResourceLocation> disabledBackgroundImage = elementData.getBoundResourceLocationAttribute(CONST_DISABLED_BACKGROUND_IMAGE);
            final IDependencyObject<Boolean> checked = elementData.getBoundBooleanAttribute(CONST_INITIALLY_CHECKED);


            final CheckBox checkBox = new CheckBox(
              style,
              id,
              elementData.getParentView(),
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

            return checkBox;
        }

        @Override
        public void writeToElementData(@NotNull final CheckBox element, @NotNull final IUIElementDataBuilder builder)
        {

            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_DEFAULT_BACKGROUND_IMAGE, element.getNormalBackgroundImageResource())
              .addResourceLocation(CONST_DISABLED_BACKGROUND_IMAGE, element.getDisabledBackgroundImageResource())
              .addResourceLocation(CONST_CHECKED_BACKGROUND_IMAGE, element.getCheckedBackgroundImageResource())
              .addBoolean(CONST_INITIALLY_CHECKED, element.isChecked());
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
