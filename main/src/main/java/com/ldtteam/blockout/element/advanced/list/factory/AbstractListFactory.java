package com.ldtteam.blockout.element.advanced.list.factory;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.advanced.AbstractChildInstantiatingAndLayoutControllableUIElement;
import com.ldtteam.blockout.element.advanced.list.List;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
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

              final IDependencyObject<IIdentifier> scrollBarBackgroundResource = elementData.getFromRawDataWithDefault(CONST_SCROLL_BACKGROUND, engine, IIdentifier.create(MISSING));
              final IDependencyObject<IIdentifier> scrollBarForegroundResource = elementData.getFromRawDataWithDefault(CONST_SCROLL_FOREGROUND, engine, IIdentifier.create(MISSING));
              final IDependencyObject<Boolean> showScrollBar = elementData.getFromRawDataWithDefault(CONST_SHOW_BAR, engine, true);

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
                .addComponent(CONST_SCROLL_BACKGROUND, element.getScrollBarBackgroundResource())
                .addComponent(CONST_SCROLL_FOREGROUND, element.getScrollBarForegroundResource())
                .addComponent(CONST_SHOW_BAR, element.getShowScrollBar());

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
          @NotNull final IDependencyObject<IIdentifier> styleId,
          @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
          @NotNull final IDependencyObject<Dock> dock,
          @NotNull final IDependencyObject<AxisDistance> margin,
          @NotNull final IDependencyObject<AxisDistance> padding,
          @NotNull final IDependencyObject<Vector2d> elementSize,
          @NotNull final IDependencyObject<Object> dataContext,
          @NotNull final IDependencyObject<Boolean> visible,
          @NotNull final IDependencyObject<Boolean> enabled,
          @NotNull final IDependencyObject<IIdentifier> templateResource,
          @NotNull final IDependencyObject<Object> source,
          @NotNull final IDependencyObject<Orientation> orientation,
          @NotNull final IDependencyObject<IIdentifier> scrollBarBackgroundResource,
          @NotNull final IDependencyObject<IIdentifier> scrollBarForegroundResource,
          @NotNull final IDependencyObject<Boolean> showScrollBar,
          @NotNull final boolean dataBoundMode,
          @NotNull final Double scrollOffset
        );
    }
}
