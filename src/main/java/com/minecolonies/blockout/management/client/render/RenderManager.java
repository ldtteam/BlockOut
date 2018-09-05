package com.minecolonies.blockout.management.client.render;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IChildDrawableUIElement;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.render.standard.RenderingController;
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
    private BlockOutGui gui;

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IUIElement host)
    {
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
    public BlockOutGui getGui()
    {
        return gui;
    }

    @Override
    public void setGui(@NotNull final BlockOutGui gui)
    {
        this.gui = gui;
    }
}
