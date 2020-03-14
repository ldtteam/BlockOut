package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.Region.KEY_REGION;

public class Region extends AbstractChildrenContainingUIElement
{
    private static final long serialVersionUID = 1811576092491061178L;

    public Region(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        super(KEY_REGION, styleId, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
    }

    public Region(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent)
    {
        super(KEY_REGION, style, id, parent);
    }

    public static class RegionConstructionDataBuilder extends AbstractChildrenContainingUIElement.SimpleControlConstructionDataBuilder<RegionConstructionDataBuilder, Region>
    {

        public RegionConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, Region.class);
        }
    }

    public static class Factory extends AbstractChildrenContainingUIElementFactory<Region>
    {

        public Factory()
        {
            super(Region.class, KEY_REGION, (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> {
                final Region element = new Region(
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
                  enabled
                );

                return element;
            }, (element, builder) -> {
                //Noop this is an invisible element that is just here to group other elements. It has no other properties.
            });
        }
    }
}
