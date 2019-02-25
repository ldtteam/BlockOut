package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.gui.BlockOutGuiData;
import com.ldtteam.blockout.gui.BlockOutGuiLogic;
import com.ldtteam.blockout.inventory.BlockOutContainerData;
import com.ldtteam.blockout.inventory.BlockOutContainerLogic;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.UIManager;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.ldtteam.jvoxelizer.IGameEngine;
import com.ldtteam.jvoxelizer.client.gui.IGuiContainer;
import com.ldtteam.jvoxelizer.inventory.IContainer;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
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
    public void onMessageArrivalAtClient(@NotNull final IMessageContext ctx)
    {
        final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(getData());

        if (!(element instanceof RootGuiElement))
        {
            throw new IllegalStateException("Root element is not a RootGuiElement");
        }

        final RootGuiElement root = (RootGuiElement) element;
        root.setUiManager(new UIManager(root, getKey()));
        root.getUiManager().getUpdateManager().updateElement(root);
        final IContainer<BlockOutContainerData> container = BlockOutContainerLogic.create(getKey(), root, getWindowId());
        final IGuiContainer<BlockOutGuiData> blockOutGuiDataIGuiContainer = BlockOutGuiLogic.create(getKey(), root, container);

        IGameEngine.getInstance().displayGuiScreen(blockOutGuiDataIGuiContainer);
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
