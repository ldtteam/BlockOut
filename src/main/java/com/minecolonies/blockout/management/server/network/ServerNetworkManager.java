package com.minecolonies.blockout.management.server.network;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.server.ServerGuiController;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.object.ObjectUIElementData;
import com.minecolonies.blockout.management.server.network.messages.*;
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
        guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnMouseClickBeginMessage(localX, localY, button), uuid));
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnMouseClickEndMessage(localX, localY, button), uuid));
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnMouseClickMoveMessage(localX, localY, button, timeElapsed), uuid));
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnKeyPressedMessage(character, key), uuid));
    }

    @Override
    public void onMouseScroll(final int localX, final int localY, final int deltaWheel)
    {
        guiController.getUUIDsOfPlayersWatching(guiKey).forEach(uuid -> NetworkManager.sendTo(new OnMouseScrollMessage(localX, localY, deltaWheel), uuid));
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
