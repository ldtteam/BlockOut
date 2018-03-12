package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.render.core.IRenderingController;
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
    private IDependencyObject<Object, ResourceLocation> icon;
    @NotNull
    private IDependencyObject<Object, Vector2d>         imageSize;

    public Image(@NotNull final String id, @NotNull final ResourceLocation icon)
    {
        super(id);
        setIcon(icon);
    }

    public Image(
      @NotNull final String id,
      @NotNull final EnumSet<Alignment> alignments,
      @NotNull final Dock dock,
      @NotNull final AxisDistance margin,
      @NotNull final Vector2d elementSize,
      @NotNull final IUIElementHost parent,
      final boolean visible,
      final boolean enabled,
      @NotNull final ResourceLocation icon)
    {
        super(id, alignments, dock, margin, elementSize, parent, visible, enabled);
        setIcon(icon);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        GlStateManager.pushMatrix();
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
        this.imageSize = DependencyObjectHelper.createFromValue(BlockOut.getBlockOut().getProxy().getImageSize(icon));
    }

    @NotNull
    public Vector2d getImageSize()
    {
        return imageSize.get(getDataContext());
    }
}
