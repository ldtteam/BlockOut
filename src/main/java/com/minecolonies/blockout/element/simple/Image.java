package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
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
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Image extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> icon;

    public Image(
      @NotNull final String id,
      @NotNull final EnumSet<Alignment> alignments,
      @NotNull final Dock dock,
      @NotNull final AxisDistance margin,
      @NotNull final Vector2d elementSize,
      @NotNull final IUIElementHost parent,
      final boolean visible,
      final boolean enabled,
      @NotNull final IDependencyObject<ResourceLocation> icon)
    {
        super(id, alignments, dock, margin, elementSize, parent, visible, enabled);
        setIcon(icon);
    }

    public void setIcon(@NotNull final IDependencyObject<ResourceLocation> icon)
    {
        this.icon = icon;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(getDrawScale().getX(), getDrawScale().getY(), 1f);

        controller.bindTexture(getIcon());
        controller.drawTexturedModalRect(getLocalBoundingBox(), getImageSize());

        GlStateManager.popMatrix();
    }

    @NotNull
    public Vector2d getImageSize()
    {
        return BlockOut.getBlockOut().getProxy().getImageSize(getIcon());
    }

    @NotNull
    Vector2d getDrawScale()
    {
        final double xScale = getImageSize().getX() / getElementSize().getX();
        final double yScale = getImageSize().getY() / getElementSize().getY();

        return new Vector2d(xScale, yScale);
    }

    public class Factory implements IUIElementFactory<Image>
    {

        @NotNull
        @Override
        public Image readFromElementData(@NotNull final IUIElementData elementData)
        {
            final String id = elementData.getStringAttribute(Constants.Controls.General.CONST_ID);
            final EnumSet<Alignment> alignments = elementData.getAlignmentAttribute(Constants.Controls.General.CONST_ALLIGNMENT);
            final Dock dock = elementData.getDockAttribute(Constants.Controls.General.CONST_DOCK, Dock.NONE);
            final AxisDistance margin = elementData.getAxisDistanceAttribute(Constants.Controls.General.CONST_MARGIN);
            final Vector2d elementSize = elementData.getSizePairAttribute()
        }

        @Override
        public void writeToElementData(@NotNull final Image element, @NotNull final IUIElementDataBuilder builder)
        {

        }
    }
}
