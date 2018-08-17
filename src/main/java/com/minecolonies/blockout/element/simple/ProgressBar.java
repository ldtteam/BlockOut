package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.ControlDirection;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.style.resources.ImageResource;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.ProgressBar.*;

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
    private IDependencyObject<ControlDirection> orientation;

    public ProgressBar(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_PROGRESS_BAR, style, id, parent);
    }

    public ProgressBar(
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
      @NotNull final IDependencyObject<ResourceLocation> backGroundResource,
      @NotNull final IDependencyObject<ResourceLocation> foreGroundResource,
      @NotNull final IDependencyObject<Double> value,
      @NotNull final IDependencyObject<Double> min,
      @NotNull final IDependencyObject<Double> max,
      @NotNull final IDependencyObject<ControlDirection> orientation)
    {
        super(KEY_PROGRESS_BAR, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);

        this.backGroundResource = backGroundResource;
        this.foreGroundResource = foreGroundResource;
        this.value = value;
        this.min = min;
        this.max = max;
        this.orientation = orientation;
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
        return backGroundResource.get(getDataContext());
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
        this.backGroundResource = DependencyObjectHelper.createFromValue(backGroundResource);
    }

    @NotNull
    public ResourceLocation getForeGroundResource()
    {
        return foreGroundResource.get(getDataContext());
    }

    public void setForeGroundResource(@NotNull final ResourceLocation foreGroundResource)
    {
        this.foreGroundResource = DependencyObjectHelper.createFromValue(foreGroundResource);
    }

    @NotNull
    public Double getValue()
    {
        return value.get(getDataContext());
    }

    public void setValue(@NotNull final Double value)
    {
        this.value = DependencyObjectHelper.createFromValue(value);
    }

    @NotNull
    public Double getMin()
    {
        return min.get(getDataContext());
    }

    public void setMin(@NotNull final Double min)
    {
        this.min = DependencyObjectHelper.createFromValue(min);
    }

    @NotNull
    public Double getMax()
    {
        return max.get(getDataContext());
    }

    public void setMax(@NotNull final Double max)
    {
        this.max = DependencyObjectHelper.createFromValue(max);
    }

    @NotNull
    public ControlDirection getOrientation()
    {
        return orientation.get(getDataContext());
    }

    public void setOrientation(@NotNull final ControlDirection orientation)
    {
        this.orientation = DependencyObjectHelper.createFromValue(orientation);
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
        public ProgressBarConstructionDataBuilder withDependentOrientation(@NotNull final IDependencyObject<ControlDirection> orientation)
        {
            return withDependency("orientation", orientation);
        }

        public ProgressBarConstructionDataBuilder withOrientation(@NotNull final ControlDirection orientation)
        {
            return withDependency("orientation", DependencyObjectHelper.createFromValue(orientation));
        }
    }

    public static class Factory implements IUIElementFactory<ProgressBar>
    {

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

        /**
         * Creates a new {@link ProgressBar} from the given {@link IUIElementData}.
         *
         * @param elementData The {@link IUIElementData} which contains the data that is to be constructed.
         * @return The {@link ProgressBar} that is stored in the {@link IUIElementData}.
         */
        @NotNull
        @Override
        public ProgressBar readFromElementData(@NotNull final IUIElementData elementData)
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
            final IDependencyObject<ResourceLocation> background = elementData.getBoundResourceLocationAttribute(CONST_BACKGROUND_IMAGE);
            final IDependencyObject<ResourceLocation> foreground = elementData.getBoundResourceLocationAttribute(CONST_FOREGROUND_IMAGE);
            final IDependencyObject<Double> min = elementData.getBoundDoubleAttribute(CONST_MIN);
            final IDependencyObject<Double> max = elementData.getBoundDoubleAttribute(CONST_MAX);
            final IDependencyObject<Double> value = elementData.getBoundDoubleAttribute(CONST_VALUE);
            final IDependencyObject<ControlDirection> orientation = elementData.getBoundControlDirectionAttribute(CONST_ORIENTATION);

            return new ProgressBar(
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
              background,
              foreground,
              min,
              max,
              value,
              orientation);
        }

        /**
         * Populates the given {@link IUIElementDataBuilder} with the data from {@link ProgressBar} so that
         * the given {@link ProgressBar} can be reconstructed with {@link #readFromElementData(IUIElementData)} created by
         * the given {@link IUIElementDataBuilder}.
         *
         * @param element The {@link ProgressBar} to write into the {@link IUIElementDataBuilder}
         * @param builder The {@link IUIElementDataBuilder} to write the {@link ProgressBar} into.
         */
        @Override
        public void writeToElementData(@NotNull final ProgressBar element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_BACKGROUND_IMAGE, element.getBackGroundResource())
              .addResourceLocation(CONST_FOREGROUND_IMAGE, element.getForeGroundResource())
              .addDouble(CONST_MIN, element.getMin())
              .addDouble(CONST_MAX, element.getMax())
              .addDouble(CONST_VALUE, element.getValue())
              .addControlDirection(CONST_ORIENTATION, element.getOrientation());
        }
    }
}