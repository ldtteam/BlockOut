package com.minecolonies.blockout.gui;

import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class BlockOutGui extends GuiContainer
{
    private final IUIElementHost root;

    public BlockOutGui(final Container inventorySlotsIn)
    {
        super(inventorySlotsIn);
        root = null;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {

    }

    public IUIElementHost getRoot()
    {
        return root;
    }
}
