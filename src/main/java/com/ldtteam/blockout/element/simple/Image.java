package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.Image.CONST_ICON;
import static com.ldtteam.blockout.util.Constants.Controls.Image.KEY_IMAGE;

public class Image extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> iconResource;

    public Image(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> iconResource)
    {
        super(KEY_IMAGE, style, id, parent);
        this.iconResource = iconResource;
    }

    public Image(
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
        super(KEY_IMAGE, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
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
        final ImageResource resource = getIcon();

        GlStateManager.pushMatrix();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.color(1,1,1);

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          getLocalBoundingBox().getSize(),
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
        //Noop
    }

    @NotNull
    public ImageResource getIcon()
    {
        return getResource(getIconResource());
    }

    @NotNull
    public ResourceLocation getIconResource()
    {
        return iconResource.get(this);
    }

    public void setIconResource(@NotNull final ResourceLocation icon)
    {
        this.iconResource.set(this, icon);
    }

    public static class ImageConstructionDataBuilder extends AbstractSimpleUIElement.SimpleControlConstructionDataBuilder<ImageConstructionDataBuilder, Image>
    {

        protected ImageConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, Image.class);
        }

        @NotNull
        public ImageConstructionDataBuilder withDependentIconResource(@NotNull final IDependencyObject<ResourceLocation> iconResource)
        {
            return withDependency("iconResource", iconResource);
        }

        @NotNull
        public ImageConstructionDataBuilder withIconResource(@NotNull final ResourceLocation iconResource)
        {
            return withDependency("iconResource", DependencyObjectHelper.createFromValue(iconResource));
        }

    }

    public static class Factory implements IUIElementFactory<Image>
    {
        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_IMAGE;
        }

        @NotNull
        @Override
        public Image readFromElementData(@NotNull final IUIElementData elementData)
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

            return new Image(
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
        public void writeToElementData(@NotNull final Image element, @NotNull final IUIElementDataBuilder builder)
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
