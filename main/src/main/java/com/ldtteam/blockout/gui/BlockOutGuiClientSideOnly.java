package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class BlockOutGuiClientSideOnly extends GuiScreen implements IBlockOutGui
{
    @NotNull
    private final IGuiKey        key;
    /**
     * The X size of the inventory window in pixels.
     */
    public        int            xSize     = 176;
    /**
     * The Y size of the inventory window in pixels.
     */
    public        int            ySize     = 166;
    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    public        int            guiLeft;
    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    public        int            guiTop;

    /**
     * Controls the scaling if the UI is bigger then the specified size.
     */
    @NotNull
    private Vector2d scaleFactor = new Vector2d(1, 1);

    @NotNull
    private       IUIElementHost root;
    /**
     * Used to track if the current slot interactions happen because of drawing or interactions.
     */
    @NotNull
    private       boolean        isDrawing = false;

    public BlockOutGuiClientSideOnly(@NotNull final IGuiKey key, @NotNull final IUIElementHost root)
    {
        this.key = key;
        this.root = root;
        this.root.getUiManager().getRenderManager().setGui(this);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        this.isDrawing = true;

        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        //Can be done here since both fore and background methods are called by the super
        this.getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(scaledMouseX, scaledMouseY);

        GlStateManager.pushMatrix();

        GlStateManager.scale(1 / this.scaleFactor.getX(), 1 / this.scaleFactor.getY(), 1f);

        GlStateManager.pushMatrix();

        GlStateManager.pushMatrix();

        GlStateManager.translate(guiLeft * this.scaleFactor.getX(), guiTop * this.scaleFactor.getY(), 0);

        drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        drawGuiContainerForegroundLayer(mouseX, mouseY);

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        this.isDrawing = false;
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode)
    {
        final KeyboardKey key = KeyboardKey.getForCode(keyCode);
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            BlockOut.getBlockOut().getProxy().getClientSideOnlyGuiController().closeUI(Minecraft.getMinecraft().player);
            return;
        }

        if (!getRoot().getUiManager().getClientSideKeyManager().onKeyPressed(typedChar, key))
        {
            getRoot().getUiManager().getKeyManager().onKeyPressed(typedChar, key);
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
    {
        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        super.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
        if (!getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickBegin((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), MouseButton.getForCode(mouseButton)))
        {
            getRoot().getUiManager()
              .getClickManager()
              .onMouseClickBegin((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), MouseButton.getForCode(mouseButton));
        }
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        super.mouseReleased(scaledMouseX, scaledMouseY, state);

        if (!getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickEnd((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), MouseButton.getForCode(state)))
        {
            getRoot().getUiManager()
              .getClickManager()
              .onMouseClickEnd((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), MouseButton.getForCode(state));
        }
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick)
    {
        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        super.mouseClickMove(scaledMouseX, scaledMouseY, clickedMouseButton, timeSinceLastClick);
        if (!getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickMove((int) (scaledMouseX - (guiLeft * scaleFactor.getX())),
                 (int) (scaledMouseY - (guiTop * scaleFactor.getY())),
                 MouseButton.getForCode(clickedMouseButton),
                 timeSinceLastClick))
        {
            getRoot().getUiManager()
              .getClickManager()
              .onMouseClickMove((int) (scaledMouseX - (guiLeft * scaleFactor.getX())),
                (int) (scaledMouseY - (guiTop * scaleFactor.getY())),
                MouseButton.getForCode(clickedMouseButton),
                timeSinceLastClick);
        }
    }

    @Override
    public void initGui()
    {
        root.getUiManager().getUpdateManager().updateElement(root);
        this.xSize = (int) root.getLocalBoundingBox().getSize().getX();
        this.ySize = (int) root.getLocalBoundingBox().getSize().getY();

        this.scaleFactor = new Vector2d(1, 1);

        //Check if we need to scale the gui
        if (this.xSize > this.width || this.ySize > this.height)
        {
            double xScalingFactor = Math.ceil(root.getLocalBoundingBox().getSize().getX() / this.width);
            double yScalingFactor = Math.ceil(root.getLocalBoundingBox().getSize().getY() / this.height);

            //Equalise the scaling.
            xScalingFactor = Math.max(xScalingFactor, yScalingFactor);
            yScalingFactor = xScalingFactor;

            this.scaleFactor = new Vector2d(xScalingFactor, yScalingFactor);

            this.xSize = (int) (root.getLocalBoundingBox().getSize().getX() / xScalingFactor);
            this.ySize = (int) (root.getLocalBoundingBox().getSize().getY() / yScalingFactor);
        }

        root.getUiManager().getRenderManager().setRenderingScalingFactor(scaleFactor);

        super.initGui();

        //Update the margins to be scaled now.
        int scaledGuiLeft = (int) (guiLeft * this.scaleFactor.getX());
        int scaledGuiTop = (int) (guiTop * this.scaleFactor.getY());

        root.setMargin(new AxisDistance(scaledGuiLeft, scaledGuiTop, scaledGuiLeft, scaledGuiTop));
        root.getUiManager().getUpdateManager().updateElement(root);

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }


    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput() throws IOException
    {
        int scaledMouseX = (int) ((Mouse.getEventX() * this.width / this.mc.displayWidth) * scaleFactor.getX());
        int scaledMouseY = (int) ((this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1) * scaleFactor.getY());

        int delta = Mouse.getEventDWheel();
        if (delta != 0)
        {
            if (!getRoot().getUiManager()
                   .getClientSideScrollManager()
                   .onMouseWheel((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), delta))
            {
                getRoot().getUiManager()
                  .getScrollManager()
                  .onMouseWheel((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), delta);
            }
        }

        super.handleMouseInput();
    }

    @Override
    @NotNull
    public IUIElementHost getRoot()
    {
        return root;
    }

    private void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        getRoot().getUiManager().getRenderManager().drawBackground(getRoot());
    }

    private void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        getRoot().getUiManager().getRenderManager().drawForeground(getRoot());
    }

    @Override
    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
        this.root.getUiManager().getRenderManager().setGui(this);
        this.root.update(this.root.getUiManager().getUpdateManager());
        this.initGui();
    }

    @Override
    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }
}
