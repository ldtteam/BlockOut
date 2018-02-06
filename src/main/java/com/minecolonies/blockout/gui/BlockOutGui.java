package com.minecolonies.blockout.gui;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class BlockOutGui extends GuiContainer
{
    private final IGuiKey        key;
    private final IUIElementHost root;

    public BlockOutGui(final Container inventorySlotsIn, final IGuiKey key)
    {
        super(inventorySlotsIn);
        this.key = key;
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

    public IGuiKey getKey()
    {
        return key;
    }
}
