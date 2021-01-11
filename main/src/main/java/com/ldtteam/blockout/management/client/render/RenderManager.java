package com.ldtteam.blockout.management.client.render;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.IUIElementWithTooltip;
import com.ldtteam.blockout.element.drawable.IChildDrawableUIElement;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.gui.BlockOutGuiData;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.standard.RenderingController;
import com.ldtteam.blockout.tooltip.ITooltipHost;
import com.ldtteam.blockout.util.math.Vector2d;
import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.NotNull;

public class RenderManager implements IRenderManager
{
    private final IRenderingController renderingController = new RenderingController(this);

    private BlockOutGuiData gui;

    private Vector2d scalingFactor;

    @Override
    public void drawBackground(@NotNull final IUIElement host)
    {
        if (!host.isVisible())
        {
            return;
        }

        RenderSystem.pushMatrix();
        RenderSystem.translated(host.getAbsoluteBoundingBox().getLocalOrigin().getX(), host.getAbsoluteBoundingBox().getLocalOrigin().getY(), 0);

        if (host instanceof IDrawableUIElement)
        {
            IDrawableUIElement iDrawableUIElement = (IDrawableUIElement) host;
            iDrawableUIElement.drawBackground(renderingController);
        }

        RenderSystem.popMatrix();

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

        RenderSystem.pushMatrix();
        RenderSystem.translated(host.getAbsoluteBoundingBox().getLocalOrigin().getX(), host.getAbsoluteBoundingBox().getLocalOrigin().getY(), 0);

        if (host instanceof IDrawableUIElement)
        {
            IDrawableUIElement iDrawableUIElement = (IDrawableUIElement) host;
            iDrawableUIElement.drawForeground(renderingController);
        }

        RenderSystem.popMatrix();

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

    @Override
    public boolean drawTooltip(@NotNull final IUIElement host, final int absoluteMouseX, final int absoluteMouseY) {
        boolean childDrew = false;
        if (host instanceof IUIElementHost)
        {
            childDrew = ((IUIElementHost) host).values().stream().anyMatch(u -> drawTooltip(u, absoluteMouseX, absoluteMouseY));
        }

        if (childDrew)
            return true;

        if (host instanceof IUIElementWithTooltip)
        {
            final IUIElementWithTooltip iuiElementWithTooltip = (IUIElementWithTooltip) host;
            final ITooltipHost tooltipHost = iuiElementWithTooltip.getTooltipHost();
            if (!tooltipHost.shouldDisplayTooltip(absoluteMouseX, absoluteMouseY))
                return false;

            final Vector2d mousePosition = new Vector2d(absoluteMouseX, absoluteMouseY);

            RenderSystem.pushMatrix();
            RenderSystem.translated(mousePosition.getX(), mousePosition.getY(), 0);

            this.drawBackground(tooltipHost);
            this.drawForeground(tooltipHost);

            RenderSystem.popMatrix();

            return true;
        }

        return false;
    }

    @NotNull
    @Override
    public IRenderingController getRenderingController()
    {
        return renderingController;
    }

    @NotNull
    @Override
    public BlockOutGuiData getGuiData()
    {
        return gui;
    }

    @Override
    public void setGuiData(@NotNull final BlockOutGuiData gui)
    {
        this.gui = gui;
    }

    @Override
    public Vector2d getRenderingScalingFactor()
    {
        return scalingFactor;
    }

    @Override
    public void setRenderingScalingFactor(@NotNull final Vector2d scalingFactor)
    {
        this.scalingFactor = scalingFactor;
    }
}
