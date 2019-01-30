package com.ldtteam.blockout.management.client.render;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IChildDrawableUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.gui.IBlockOutGui;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.standard.RenderingController;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderManager implements IRenderManager
{
    @SideOnly(Side.CLIENT)
    private final IRenderingController renderingController = new RenderingController(this);

    @SideOnly(Side.CLIENT)
    private IBlockOutGui gui;

    @SideOnly(Side.CLIENT)
    private Vector2d scalingFactor;

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IUIElement host)
    {
        if (!host.isVisible())
        {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(host.getAbsoluteBoundingBox().getLocalOrigin().getX(), host.getAbsoluteBoundingBox().getLocalOrigin().getY(), 0);

        if (host instanceof IDrawableUIElement)
        {
            IDrawableUIElement iDrawableUIElement = (IDrawableUIElement) host;
            iDrawableUIElement.drawBackground(renderingController);
        }

        GlStateManager.popMatrix();

        if (host instanceof IChildDrawableUIElement)
        {
            ((IChildDrawableUIElement) host).preBackgroundDrawOfChildren(this);
            ((IChildDrawableUIElement) host).backgroundDrawOfChildren(this);
            ((IChildDrawableUIElement) host).postBackgroundDrawOfChildren(this);
        }
        else if (host instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) host;
            iuiElementHost.values().forEach(this::drawBackground);
        }
    }

    @Override
    public void drawForeground(@NotNull final IUIElement host)
    {
        if (!host.isVisible())
        {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(host.getAbsoluteBoundingBox().getLocalOrigin().getX(), host.getAbsoluteBoundingBox().getLocalOrigin().getY(), 0);

        if (host instanceof IDrawableUIElement)
        {
            IDrawableUIElement iDrawableUIElement = (IDrawableUIElement) host;
            iDrawableUIElement.drawForeground(renderingController);
        }

        GlStateManager.popMatrix();

        if (host instanceof IChildDrawableUIElement)
        {
            ((IChildDrawableUIElement) host).preForegroundDrawOfChildren(this);
            ((IChildDrawableUIElement) host).foregroundDrawOfChildren(this);
            ((IChildDrawableUIElement) host).postForegroundDrawOfChildren(this);
        }
        else if (host instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) host;
            iuiElementHost.values().forEach(this::drawForeground);
        }
    }

    @NotNull
    @Override
    public IRenderingController getRenderingController()
    {
        return renderingController;
    }

    @NotNull
    @Override
    public IBlockOutGui getGui()
    {
        return gui;
    }

    @Override
    public void setGui(@NotNull final IBlockOutGui gui)
    {
        this.gui = gui;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vector2d getRenderingScalingFactor()
    {
        return scalingFactor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setRenderingScalingFactor(@NotNull final Vector2d scalingFactor)
    {
        this.scalingFactor = scalingFactor;
    }
}
