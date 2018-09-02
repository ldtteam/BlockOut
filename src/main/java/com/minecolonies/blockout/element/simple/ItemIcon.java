package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.style.resources.ItemStackResource;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.ItemIcon.CONST_ICON;
import static com.minecolonies.blockout.util.Constants.Controls.ItemIcon.KEY_ITEM;

public class ItemIcon extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> iconResource;

    public ItemIcon(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> iconResource)
    {
        super(KEY_ITEM, style, id, parent);
        this.iconResource = iconResource;
    }

    public ItemIcon(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> iconResource)
    {
        super(KEY_ITEM, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final ItemStackResource resource = getIcon();
        final ItemStack stack = resource.getStack();
        if (stack != null && !stack.isEmpty())
        {
            GlStateManager.pushMatrix();
            final Vector2d scalingFactor = resource.getScalingFactor(getLocalBoundingBox().getSize());
            GlStateManager.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);

            controller.drawItemStack(stack, 0, 0);

            GlStateManager.popMatrix();
        }
    }

    @NotNull
    public ItemStackResource getIcon()
    {
        return getResource(getIconResource());
    }

    @NotNull
    public ResourceLocation getIconResource()
    {
        return iconResource.get(getDataContext());
    }

    public void setIconResource(@NotNull final ResourceLocation icon)
    {
        this.iconResource = DependencyObjectHelper.createFromValue(icon);
    }

    @SideOnly(Side.CLIENT)
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

    public static class Factory implements IUIElementFactory<ItemIcon>
    {
        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_ITEM;
        }

        @NotNull
        @Override
        public ItemIcon readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getElementId();
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> icon = elementData.getBoundResourceLocationAttribute(CONST_ICON);

            return new ItemIcon(
              style,
              id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              dataContext,
              visible,
              enabled,
              icon);
        }

        @Override
        public void writeToElementData(@NotNull final ItemIcon element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_ICON, element.getIconResource());
        }
    }
}
