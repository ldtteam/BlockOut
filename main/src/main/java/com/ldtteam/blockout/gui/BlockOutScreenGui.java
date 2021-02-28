package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class BlockOutScreenGui extends Screen implements IBlockOutGui
{
    private final BlockOutGuiData guiData;

    public BlockOutScreenGui(final IGuiKey key, final IUIElementHost host)
    {
        super(new StringTextComponent(""));

        this.guiData = new BlockOutGuiData(key, host);
    }

    @Override
    public BlockOutGuiData getInstanceData()
    {
        return guiData;
    }

    @Override
    protected void init()
    {
        getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(getInstanceData().getRoot());
        getInstanceData().setXSize((int) getInstanceData().getRoot().getLocalBoundingBox().getSize().getX());
        getInstanceData().setYSize((int) getInstanceData().getRoot().getLocalBoundingBox().getSize().getY());

        getInstanceData().setScaleFactor(new Vector2d(1, 1));

        //Check if we need to scale the guitemp
        if (getInstanceData().getXSize() > width || getInstanceData().getYSize() > height)
        {
            double xScalingFactor = Math.ceil(getInstanceData().getRoot().getLocalBoundingBox().getSize().getX() / width);
            double yScalingFactor = Math.ceil(getInstanceData().getRoot().getLocalBoundingBox().getSize().getY() / height);

            //Equalise the scaling.
            xScalingFactor = Math.max(xScalingFactor, yScalingFactor);
            yScalingFactor = xScalingFactor;

            getInstanceData().setScaleFactor(new Vector2d(xScalingFactor, yScalingFactor));
            getInstanceData().setXSize((int) (getInstanceData().getRoot().getLocalBoundingBox().getSize().getX() / xScalingFactor));
            getInstanceData().setYSize((int) (getInstanceData().getRoot().getLocalBoundingBox().getSize().getY() / xScalingFactor));
        }

        getInstanceData().getRoot().getUiManager().getRenderManager().setRenderingScalingFactor(getInstanceData().getScaleFactor());

        super.init();
        getInstanceData().setGuiOffset((height - getInstanceData().getYSize()) / 2, (width - getInstanceData().getXSize()) / 2);

        //Update the margins to be scaled now.
        int scaledGuiLeft = (int) (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX());
        int scaledGuiTop = (int) (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY());

        getInstanceData().getRoot().setMargin(new AxisDistance(scaledGuiLeft, scaledGuiTop, scaledGuiLeft, scaledGuiTop));
        getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(getInstanceData().getRoot());
    }

    @Override
    public void render(final MatrixStack matrixStack, int mouseX, final int mouseY, final float partialTickTime)
    {
        getInstanceData().setDrawing(true);

        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        //Can be done here since both fore and background methods are called by the super
        getInstanceData().getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(scaledMouseX, scaledMouseY);

        RenderSystem.pushMatrix();

        RenderSystem.scaled(1 / getInstanceData().getScaleFactor().getX(), 1 / getInstanceData().getScaleFactor().getY(), 1d);

        RenderSystem.pushMatrix();

        getInstanceData().getRoot().getUiManager().getRenderManager().drawBackground(getInstanceData().getRoot());

        RenderSystem.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();

        super.render(matrixStack,mouseX, mouseY, partialTickTime);

        RenderHelper.enableStandardItemLighting();
        RenderSystem.pushMatrix();
        RenderSystem.translated((float)getInstanceData().getGuiLeft(), (float)getInstanceData().getGuiTop(), 0.0d);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableRescaleNormal();

        RenderHelper.disableStandardItemLighting();

        getInstanceData().getRoot().getUiManager().getRenderManager().drawForeground(getInstanceData().getRoot());
        getInstanceData().getRoot().getUiManager().getRenderManager().drawTooltip(getInstanceData().getRoot(), scaledMouseX, scaledMouseY);

        RenderSystem.popMatrix();
        RenderSystem.enableLighting();
        RenderSystem.enableDepthTest();
        RenderHelper.enableStandardItemLighting();

        RenderSystem.popMatrix();
        RenderSystem.popMatrix();

        getInstanceData().setDrawing(false);
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double dWheel)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        int delta = (int) dWheel;
        if (delta != 0)
        {
            if (!getInstanceData().getRoot().getUiManager()
                   .getClientSideScrollManager()
                   .onMouseWheel((int) (scaledMouseX - (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY())), delta))
            {
                getInstanceData().getRoot().getUiManager()
                  .getNetworkManager()
                  .onMouseWheel((int) (scaledMouseX - (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY())), delta);
            }
        }

        super.mouseScrolled(mouseX, mouseY, dWheel);

        return true;
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        super.mouseClicked(scaledMouseX, scaledMouseY, button);

        if (!getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickBegin(scaledMouseX, scaledMouseY, MouseButton.getForCode(button)))
        {
            getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickBegin((int) (scaledMouseX - (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(button));
        }

        return true;
    }

    @Override
    public boolean mouseDragged(
      final double mouseX,
      final double mouseY,
      final int button,
      final double deltaX,
      final double deltaY)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        super.mouseClicked(scaledMouseX, scaledMouseY, button);

        if (!getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickMove(scaledMouseX, scaledMouseY, MouseButton.getForCode(button), 0f))
        {
            getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickMove((int) (scaledMouseX - (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(button), 0f);
        }

        return true;
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        super.mouseClicked(scaledMouseX, scaledMouseY, button);

        if (!getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(button)))
        {
            getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickEnd((int) (scaledMouseX - (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(button));
        }

        return true;
    }
}
