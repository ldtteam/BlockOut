package com.ldtteam.blockout.element.root;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.Root.KEY_ROOT;

public class RootGuiElement extends AbstractChildrenContainingUIElement
{

    public static final class RootGuiConstructionDataBuilder extends SimpleControlConstructionDataBuilder<RootGuiConstructionDataBuilder, RootGuiElement>
    {

        public RootGuiConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, RootGuiElement.class);
        }
    }

    @NotNull
    private IUIManager manager;

    public RootGuiElement(
      @NotNull final IDependencyObject<IIdentifier> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        super(KEY_ROOT, styleId, KEY_ROOT.split(":")[1], null, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

        this.setParent(this);
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        return dataContext.get(new Object());
    }

    @NotNull
    @Override
    public IUIManager getUiManager()
    {
        return manager;
    }

    public void setUiManager(@NotNull final IUIManager manager)
    {
        this.manager = manager;
    }

    public static class Factory extends AbstractChildrenContainingUIElementFactory<RootGuiElement>
    {

        public Factory()
        {
            super(RootGuiElement.class,
              KEY_ROOT,
              (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> new RootGuiElement(styleId,
              alignments,
              dock,
              margin,
              padding,
              elementSize,
              dataContext,
              visible,
              enabled), (element, builder) -> {
                //No additional information is stored in here.
            });
        }
    }
}
