package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.input.IClickAcceptingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.event.Event;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.ldtteam.blockout.util.Constants.Controls.RangeSelector.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class RangeSelector extends AbstractSimpleUIElement implements IClickAcceptingUIElement, IDrawableUIElement
{
    private static final int CONST_SELECTOR_SIZE = 5;

    public static final class Factory extends AbstractSimpleUIElement.AbstractSimpleUIElementFactory<RangeSelector>
    {
        public Factory()
        {
            super(RangeSelector.class, KEY_RANGE_SELECTOR, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                @NotNull final IDependencyObject<Float> leftValue = elementData.getFromRawDataWithDefault(CONST_LEFT_VALUE, engine, 0f);
                @NotNull final IDependencyObject<Float> rightValue = elementData.getFromRawDataWithDefault(CONST_RIGHT_VALUE, engine, 1f);

                @NotNull final IDependencyObject<ResourceLocation> leftBackgroundTexture = elementData.getFromRawDataWithDefault(CONST_LEFT_BACKGROUND, engine, MISSING);
                @NotNull final IDependencyObject<ResourceLocation> selectedRegionBackgroundTexture =
                  elementData.getFromRawDataWithDefault(CONST_SELECTED_BACKGROUND, engine, MISSING);
                @NotNull final IDependencyObject<ResourceLocation> rightBackgroundTexture = elementData.getFromRawDataWithDefault(CONST_RIGHT_BACKGROUND, engine, MISSING);
                @NotNull final IDependencyObject<ResourceLocation> leftSelectorTexture = elementData.getFromRawDataWithDefault(CONST_LEFT_SELECTOR_BACKGROUND, engine, MISSING);
                @NotNull final IDependencyObject<ResourceLocation> rightSelectorTexture = elementData.getFromRawDataWithDefault(CONST_RIGHT_SELECTOR_BACKGROUND, engine, MISSING);

                return new RangeSelector(
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
                  leftValue,
                  rightValue,
                  leftBackgroundTexture,
                  selectedRegionBackgroundTexture,
                  rightBackgroundTexture,
                  leftSelectorTexture,
                  rightSelectorTexture
                );
            }, ((element, builder) -> builder
                                        .addComponent(CONST_LEFT_VALUE, element.getLeftValue())
                                        .addComponent(CONST_RIGHT_VALUE, element.getRightValue())
                                        .addComponent(CONST_LEFT_BACKGROUND, element.getLeftBackgroundTexture())
                                        .addComponent(CONST_SELECTED_BACKGROUND, element.getSelectedRegionBackgroundTexture())
                                        .addComponent(CONST_RIGHT_BACKGROUND, element.getRightBackgroundTexture())
                                        .addComponent(CONST_LEFT_SELECTOR_BACKGROUND, element.getLeftSelectorTexture())
                                        .addComponent(CONST_RIGHT_SELECTOR_BACKGROUND, element.getRightSelectorTexture())));
        }
    }

    @NotNull
    public  IDependencyObject<Float>                    leftValue;
    @NotNull
    public  IDependencyObject<Float>                    rightValue;
    @NotNull
    public  IDependencyObject<ResourceLocation>         leftBackgroundTexture;
    @NotNull
    public  IDependencyObject<ResourceLocation>         selectedRegionBackgroundTexture;
    @NotNull
    public  IDependencyObject<ResourceLocation>         rightBackgroundTexture;
    @NotNull
    public  IDependencyObject<ResourceLocation>         leftSelectorTexture;
    @NotNull
    public  IDependencyObject<ResourceLocation>         rightSelectorTexture;
    @NotNull
    public  Event<RangeSelector, RangeChangedEventArgs> onValuesChanged     = new Event<>(RangeSelector.class, RangeChangedEventArgs.class);
    @NotNull
    private Optional<MovementType>                      currentMovementType = Optional.empty();
    @NotNull
    private int                                         lastClickX          = -1;

    public RangeSelector(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<Float> leftValue,
      @NotNull final IDependencyObject<Float> rightValue,
      @NotNull final IDependencyObject<ResourceLocation> leftBackgroundTexture,
      @NotNull final IDependencyObject<ResourceLocation> selectedRegionBackgroundTexture,
      @NotNull final IDependencyObject<ResourceLocation> rightBackgroundTexture,
      @NotNull final IDependencyObject<ResourceLocation> leftSelectorTexture,
      @NotNull final IDependencyObject<ResourceLocation> rightSelectorTexture
    )
    {
        super(KEY_RANGE_SELECTOR, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);

        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.leftBackgroundTexture = leftBackgroundTexture;
        this.selectedRegionBackgroundTexture = selectedRegionBackgroundTexture;
        this.rightBackgroundTexture = rightBackgroundTexture;
        this.leftSelectorTexture = leftSelectorTexture;
        this.rightSelectorTexture = rightSelectorTexture;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (this.leftValue.hasChanged(this))
        {
            updateManager.markDirty();
        }
        if (this.rightValue.hasChanged(this))
        {
            updateManager.markDirty();
        }


        if (this.leftBackgroundTexture.hasChanged(this))
        {
            updateManager.markDirty();
        }
        if (this.selectedRegionBackgroundTexture.hasChanged(this))
        {
            updateManager.markDirty();
        }
        if (this.rightBackgroundTexture.hasChanged(this))
        {
            updateManager.markDirty();
        }
        if (this.leftSelectorTexture.hasChanged(this))
        {
            updateManager.markDirty();
        }
        if (this.rightSelectorTexture.hasChanged(this))
        {
            updateManager.markDirty();
        }
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        return true;
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        onMouseLeave();
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        final MovementType movementType = this.currentMovementType.orElseGet(() -> Arrays.stream(MovementType.values())
                                                                                     .filter(t -> t.ShouldSelect(this, localX))
                                                                                     .findFirst()
                                                                                     .orElseThrow(() -> new IllegalStateException("MovementType did not find any possible candidate")));

        this.currentMovementType = Optional.of(movementType);
        final float value = convertXPositionToValue(localX);

        Log.getLogger().info(value);

        if (!movementType.IsValid(this, value))
        {
            this.lastClickX = localX;
            return;
        }

        movementType.set(this, value);

        this.lastClickX = localX;

        getParent().getUiManager().getUpdateManager().markDirty();
    }

    @Override
    public void onMouseLeave()
    {
        this.currentMovementType = Optional.empty();
        this.lastClickX = -1;
    }

    public float convertXPositionToValue(final int x)
    {
        final double width = getLocalBoundingBox().getSize().getX();

        if (x < CONST_SELECTOR_SIZE)
        {
            return 0f;
        }

        if (x > (width - CONST_SELECTOR_SIZE))
        {
            return 1f;
        }

        final double v = (x - CONST_SELECTOR_SIZE) / (width - 2 * CONST_SELECTOR_SIZE);
        return (float) v;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);

        final ImageResource leftBackgroundResource = getLeftBackgroundTextureResource();
        final ImageResource selectorRegionBackgroundResource = getSelectedRegionBackgroundTextureResource();
        final ImageResource rightBackgroundResource = getRightBackgroundTextureResource();
        final ImageResource leftSelectorResource = getLeftSelectorTextureResource();
        final ImageResource rightSelectorResource = getRightSelectorTextureResource();

        final double height = getLocalBoundingBox().getSize().getY();

        final Vector2d leftBackgroundOffset = new Vector2d();
        final Vector2d leftBackgroundSize = new Vector2d(getLeftSelectorOffset() - CONST_SELECTOR_SIZE, height).nullifyNegatives();
        drawImageResource(controller, leftBackgroundResource, leftBackgroundOffset, leftBackgroundSize);

        final Vector2d leftSelectorOffset = leftBackgroundOffset.move(leftBackgroundSize.getX(), 0);
        final Vector2d leftSelectorSize = new Vector2d(CONST_SELECTOR_SIZE, height).nullifyNegatives();
        drawImageResource(controller, leftSelectorResource, leftSelectorOffset, leftSelectorSize);

        final Vector2d selectedRegionOffset = leftSelectorOffset.move(leftSelectorSize.getX(), 0);
        final Vector2d selectedRegionSize = new Vector2d(getRightSelectorOffset() - getLeftSelectorOffset(), height).nullifyNegatives();
        drawImageResource(controller, selectorRegionBackgroundResource, selectedRegionOffset, selectedRegionSize);

        final Vector2d rightSelectorOffset = selectedRegionOffset.move(selectedRegionSize.getX(), 0);
        final Vector2d rightSelectorSize = new Vector2d(CONST_SELECTOR_SIZE, height).nullifyNegatives();
        drawImageResource(controller, rightSelectorResource, rightSelectorOffset, rightSelectorSize);

        final Vector2d rightBackgroundOffset = rightSelectorOffset.move(rightSelectorSize.getX(), 0);
        final Vector2d rightBackgroundSize = new Vector2d(getLocalBoundingBox().getSize().getX() - rightBackgroundOffset.getX(), height).nullifyNegatives();
        drawImageResource(controller, rightBackgroundResource, rightBackgroundOffset, rightBackgroundSize);

        GlStateManager.popMatrix();
    }

    public ImageResource getLeftBackgroundTextureResource()
    {
        return getResource(getLeftBackgroundTexture());
    }

    public float getLeftValue()
    {
        return this.leftValue.get(this);
    }

    public ImageResource getSelectedRegionBackgroundTextureResource()
    {
        return getResource(getSelectedRegionBackgroundTexture());
    }

    public void setLeftValue(float value)
    {
        final float previous = getLeftValue();

        if (previous == value)
        {
            return;
        }

        this.leftValue.set(this, value);

        onValuesChanged.raise(this, new RangeChangedEventArgs(previous, getRightValue(), value, getRightValue()));
    }

    public ImageResource getRightBackgroundTextureResource()
    {
        return getResource(getRightBackgroundTexture());
    }

    public float getRightValue()
    {
        return this.rightValue.get(this);
    }

    public ImageResource getLeftSelectorTextureResource()
    {
        return getResource(getLeftSelectorTexture());
    }

    public void setRightValue(float value)
    {
        final float previous = getRightValue();

        if (previous == value)
        {
            return;
        }

        this.rightValue.set(this, value);

        onValuesChanged.raise(this, new RangeChangedEventArgs(getLeftValue(), previous, getLeftValue(), value));
    }

    public ImageResource getRightSelectorTextureResource()
    {
        return getResource(getRightSelectorTexture());
    }

    public float getLeftSelectorOffset()
    {
        return CONST_SELECTOR_SIZE + (float) (getLocalBoundingBox().getSize().getX() - 2 * CONST_SELECTOR_SIZE) * getLeftValue();
    }

    private void drawImageResource(@NotNull IRenderingController controller, @NotNull final ImageResource resource, @NotNull final Vector2d offset, @NotNull final Vector2d size)
    {
        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(offset,
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());
    }

    public float getRightSelectorOffset()
    {
        return CONST_SELECTOR_SIZE + (float) (getLocalBoundingBox().getSize().getX() - 2 * CONST_SELECTOR_SIZE) * getRightValue();
    }

    public ResourceLocation getLeftBackgroundTexture()
    {
        return this.leftBackgroundTexture.get(this);
    }

    public void setLeftBackgroundTexture(@NotNull final ResourceLocation leftBackgroundTexture)
    {
        this.leftBackgroundTexture.set(this, leftBackgroundTexture);
    }

    public ResourceLocation getSelectedRegionBackgroundTexture()
    {
        return this.selectedRegionBackgroundTexture.get(this);
    }

    public void setSelectedRegionBackgroundTexture(@NotNull final ResourceLocation selectedRegionBackgroundTexture)
    {
        this.selectedRegionBackgroundTexture.set(this, selectedRegionBackgroundTexture);
    }

    public ResourceLocation getRightBackgroundTexture()
    {
        return this.rightBackgroundTexture.get(this);
    }

    public void setRightBackgroundTexture(@NotNull final ResourceLocation rightBackgroundTexture)
    {
        this.rightBackgroundTexture.set(this, rightBackgroundTexture);
    }

    public ResourceLocation getLeftSelectorTexture()
    {
        return this.leftSelectorTexture.get(this);
    }

    public void setLeftSelectorTexture(@NotNull final ResourceLocation leftSelectorTexture)
    {
        this.leftSelectorTexture.set(this, leftSelectorTexture);
    }

    public ResourceLocation getRightSelectorTexture()
    {
        return this.rightSelectorTexture.get(this);
    }

    public void setRightSelectorTexture(@NotNull final ResourceLocation rightSelectorTexture)
    {
        this.rightSelectorTexture.set(this, rightSelectorTexture);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    private enum MovementType
    {
        LEFT_ONLY((s, x) -> {
            final float currentLeftSelectorOffset = s.getLeftSelectorOffset();
            final float currentRightSelectorOffset = s.getRightSelectorOffset();

            float selectorWidth = currentRightSelectorOffset - currentLeftSelectorOffset;
            if (Math.abs(selectorWidth) <= 0.00000001f)
            {
                selectorWidth = 0f;
            }

            return x <= currentLeftSelectorOffset + (selectorWidth / 3);
        }, (s, v) -> v <= s.getRightValue(),
          RangeSelector::setLeftValue),
        RIGHT_ONLY((s, x) -> {
            final float currentLeftSelectorOffset = s.getLeftSelectorOffset();
            final float currentRightSelectorOffset = s.getRightSelectorOffset();

            float selectorWidth = currentRightSelectorOffset - currentLeftSelectorOffset;
            if (Math.abs(selectorWidth) <= 0.00000001f)
            {
                selectorWidth = 0f;
            }

            return x >= currentRightSelectorOffset - (selectorWidth / 3);
        }, (s, v) -> v >= s.getLeftValue(),
          RangeSelector::setRightValue),
        BOTH((s, x) -> !LEFT_ONLY.ShouldSelect(s, x) && !RIGHT_ONLY.ShouldSelect(s, x),
          (s, v) -> {
              final float lastValue = s.convertXPositionToValue(s.lastClickX);
              final float delta = v - lastValue;

              return s.getLeftValue() + delta >= 0f && s.getRightValue() + delta <= 1f;
          },
          (s, v) -> {
              final float lastValue = s.convertXPositionToValue(s.lastClickX);
              final float delta = v - lastValue;

              s.setLeftValue(s.getLeftValue() + delta);
              s.setRightValue(s.getRightValue() + delta);
          });

        private final BiFunction<RangeSelector, Integer, Boolean> shouldSelectCallback;
        private final BiFunction<RangeSelector, Float, Boolean>   isValidCallback;
        private final BiConsumer<RangeSelector, Float>            setterCallback;

        MovementType(
          final BiFunction<RangeSelector, Integer, Boolean> shouldSelectCallback,
          final BiFunction<RangeSelector, Float, Boolean> isValidCallback,
          final BiConsumer<RangeSelector, Float> setterCallback)
        {
            this.shouldSelectCallback = shouldSelectCallback;
            this.isValidCallback = isValidCallback;
            this.setterCallback = setterCallback;
        }

        public boolean ShouldSelect(RangeSelector s, int x)
        {
            return this.shouldSelectCallback.apply(s, x);
        }

        public boolean IsValid(RangeSelector s, float v)
        {
            return this.isValidCallback.apply(s, v);
        }

        public void set(RangeSelector s, float v)
        {
            this.setterCallback.accept(s, v);
        }
    }

    private static class RangeChangedEventArgs
    {

        private final float previousLeftValue;
        private final float previousRightValue;
        private final float currentLeftValue;
        private final float currentRightValue;

        private RangeChangedEventArgs(final float previousLeftValue, final float previousRightValue, final float currentLeftValue, final float currentRightValue)
        {
            this.previousLeftValue = previousLeftValue;
            this.previousRightValue = previousRightValue;
            this.currentLeftValue = currentLeftValue;
            this.currentRightValue = currentRightValue;
        }

        public float getPreviousLeftValue()
        {
            return previousLeftValue;
        }

        public float getPreviousRightValue()
        {
            return previousRightValue;
        }

        public float getCurrentLeftValue()
        {
            return currentLeftValue;
        }

        public float getCurrentRightValue()
        {
            return currentRightValue;
        }
    }
}
