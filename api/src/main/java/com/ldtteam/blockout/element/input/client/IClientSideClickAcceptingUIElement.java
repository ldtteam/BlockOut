package com.ldtteam.blockout.element.input.client;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.mouse.MouseButton;

public interface IClientSideClickAcceptingUIElement extends IUIElement
{

    boolean canAcceptMouseInputClient(final int localX, final int localY, final MouseButton button);

    default boolean onMouseClickBeginClient(final int localX, final int localY, final MouseButton button)
    {
        return false;
    }

    default boolean onMouseClickEndClient(final int localX, final int localY, final MouseButton button)
    {
        return false;
    }

    default boolean onMouseClickMoveClient(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        return false;
    }
}
