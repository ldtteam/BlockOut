package com.minecolonies.blockout.management.server.network;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.server.ServerGuiController;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.object.ObjectUIElementData;
import com.minecolonies.blockout.management.server.network.messages.OnElementUpdatedMessage;
import com.minecolonies.blockout.management.server.network.messages.OnFocusedElementChangedMessage;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.mouse.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerNetworkManager implements INetworkManager
{
    @NotNull
    private final IGuiKey             guiKey;
    @NotNull
    private final ServerGuiController guiController;

    public ServerNetworkManager(@NotNull final IGuiKey guiKey)
    {
        this.guiKey = guiKey;
        guiController = (ServerGuiController) BlockOut.getBlockOut().getProxy().getGuiController();
    }

    @Override
    public void onFocusChanged(@Nullable final IUIElement newElement)
    {
        guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnFocusedElementChangedMessage(newElement), uuid));
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        //NOOP
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        //NOOP
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        //NOOP
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        //NOOP
    }

    @Override
    public void onElementChanged(@NotNull final IUIElement changedElement)
    {
        final IUIElementData dataCandidate = BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(changedElement);
        if (dataCandidate instanceof ObjectUIElementData)
        {
            guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnElementUpdatedMessage((ObjectUIElementData) dataCandidate), uuid));
        }
        else
        {
            throw new IllegalArgumentException("Cannot serialize given element into a serializable form of data");
        }
    }
}
