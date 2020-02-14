package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractSimpleUIElement;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.Image.CONST_ICON;
import static com.ldtteam.blockout.util.Constants.Controls.Image.KEY_IMAGE;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class Image extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    public IDependencyObject<ResourceLocation> iconResource;

    public Image(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> iconResource)
    {
        super(KEY_IMAGE, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
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
        final ImageResource resource = getIcon();

        RenderSystem.pushMatrix();

        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        Color.resetOpenGLColoring();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          getLocalBoundingBox().getSize(),
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();

        RenderSystem.popMatrix();
    }

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

    public static class Factory extends AbstractSimpleUIElementFactory<Image>
    {
        public Factory()
        {
            super(Image.class, KEY_IMAGE, (elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<ResourceLocation> icon = elementData.getFromRawDataWithDefault(CONST_ICON, engine, new ResourceLocation(MISSING), ResourceLocation.class);

                final Image element = new Image(
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
                  icon);

                return element;
            }, (element, builder) -> builder.addComponent(CONST_ICON, element.getIconResource(), ResourceLocation.class));
        }
    }
}
