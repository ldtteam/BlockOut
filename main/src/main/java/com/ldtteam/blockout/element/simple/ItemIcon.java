package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ItemStackResource;
import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.ItemIcon.CONST_ICON;
import static com.ldtteam.blockout.util.Constants.Controls.ItemIcon.KEY_ITEM;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class ItemIcon extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    public IDependencyObject<IIdentifier> iconResource;

    public ItemIcon(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<IIdentifier> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<IIdentifier> iconResource)
    {
        super(KEY_ITEM, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.iconResource = iconResource;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (iconResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final ItemStackResource resource = getIcon();
        final IItemStack stack = resource.getStack();
        if (stack != null && !stack.isEmpty())
        {
            IOpenGl.pushMatrix();
            final Vector2d scalingFactor = resource.getScalingFactor(getLocalBoundingBox().getSize());
            IOpenGl.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);

            controller.drawItemStack(stack, 0, 0);

            IOpenGl.popMatrix();
        }
    }

    @NotNull
    public ItemStackResource getIcon()
    {
        return getResource(getIconResource());
    }

    @NotNull
    public IIdentifier getIconResource()
    {
        return iconResource.get(this);
    }

    public void setIconResource(@NotNull final IIdentifier icon)
    {
        this.iconResource.set(this, icon);
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
        //Noop
    }

    public static class ItemIconConstructionDataBuilder extends SimpleControlConstructionDataBuilder<ItemIconConstructionDataBuilder, ItemIcon>
    {

        protected ItemIconConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, ItemIcon.class);
        }

        @NotNull
        public ItemIconConstructionDataBuilder withDependentIconResource(@NotNull final IDependencyObject<ItemStackResource> iconResource)
        {
            return withDependency("iconResource", iconResource);
        }

        @NotNull
        public ItemIconConstructionDataBuilder withIconResource(@NotNull final ItemStackResource iconResource)
        {
            return withDependency("iconResource", DependencyObjectHelper.createFromValue(iconResource));
        }
    }

    public static class Factory extends AbstractSimpleUIElementFactory<ItemIcon>
    {
        public Factory()
        {
            super(ItemIcon.class, KEY_ITEM, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<IIdentifier> icon = elementData.getFromRawDataWithDefault(CONST_ICON, engine, IIdentifier.create(MISSING));

                final ItemIcon element = new ItemIcon(
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
                  icon
                );

                return element;
            }, (element, builder) -> builder.addComponent(CONST_ICON, element.getIconResource()));
        }
    }
}
