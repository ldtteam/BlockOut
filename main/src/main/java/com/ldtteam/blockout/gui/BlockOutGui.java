package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.inventory.slot.SlotBlockOut;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.ldtteam.jvoxelizer.client.guitemp.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.IOpenGl;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class BlockOutGui extends GuiContainer implements IBlockOutGui, IGui
{
    @NotNull
    private final IGuiKey        key;
    @NotNull
    private       IUIElementHost root;

    @NotNull
    private Vector2d scaleFactor = new Vector2d(1, 1);

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
    public void setRoot(@NotNull final IUIElementHost root)
    {
        this.root = root;
        this.root.getUiManager().getRenderManager().setGui(this);
        this.root.update(this.root.getUiManager().getUpdateManager());
        this.initGui();
        ((BlockOutContainer) this.inventorySlots).setRoot(root);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        this.isDrawing = true;

        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        //Can be done here since both fore and background methods are called by the super
        this.getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(scaledMouseX, scaledMouseY);

        IOpenGl.pushMatrix();

        IOpenGl.scale(1 / this.scaleFactor.getX(), 1 / this.scaleFactor.getY(), 1f);

        IOpenGl.pushMatrix();

        super.drawScreen(scaledMouseX, scaledMouseY, partialTicks);

        IOpenGl.popMatrix();
        IOpenGl.popMatrix();

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

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
    {
        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        super.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
        if (!getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickBegin(scaledMouseX, scaledMouseY, MouseButton.getForCode(mouseButton)))
        {
            getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickBegin((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), MouseButton.getForCode(mouseButton));
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
               .onMouseClickMove(scaledMouseX, scaledMouseY,
                 MouseButton.getForCode(clickedMouseButton),
                 timeSinceLastClick))
        {
            getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickMove((int) (scaledMouseX - (guiLeft * scaleFactor.getX())),
                (int) (scaledMouseY - (guiTop * scaleFactor.getY())),
                MouseButton.getForCode(clickedMouseButton),
                timeSinceLastClick);
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
               .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(state)))
        {
            getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickEnd((int) (scaledMouseX - (guiLeft * scaleFactor.getX())), (int) (scaledMouseY - (guiTop * scaleFactor.getY())), MouseButton.getForCode(state));
        }
    }

    /**
     * Returns whether the mouse is over the given slot.
     */
    @Override
    public boolean isMouseOverSlot(final Slot slotIn, final int mouseX, final int mouseY)
    {
        int scaledMouseX = (int) (mouseX * this.scaleFactor.getX());
        int scaledMouseY = (int) (mouseY * this.scaleFactor.getY());

        return ((!isDrawing && (!(slotIn instanceof SlotBlockOut) || ((SlotBlockOut) slotIn).getUiSlotInstance().isEnabled())) || !(slotIn instanceof SlotBlockOut))
                 && super.isMouseOverSlot(slotIn, scaledMouseX, scaledMouseY);
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

        if (!getRoot().getUiManager().getClientSideKeyManager().onKeyPressed(typedChar, key))
        {
            getRoot().getUiManager().getNetworkManager().onKeyPressed(typedChar, key);
        }
    }

    @Override
    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }
}
