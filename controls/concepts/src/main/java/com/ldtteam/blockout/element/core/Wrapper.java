package com.ldtteam.blockout.element.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.Wrapper.KEY_WRAPPER;

public class Wrapper extends AbstractChildrenContainingUIElement
{
    private static final long serialVersionUID = 1811576092491061178L;

    Wrapper(
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
        super(KEY_WRAPPER, styleId, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
    }

    public Wrapper(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent)
    {
        super(KEY_WRAPPER, style, id, parent);
    }

    public static class Factory extends AbstractChildrenContainingUIElementFactory<Wrapper>
    {

        public Factory()
        {
            super(Wrapper.class, KEY_WRAPPER, (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> new Wrapper(
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
            ), (element, builder) -> {
                //Noop this is an invisible element that is just here to group other elements. It has no other properties.
            });
        }
    }
}
