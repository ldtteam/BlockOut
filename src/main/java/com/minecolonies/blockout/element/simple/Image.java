package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.Image.*;

public class Image extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> icon;
    @NotNull
    private IDependencyObject<BoundingBox>      imageData;

    public Image(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> icon,
      @NotNull final IDependencyObject<BoundingBox> imageData)
    {
        super(KEY_IMAGE, id, parent);
        this.icon = icon;
        this.imageData = imageData;
    }

    public Image(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> icon,
      @NotNull final IDependencyObject<BoundingBox> imageData)
    {
        super(KEY_IMAGE, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.icon = icon;
        this.imageData = imageData;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final Vector2d scalingFactor = getScalingFactor();

        GlStateManager.pushMatrix();
        GlStateManager.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);

        controller.bindTexture(getIcon());
        controller.drawTexturedModalRect(getLocalBoundingBox().getLocalOrigin(),
          getLocalBoundingBox().getSize(),
          getImageData().getLocalOrigin(),
          getImageData().getSize(),
          getImageSize());

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {
        //Noop
    }

    @NotNull
    public ResourceLocation getIcon()
    {
        return icon.get(getDataContext());
    }

    public void setIcon(@NotNull final ResourceLocation icon)
    {
        this.icon = DependencyObjectHelper.createFromValue(icon);
    }

    @NotNull
    public Vector2d getImageSize()
    {
        return BlockOut.getBlockOut().getProxy().getImageSize(getIcon());
    }

    @NotNull
    public Vector2d getScalingFactor()
    {
        final Vector2d imageSize = getImageData().getSize();
        final Vector2d elementSize = getElementSize();

        return new Vector2d(imageSize.getX() / elementSize.getX(), imageSize.getY() / elementSize.getY());
    }

    @NotNull
    public BoundingBox getImageData()
    {
        return imageData.get(getDataContext());
    }

    public void setImageData(@NotNull final BoundingBox box)
    {
        this.imageData = DependencyObjectHelper.createFromValue(box);
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
            final String id = elementData.getStringAttribute(CONST_ID);
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDatacontext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> icon = elementData.getBoundResourceLocationAttribute(CONST_ICON);
            final IDependencyObject<BoundingBox> imageData = elementData.getBoundBoundingBoxAttribute(CONST_IMAGE_DATA);

            return new Image(id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              dataContext,
              visible,
              enabled,
              icon,
              imageData);
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
              .addResourceLocation(CONST_ICON, element.getIcon())
              .addBoundingBox(CONST_IMAGE_DATA, element.getImageData());
        }
    }
}
