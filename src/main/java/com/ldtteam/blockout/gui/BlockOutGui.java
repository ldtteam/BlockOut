package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.core.element.IUIElementHost;
import com.ldtteam.blockout.core.element.values.AxisDistance;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.inventory.slot.SlotBlockOut;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class BlockOutGui extends GuiContainer implements IBlockOutGui
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private       IUIElementHost root;

    /**
     * Used to track if the current slot interactions happen because of drawing or interactions.
     */
    @NotNull
    private boolean isDrawing = false;

    public BlockOutGui(final BlockOutContainer inventorySlotsIn)
    {
        super(inventorySlotsIn);
        this.key = inventorySlotsIn.getKey();
        this.root = inventorySlotsIn.getRoot();
        this.root.getUiManager().getRenderManager().setGui(this);
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        getRoot().getUiManager().getRenderManager().drawBackground(getRoot());
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        this.isDrawing = true;

        //Can be done here since both fore and background methods are called by the super
        this.getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.isDrawing = false;
    }

    /**
     * Draws the given slot: any item in it, the slot's background, the hovered highlight, etc.
     */
    @Override
    public void drawSlot(final Slot slotIn)
    {
        if (isDrawing && slotIn instanceof SlotBlockOut)
        {
            return;
        }

        super.drawSlot(slotIn);
    }

    /**
     * Returns whether the mouse is over the given slot.
     */
    @Override
    public boolean isMouseOverSlot(final Slot slotIn, final int mouseX, final int mouseY)
    {
        return (!isDrawing || !(slotIn instanceof SlotBlockOut)) && super.isMouseOverSlot(slotIn, mouseX, mouseY);
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
    protected void keyTyped(final char typedChar, final int keyCode)
    {
        final KeyboardKey key = KeyboardKey.getForCode(keyCode);
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            BlockOut.getBlockOut().getProxy().getGuiController().closeUI(Minecraft.getMinecraft().player);
            return;
        }
        // Disabled to avoid any functionality like opening the player GUI on e. If we want this behavior we gotta write an if (shouldHandleKey()) which the fields should decide.
        //super.keyTyped(typedChar, keyCode);
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

    @Override
    @NotNull
    public IUIElementHost getRoot()
    {
        return root;
    }

    @Override
    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
        this.root.getUiManager().getRenderManager().setGui(this);
        this.root.update(this.root.getUiManager().getUpdateManager());
        this.initGui();
        ((BlockOutContainer) this.inventorySlots).setRoot(root);
    }

    @Override
    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }
}
