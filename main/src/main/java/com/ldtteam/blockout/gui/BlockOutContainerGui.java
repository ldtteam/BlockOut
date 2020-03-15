package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.inventory.slot.BlockOutSlot;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.StringTextComponent;

public class BlockOutContainerGui extends ContainerScreen<BlockOutContainer> implements IBlockOutGui
{

    private final BlockOutGuiData guiData;

    public BlockOutContainerGui(final BlockOutContainer screenContainer, final PlayerInventory inv, final IGuiKey key, final IUIElementHost host)
    {
        super(screenContainer, inv, new StringTextComponent(""));
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
        xSize = (int) getInstanceData().getXSize();
        ySize = (int) getInstanceData().getYSize();

        super.init();

        getInstanceData().setGuiOffset(getGuiTop(), getGuiLeft());

        //Update the margins to be scaled now.
        int scaledGuiLeft = (int) (getInstanceData().getGuiLeft() * getInstanceData().getScaleFactor().getX());
        int scaledGuiTop = (int) (getInstanceData().getGuiTop() * getInstanceData().getScaleFactor().getY());

        getInstanceData().getRoot().setMargin(new AxisDistance(scaledGuiLeft, scaledGuiTop, scaledGuiLeft, scaledGuiTop));
        getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(getInstanceData().getRoot());
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTick)
    {
        getInstanceData().setDrawing(true);

        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        //Can be done here since both fore and background methods are called by the super
        getInstanceData().getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(scaledMouseX, scaledMouseY);

        RenderSystem.pushMatrix();

        RenderSystem.scaled(1 / getInstanceData().getScaleFactor().getX(), 1 / getInstanceData().getScaleFactor().getY(), 1d);

        RenderSystem.pushMatrix();

        super.render(mouseX, mouseY, partialTick);

        RenderSystem.popMatrix();
        RenderSystem.popMatrix();

        getInstanceData().setDrawing(false);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        getInstanceData().getRoot().getUiManager().getRenderManager().drawForeground(getInstanceData().getRoot());
        getInstanceData().getRoot().getUiManager().getRenderManager().drawTooltip(getInstanceData().getRoot(), scaledMouseX, scaledMouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        getInstanceData().getRoot().getUiManager().getRenderManager().drawBackground(getInstanceData().getRoot());
    }

    @Override
    public void drawSlot(final Slot slotIn)
    {
        if (getInstanceData().isDrawing() && slotIn instanceof BlockOutSlot)
        {
            return;
        }

        super.drawSlot(slotIn);
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double dWheel)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        super.mouseScrolled(mouseX, mouseY, dWheel);

        int delta = (int) dWheel;
        if (delta != 0)
        {
            if (!getInstanceData().getRoot().getUiManager()
                   .getClientSideScrollManager()
                   .onMouseWheel(scaledMouseX, scaledMouseY, delta))
            {
                getInstanceData().getRoot().getUiManager()
                  .getNetworkManager()
                  .onMouseWheel((int) (scaledMouseX - getGuiLeft() * getInstanceData().getScaleFactor().getX()),
                          (int) (scaledMouseY - getGuiTop() * getInstanceData().getScaleFactor().getY()), delta);
            }
        }

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
              .onMouseClickBegin((int) (scaledMouseX - getGuiLeft() * getInstanceData().getScaleFactor().getX()),
                      (int) (scaledMouseY - getGuiTop() * getInstanceData().getScaleFactor().getY()),
                      MouseButton.getForCode(button));
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

        double scaledDeltaX = deltaX * getInstanceData().getScaleFactor().getX();
        double scaledDeltaY = deltaY * getInstanceData().getScaleFactor().getY();

        super.mouseDragged(scaledMouseX, scaledMouseY, button, scaledDeltaX, scaledDeltaY);

        if (!getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickMove(scaledMouseX, scaledMouseY, MouseButton.getForCode(button), 0f))
        {
            getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickMove((int) (scaledMouseX - getGuiLeft() * getInstanceData().getScaleFactor().getX()),
                      (int) (scaledMouseY - getGuiTop() * getInstanceData().getScaleFactor().getY()), MouseButton.getForCode(button), 0f);
        }

        return true;
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button)
    {
        int scaledMouseX = (int) (mouseX * getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (mouseY * getInstanceData().getScaleFactor().getY());

        super.mouseReleased(scaledMouseX, scaledMouseY, button);

        if (!getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(button)))
        {
            getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickEnd((int) (scaledMouseX - getGuiLeft() * getInstanceData().getScaleFactor().getX()),
                      (int) (scaledMouseY - getGuiTop() * getInstanceData().getScaleFactor().getY()), MouseButton.getForCode(button));
        }

        return true;
    }

    @Override
    public boolean isSlotSelected(final Slot slot, final double mouseX, final double mouseY)
    {
        return ((!getInstanceData().isDrawing() && (!(slot instanceof BlockOutSlot) || ((BlockOutSlot) slot).getUiSlotInstance().isEnabled())) || !(slot instanceof BlockOutSlot)) && super.isSlotSelected(slot, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifier)
    {
        final KeyboardKey key = KeyboardKey.getForCode(keyCode);
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            ProxyHolder.getInstance().getGuiController().closeUI(Minecraft.getInstance().player);
            return true;
        }

        if (!getInstanceData().getRoot().getUiManager().getClientSideKeyManager().onKeyPressed(keyCode, key))
        {
            getInstanceData().getRoot().getUiManager().getNetworkManager().onKeyPressed(keyCode, key);
        }

        return true;
    }

    @Override
    public boolean charTyped(final char character, final int modifier) {
        if (!getInstanceData().getRoot().getUiManager().getClientSideKeyManager().onCharacterTyped(character, modifier))
        {
            getInstanceData().getRoot().getUiManager().getNetworkManager().onCharacterTyped(character, modifier);
        }

        return true;
    }

    @Override
    public void tick()
    {
        getInstanceData().setGuiOffset(guiTop, guiLeft);
        getInstanceData().setGuiSize(xSize, ySize);
    }
}
