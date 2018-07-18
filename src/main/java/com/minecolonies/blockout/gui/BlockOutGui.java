package com.minecolonies.blockout.gui;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class BlockOutGui extends GuiContainer
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private       IUIElementHost root;

    public BlockOutGui(final BlockOutContainer inventorySlotsIn)
    {
        super(inventorySlotsIn);
        this.key = inventorySlotsIn.getKey();
        this.root = inventorySlotsIn.getRoot();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    @Override
    protected void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor)
    {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Override
    public void initGui()
    {
        root.getUiManager().getUpdateManager().updateElement(root);
        this.xSize = (int) root.getLocalBoundingBox().getSize().getX();
        this.ySize = (int) root.getLocalInternalBoundingBox().getSize().getY();
        super.initGui();

        root.setMargin(new AxisDistance(guiLeft, guiTop, guiLeft, guiTop));
        root.getUiManager().getUpdateManager().updateElement(root);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        getRoot().getUiManager().getRenderManager().drawForeground(getRoot());
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        getRoot().getUiManager().getRenderManager().drawBackground(getRoot());
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        getRoot().getUiManager().getNetworkManager().onMouseClickBegin(mouseX - guiLeft, mouseY - guiTop, MouseButton.getForCode(mouseButton));
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        getRoot().getUiManager().getNetworkManager().onMouseClickMove(mouseX - guiLeft, mouseY - guiTop, MouseButton.getForCode(clickedMouseButton), timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        getRoot().getUiManager().getNetworkManager().onMouseClickEnd(mouseX - guiLeft, mouseY - guiTop, MouseButton.getForCode(state));
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        final KeyboardKey key = KeyboardKey.getForCode(keyCode);
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            BlockOut.getBlockOut().getProxy().getGuiController().closeUI(Minecraft.getMinecraft().player);
            return;
        }

        super.keyTyped(typedChar, keyCode);
        getRoot().getUiManager().getNetworkManager().onKeyPressed(typedChar, key);
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
            getRoot().getUiManager().getNetworkManager().onMouseWheel(x - guiLeft, y - guiTop, delta);
        }
        super.handleMouseInput();
    }

    @NotNull
    public IUIElementHost getRoot()
    {
        return root;
    }

    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
    }

    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }
}
