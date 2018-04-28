package com.minecolonies.blockout.management.client.network;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.management.client.network.messages.OnKeyPressedMessage;
import com.minecolonies.blockout.management.client.network.messages.OnMouseClickBeginMessage;
import com.minecolonies.blockout.management.client.network.messages.OnMouseClickEndMessage;
import com.minecolonies.blockout.management.client.network.messages.OnMouseClickMoveMessage;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.mouse.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientNetworkManager implements INetworkManager
{

    @Override
    public void onFocusChanged(@Nullable final IUIElement newElement)
    {
        //NOOP
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        NetworkManager.sendToServer(new OnMouseClickBeginMessage(localX, localY, button));
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        NetworkManager.sendToServer(new OnMouseClickEndMessage(localX, localY, button));
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        NetworkManager.sendToServer(new OnMouseClickMoveMessage(localX, localY, button, timeElapsed));
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        NetworkManager.sendToServer(new OnKeyPressedMessage(character, key));
    }

    @Override
    public void onElementChanged(@NotNull final IUIElement changedElement)
    {
        //NOOP
    }
}
