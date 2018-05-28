/*
package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ProgressBar extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> backGround;
    @NotNull
    private IDependencyObject<BoundingBox>      backGroundImageData;
    @NotNull
    private IDependencyObject<ResourceLocation> foreGround;
    @NotNull
    private IDependencyObject<BoundingBox>      foreGroundImageData;
    @NotNull
    private IDependencyObject<Double> value;
    @NotNull
    private IDependencyObject<Double> min;
    @NotNull
    private IDependencyObject<Double> max;

    public ProgressBar(@NotNull final String id, @NotNull final IUIElementHost parent)
    {
        super(type, id, parent);
    }

    public ProgressBar(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> backGround,
      @NotNull final IDependencyObject<BoundingBox> backGroundImageData,
      @NotNull final IDependencyObject<ResourceLocation> foreGround,
      @NotNull final IDependencyObject<BoundingBox> foreGroundImageData,
      @NotNull final IDependencyObject<Double> value,
      @NotNull final IDependencyObject<Double> min,
      @NotNull final IDependencyObject<Double> max)
    {
        super(type, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        
        this.backGround = backGround;
        this.backGroundImageData = backGroundImageData;
        this.foreGround = foreGround;
        this.foreGroundImageData = foreGroundImageData;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();

        final

        GlStateManager.popMatrix();
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    @NotNull
    public ResourceLocation getBackGround()
    {
        return backGround.get(getDataContext());
    }

    public void setBackGround(@NotNull final ResourceLocation backGround)
    {
        this.backGround = DependencyObjectHelper.createFromValue(backGround);
    }

    @NotNull
    public BoundingBox getBackGroundImageData()
    {
        return backGroundImageData.get(getDataContext());
    }

    public void setBackGroundImageData(@NotNull final BoundingBox backGroundImageData)
    {
        this.backGroundImageData = DependencyObjectHelper.createFromValue(backGroundImageData);
    }

    @NotNull
    public ResourceLocation getForeGround()
    {
        return foreGround.get(getDataContext());
    }

    public void setForeGround(@NotNull final ResourceLocation foreGround)
    {
        this.foreGround = DependencyObjectHelper.createFromValue(foreGround);
    }

    @NotNull
    public BoundingBox getForeGroundImageData()
    {
        return foreGroundImageData.get(getDataContext());
    }

    public void setForeGroundImageData(@NotNull final BoundingBox foreGroundImageData)
    {
        this.foreGroundImageData = DependencyObjectHelper.createFromValue(foreGroundImageData);
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
}
*/
