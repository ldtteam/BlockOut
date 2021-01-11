package com.ldtteam.blockout.management.client.network;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.client.network.messages.*;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.mouse.MouseButton;
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
        IProxy.getInstance().getNetworkingManager().sendToServer(new OnMouseClickBeginMessage(localX, localY, button));
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        IProxy.getInstance().getNetworkingManager().sendToServer(new OnMouseClickEndMessage(localX, localY, button));
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        IProxy.getInstance().getNetworkingManager().sendToServer(new OnMouseClickMoveMessage(localX, localY, button, timeElapsed));
    }

    @Override
    public void onMouseWheel(final int localX, final int localY, final int deltaWheel)
    {
        IProxy.getInstance().getNetworkingManager().sendToServer(new OnMouseWheelMessage(localX, localY, deltaWheel));
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        IProxy.getInstance().getNetworkingManager().sendToServer(new OnKeyPressedMessage(character, key));
    }

    @Override
    public void onCharacterTyped(final char character, final int modifier)
    {
        IProxy.getInstance().getNetworkingManager().sendToServer(new OnCharacterTypedMessage(character, modifier));
    }

    @Override
    public void onElementChanged(@NotNull final IUIElement changedElement)
    {
        //NOOP
    }
}
