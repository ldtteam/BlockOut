package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.gui.BlockOutContainerGui;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.UIManager;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
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

    @Nullable
    @Override
    public void onMessageArrivalAtClient(@NotNull final NetworkEvent.Context ctx)
    {
        final IUIElement element = ProxyHolder.getInstance().getFactoryController().getElementFromData(getData());

        if (!(element instanceof RootGuiElement))
        {
            throw new IllegalStateException("Root element is not a RootGuiElement");
        }

        final RootGuiElement root = (RootGuiElement) element;
        root.setUiManager(new UIManager(root, getKey()));
        root.getUiManager().getUpdateManager().updateElement(root);
        final BlockOutContainer container = new BlockOutContainer(key, root, windowId);
        container.reinitializeSlots();
        final BlockOutContainerGui blockOutGuiDataIGuiContainer = new BlockOutContainerGui(container, Minecraft.getInstance().player.inventory, getKey(), root);

        Minecraft.getInstance().player.openContainer = container;
        Minecraft.getInstance().displayGuiScreen(blockOutGuiDataIGuiContainer);
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
