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
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.Image.CONST_ICON;
import static com.minecolonies.blockout.util.Constants.Controls.Image.KEY_ICON;

public class Image extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> icon;

    public Image(
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> icon)
    {
        super(id, parent);
        this.icon = icon;
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
      @NotNull final IDependencyObject<ResourceLocation> icon)
    {
        super(id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.icon = icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final Vector2d scalingFactor = getScalingFactor();

        GlStateManager.pushMatrix();
        GlStateManager.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);

        controller.bindTexture(getIcon());
        controller.drawTexturedModalRect(getLocalBoundingBox(), getImageSize());

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
        final Vector2d imageSize = getImageSize();
        final Vector2d elementSize = getElementSize();

        return new Vector2d(imageSize.getX() / elementSize.getX(), imageSize.getY() / elementSize.getY());
    }

    public class Factory implements IUIElementFactory<Image>
    {
        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_ICON;
        }

        @NotNull
        @Override
        public Image readFromElementData(@NotNull final IUIElementData elementData)
        {
            final String id = elementData.getStringAttribute(CONST_ID);
            final EnumSet<Alignment> alignments = elementData.getAlignmentAttribute(CONST_ALIGNMENT);
            final Dock dock = elementData.getEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final AxisDistance margin = elementData.getAxisDistanceAttribute(CONST_MARGIN);
            final Vector2d elementSize = elementData.getVector2dAttribute(CONST_ELEMENT_SIZE);
            final Object dataContext = new Object();
            final Boolean visible = elementData.getBooleanAttribute(CONST_VISIBLE);
            final Boolean enabled = elementData.getBooleanAttribute(CONST_ENABLED);
            final ResourceLocation icon = elementData.getResourceLocationAttribute(CONST_ICON);

            return new Image(id,
              elementData.getParentView(),
              DependencyObjectHelper.createFromValue(alignments),
              DependencyObjectHelper.createFromValue(dock),
              DependencyObjectHelper.createFromValue(margin),
              DependencyObjectHelper.createFromValue(elementSize),
              DependencyObjectHelper.createFromValue(dataContext),
              DependencyObjectHelper.createFromValue(visible),
              DependencyObjectHelper.createFromValue(enabled),
              DependencyObjectHelper.createFromValue(icon));
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
              .addResourceLocation(CONST_ICON, element.getIcon());
        }
    }
}
