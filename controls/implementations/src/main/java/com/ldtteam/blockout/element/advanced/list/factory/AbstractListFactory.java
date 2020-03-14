package com.ldtteam.blockout.element.advanced.list.factory;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractChildInstantiatingAndLayoutControllableUIElement;
import com.ldtteam.blockout.element.advanced.list.List;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.List.*;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class AbstractListFactory<U extends List> extends AbstractChildInstantiatingAndLayoutControllableUIElement.AbstractChildInstantiatingAndLayoutControllableUIElementFactory<U>
{
    public AbstractListFactory(
      @NotNull final Class<U> clz,
      @NotNull final String key,
      @NotNull final IListConstructor<U> constructor,
      @NotNull final ISimpleUIElementWriter<U> writer
    )
    {
        super(clz,
          key,
          (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled, templateResource, source, orientation, dataBoundMode, scrollOffset) -> {

              final IDependencyObject<ResourceLocation> scrollBarBackgroundResource =
                elementData.getFromRawDataWithDefault(CONST_SCROLL_BACKGROUND, engine, new ResourceLocation(MISSING), ResourceLocation.class);
              final IDependencyObject<ResourceLocation> scrollBarForegroundResource =
                elementData.getFromRawDataWithDefault(CONST_SCROLL_FOREGROUND, engine, new ResourceLocation(MISSING), ResourceLocation.class);
              final IDependencyObject<Boolean> showScrollBar = elementData.getFromRawDataWithDefault(CONST_SHOW_BAR, engine, true, Boolean.class);

              return constructor.constructUsing(
                elementData,
                engine,
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
                templateResource,
                source,
                orientation,
                scrollBarBackgroundResource,
                scrollBarForegroundResource,
                showScrollBar,
                dataBoundMode,
                scrollOffset
              );
          },
          (element, builder) -> {
              builder
                .addComponent(CONST_SCROLL_BACKGROUND, element.getScrollBarBackgroundResource(), ResourceLocation.class)
                .addComponent(CONST_SCROLL_FOREGROUND, element.getScrollBarForegroundResource(), ResourceLocation.class)
                .addComponent(CONST_SHOW_BAR, element.getShowScrollBar(), Boolean.class);

              writer.write(element, builder);
          });
    }

    @FunctionalInterface
    public interface IListConstructor<U extends List>
    {
        U constructUsing(
          @NotNull final IUIElementData<?> elementData,
          @NotNull final IBindingEngine engine,
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
          @NotNull final IDependencyObject<Boolean> enabled,
          @NotNull final IDependencyObject<ResourceLocation> templateResource,
          @NotNull final IDependencyObject<Object> source,
          @NotNull final IDependencyObject<Orientation> orientation,
          @NotNull final IDependencyObject<ResourceLocation> scrollBarBackgroundResource,
          @NotNull final IDependencyObject<ResourceLocation> scrollBarForegroundResource,
          @NotNull final IDependencyObject<Boolean> showScrollBar,
          @NotNull final boolean dataBoundMode,
          @NotNull final Double scrollOffset
        );
    }
}
