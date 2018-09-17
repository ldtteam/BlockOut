package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.Region.KEY_REGION;

public class Region extends AbstractChildrenContainingUIElement
{
    public Region(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        super(KEY_REGION, style, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
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

    public static class Factory implements IUIElementFactory<Region>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_REGION;
        }

        @NotNull
        @Override
        public Region readFromElementData(@NotNull final IUIElementData elementData)
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

            final Region region = new Region(
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
              enabled);

            elementData.getChildren(region).forEach(childData -> {
                IUIElement child = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(childData);
                region.put(child.getId(), child);
            });

            return region;
        }

        @Override
        public void writeToElementData(@NotNull final Region element, @NotNull final IUIElementDataBuilder builder)
        {

            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addAxisDistance(CONST_PADDING, element.getPadding())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled());

            element.values().forEach(child -> {
                builder.addChild(BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(child));
            });
        }
    }
}
