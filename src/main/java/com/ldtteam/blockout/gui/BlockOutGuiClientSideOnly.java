package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.core.element.IUIElementHost;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
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
    private final IGuiKey key;
    /**
     * The X size of the inventory window in pixels.
     */
    public int xSize = 176;
    /**
     * The Y size of the inventory window in pixels.
     */
    public int ySize = 166;
    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    public  int            guiLeft;
    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    public  int            guiTop;
    @NotNull
    private IUIElementHost root;
    /**
     * Used to track if the current slot interactions happen because of drawing or interactions.
     */
    @NotNull
    private boolean isDrawing = false;

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

        //Can be done here since both fore and background methods are called by the super
        this.getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(mouseX, mouseY);

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);

        drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        drawGuiContainerForegroundLayer(mouseX, mouseY);

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
        // Disabled to avoid any functionality like opening the player GUI on e. If we want this behavior we gotta write an if (shouldHandleKey()) which the fields should decide.
        //super.keyTyped(typedChar, keyCode);
        getRoot().getUiManager().getKeyManager().onKeyPressed(typedChar, key);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        getRoot().getUiManager().getClickManager().onMouseClickBegin(mouseX - guiLeft, mouseY - guiTop, MouseButton.getForCode(mouseButton));
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        getRoot().getUiManager().getClickManager().onMouseClickEnd(mouseX - guiLeft, mouseY - guiTop, MouseButton.getForCode(state));
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        getRoot().getUiManager().getClickManager().onMouseClickMove(mouseX - guiLeft, mouseY - guiTop, MouseButton.getForCode(clickedMouseButton), timeSinceLastClick);
    }

    @Override
    public void initGui()
    {
        root.getUiManager().getUpdateManager().updateElement(root);
        this.xSize = (int) root.getLocalBoundingBox().getSize().getX();
        this.ySize = (int) root.getLocalBoundingBox().getSize().getY();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        //root.setMargin(new AxisDistance(guiLeft, guiTop, guiLeft, guiTop));
        root.getUiManager().getUpdateManager().updateElement(root);
    }

    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput() throws IOException
    {
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        int delta = Mouse.getEventDWheel();
        if (delta != 0)
        {
            getRoot().getUiManager().getScrollManager().onMouseWheel(x - guiLeft, y - guiTop, delta);
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
