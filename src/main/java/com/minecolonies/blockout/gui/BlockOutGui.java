package com.minecolonies.blockout.gui;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.jetbrains.annotations.NotNull;

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
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {

    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
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
