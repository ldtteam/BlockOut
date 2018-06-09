package com.minecolonies.blockout.gui;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.mouse.MouseButton;
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

    @Override
    public void initGui()
    {
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
        getRoot().getUiManager().getNetworkManager().onMouseClickBegin(mouseX, mouseY, MouseButton.getForCode(mouseButton));
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        getRoot().getUiManager().getNetworkManager().onMouseClickMove(mouseX, mouseY, MouseButton.getForCode(clickedMouseButton), timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        getRoot().getUiManager().getNetworkManager().onMouseClickEnd(mouseX, mouseY, MouseButton.getForCode(state));
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        getRoot().getUiManager().getNetworkManager().onKeyPressed(typedChar, KeyboardKey.getForCode(keyCode));
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
            getRoot().getUiManager().getNetworkManager().onMouseWheel(x, y, delta);
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
