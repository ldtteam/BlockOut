package com.minecolonies.blockout.element.root;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class RootGuiElement extends AbstractChildrenContainingUIElement
{
    public RootGuiElement(
      @NotNull final ResourceLocation type,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        super(type, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);
    }

    public RootGuiElement(
      @NotNull final ResourceLocation type,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IUIManager uiManager)
    {
        super(type, id, parent, uiManager);
    }

    public class Factory implements IUIElementFactory<RootGuiElement>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return null;
        }

        @NotNull
        @Override
        public RootGuiElement readFromElementData(@NotNull final IUIElementData elementData)
        {
            return null;
        }

        @Override
        public void writeToElementData(@NotNull final RootGuiElement element, @NotNull final IUIElementDataBuilder builder)
        {

        }
    }
}
