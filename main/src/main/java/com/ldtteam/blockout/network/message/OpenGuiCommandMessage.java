package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.gui.BlockOutGui;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.UIManager;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenGuiCommandMessage implements IBlockOutServerToClientMessage
{
    @NotNull
    private IGuiKey        key;
    @NotNull
    private IUIElementData data;
    @NotNull
    private int            windowId;

    public OpenGuiCommandMessage()
    {
    }

    public OpenGuiCommandMessage(@NotNull final IGuiKey key, @NotNull final IUIElementData data, @NotNull final int windowId)
    {
        this.key = key;
        this.data = data;
        this.windowId = windowId;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(getData());

        if (!(element instanceof RootGuiElement))
        {
            throw new IllegalStateException("Root element is not a RootGuiElement");
        }

        final RootGuiElement root = (RootGuiElement) element;
        root.setUiManager(new UIManager(root, getKey()));
        root.getUiManager().getUpdateManager().updateElement(root);
        final BlockOutGui gui = new BlockOutGui(new BlockOutContainer(getKey(), root, getWindowId()));

        Minecraft.getMinecraft().displayGuiScreen(gui);
    }

    @NotNull
    public IUIElementData getData()
    {
        return data;
    }

    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }

    @NotNull
    public int getWindowId()
    {
        return windowId;
    }
}
