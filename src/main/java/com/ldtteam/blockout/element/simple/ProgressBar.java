package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementDataBuilder;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.ProgressBar.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class ProgressBar extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> backGroundResource;
    @NotNull
    private IDependencyObject<ResourceLocation> foreGroundResource;
    @NotNull
    private IDependencyObject<Double>           value;
    @NotNull
    private IDependencyObject<Double>           min;
    @NotNull
    private IDependencyObject<Double>           max;
    @Nonnull
    private IDependencyObject<Orientation>      orientation;

    public ProgressBar(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_PROGRESS_BAR, style, id, parent);
    }

    public ProgressBar(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> backGroundResource,
      @NotNull final IDependencyObject<ResourceLocation> foreGroundResource,
      @NotNull final IDependencyObject<Double> value,
      @NotNull final IDependencyObject<Double> min,
      @NotNull final IDependencyObject<Double> max,
      @NotNull final IDependencyObject<Orientation> orientation)
    {
        super(KEY_PROGRESS_BAR, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);

        this.backGroundResource = backGroundResource;
        this.foreGroundResource = foreGroundResource;
        this.value = value;
        this.min = min;
        this.max = max;
        this.orientation = orientation;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (backGroundResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (foreGroundResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (value.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (min.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (max.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (orientation.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        controller.getScissoringController().focus(this);
        GlStateManager.pushMatrix();

        final ImageResource backGround = getBackGround();
        final ImageResource foreGround = getForeGround();

        final BoundingBox localBox = getLocalBoundingBox();
        final BoundingBox absoluteBox = getAbsoluteBoundingBox();
        final BoundingBox scissoredForeground = getForegroundRenderingBox(absoluteBox);


        final Vector2d backgroundScalingFactor = backGround.getScalingFactor(absoluteBox.getSize());

        GlStateManager.scale(backgroundScalingFactor.getX(), backgroundScalingFactor.getY(), 1f);

        controller.bindTexture(backGround.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          localBox.getSize(),
          backGround.getOffset(),
          backGround.getSize(),
          backGround.getFileSize());

        GlStateManager.popMatrix();
        controller.getScissoringController().pop();

        controller.getScissoringController().push(scissoredForeground);
        GlStateManager.pushMatrix();

        final Vector2d foregroundScalingFactor = foreGround.getScalingFactor(scissoredForeground.getSize());
        GlStateManager.scale(foregroundScalingFactor.getX(), foregroundScalingFactor.getY(), 1f);

        controller.bindTexture(foreGround.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          localBox.getSize(),
          foreGround.getOffset(),
          foreGround.getSize(),
          foreGround.getFileSize());

        GlStateManager.popMatrix();
        controller.getScissoringController().pop();
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    @NotNull
    public ImageResource getBackGround()
    {
        return getResource(getBackGroundResource());
    }

    @NotNull
    public ImageResource getForeGround()
    {
        return getResource(getForeGroundResource());
    }

    @NotNull
    public ResourceLocation getBackGroundResource()
    {
        return backGroundResource.get(this);
    }

    public BoundingBox getForegroundRenderingBox(@NotNull final BoundingBox inputBox)
    {
        final Vector2d size = inputBox.getSize();
        final double relativeProgression = ((getValue() - getMin()) / (getMax() - getMin()));

        switch (getOrientation())
        {
            case LEFT_RIGHT:
            {
                final double width = size.getX() * relativeProgression;
                return new BoundingBox(inputBox.getLocalOrigin(), new Vector2d(width, size.getY()));
            }
            case RIGHT_LEFT:
            {
                final double width = size.getX() * relativeProgression;
                final Vector2d translatedOrigin = inputBox.getLocalOrigin().move(size.getX() - width, 0).nullifyNegatives();
                return new BoundingBox(translatedOrigin, new Vector2d(width, size.getY()));
            }
            case TOP_BOTTOM:
            {
                final double height = size.getY() * relativeProgression;
                final Vector2d translatedOrigin = inputBox.getLocalOrigin().move(0, size.getY() - height).nullifyNegatives();
                return new BoundingBox(translatedOrigin, new Vector2d(size.getX(), height));
            }
            case BOTTOM_TOP:
            {
                final double height = size.getY() * relativeProgression;
                return new BoundingBox(inputBox.getLocalOrigin(), new Vector2d(size.getX(), height));
            }
        }

        throw new IllegalStateException(String.format("Unsupported orientation for ProgressBar: %s", getOrientation()));
    }

    public void setBackGroundResource(@NotNull final ResourceLocation backGroundResource)
    {
        this.backGroundResource.set(this, backGroundResource);
    }

    @NotNull
    public ResourceLocation getForeGroundResource()
    {
        return foreGroundResource.get(this);
    }

    public void setForeGroundResource(@NotNull final ResourceLocation foreGroundResource)
    {
        this.foreGroundResource.set(this, foreGroundResource);
    }

    @NotNull
    public Double getValue()
    {
        return value.get(this);
    }

    public void setValue(@NotNull final Double value)
    {
        this.value.set(this, value);
    }

    @NotNull
    public Double getMin()
    {
        return min.get(this);
    }

    public void setMin(@NotNull final Double min)
    {
        this.min.set(this, min);
    }

    @NotNull
    public Double getMax()
    {
        return max.get(this);
    }

    public void setMax(@NotNull final Double max)
    {
        this.max.set(this, max);
    }

    @NotNull
    public Orientation getOrientation()
    {
        return orientation.get(this);
    }

    public void setOrientation(@NotNull final Orientation orientation)
    {
        this.orientation.set(this, orientation);
    }

    public static class ProgressBarConstructionDataBuilder extends AbstractSimpleUIElement.SimpleControlConstructionDataBuilder<ProgressBarConstructionDataBuilder, ProgressBar>
    {

        protected ProgressBarConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, ProgressBar.class);
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withDependentBackgroundImageResource(@NotNull final IDependencyObject<ResourceLocation> backgroundImageResource)
        {
            return withDependency("backGroundResource", backgroundImageResource);
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withBackgroundImageResource(@NotNull final ResourceLocation backgroundImageResource)
        {
            return withDependency("backGroundResource", DependencyObjectHelper.createFromValue(backgroundImageResource));
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withDependentForegroundImageResource(@NotNull final IDependencyObject<ResourceLocation> foregroundImageResource)
        {
            return withDependency("foreGroundResource", foregroundImageResource);
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withForegroundImageResource(@NotNull final ResourceLocation foregroundImageResource)
        {
            return withDependency("foreGroundResource", DependencyObjectHelper.createFromValue(foregroundImageResource));
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withDependentMinValue(@NotNull final IDependencyObject<Double> min)
        {
            return withDependency("min", min);
        }

        public ProgressBarConstructionDataBuilder withMinValue(@NotNull final Double min)
        {
            return withDependency("min", DependencyObjectHelper.createFromValue(min));
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withDependentMaxValue(@NotNull final IDependencyObject<Double> max)
        {
            return withDependency("max", max);
        }

        public ProgressBarConstructionDataBuilder withMaxValue(@NotNull final Double max)
        {
            return withDependency("max", DependencyObjectHelper.createFromValue(max));
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withDependentValue(@NotNull final IDependencyObject<Double> value)
        {
            return withDependency("value", value);
        }

        public ProgressBarConstructionDataBuilder withValue(@NotNull final Double value)
        {
            return withDependency("value", DependencyObjectHelper.createFromValue(value));
        }

        @NotNull
        public ProgressBarConstructionDataBuilder withDependentOrientation(@NotNull final IDependencyObject<Orientation> orientation)
        {
            return withDependency("orientation", orientation);
        }

        public ProgressBarConstructionDataBuilder withOrientation(@NotNull final Orientation orientation)
        {
            return withDependency("orientation", DependencyObjectHelper.createFromValue(orientation));
        }
    }

    public static class Factory extends AbstractSimpleUIElementFactory<ProgressBar>
    {

        protected Factory()
        {
            super((elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<ResourceLocation> background = elementData.getFromRawDataWithDefault(CONST_BACKGROUND_IMAGE, engine, MISSING);
                final IDependencyObject<ResourceLocation> foreground = elementData.getFromRawDataWithDefault(CONST_FOREGROUND_IMAGE, engine, MISSING);
                final IDependencyObject<Double> min = elementData.getFromRawDataWithDefault(CONST_MIN, engine, 0d);
                final IDependencyObject<Double> max = elementData.getFromRawDataWithDefault(CONST_MAX, engine, 100d);
                final IDependencyObject<Double> value = elementData.getFromRawDataWithDefault(CONST_VALUE, engine, 50d);
                final IDependencyObject<Orientation> orientation = elementData.getFromRawDataWithDefault(CONST_ORIENTATION, engine, Orientation.LEFT_RIGHT);

                final ProgressBar element = new ProgressBar(
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
                  background,
                  foreground,
                  min,
                  max,
                  value,
                  orientation
                );

                return element;
            }, (element, builder) -> builder
              .addComponent(CONST_BACKGROUND_IMAGE, element.getBackGroundResource())
              .addComponent(CONST_FOREGROUND_IMAGE, element.getForeGroundResource())
              .addComponent(CONST_MIN, element.getMin())
              .addComponent(CONST_MAX, element.getMax())
              .addComponent(CONST_VALUE, element.getValue())
              .addComponent(CONST_ORIENTATION, element.getOrientation()));
        }

        /**
         * Returns the type that this factory builds.
         *
         * @return The type.
         */
        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_PROGRESS_BAR;
        }
    }
}