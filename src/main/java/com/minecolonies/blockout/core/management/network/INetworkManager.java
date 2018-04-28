package com.minecolonies.blockout.core.management.network;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.mouse.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface INetworkManager
{
    void onFocusChanged(@Nullable final IUIElement newElement);

    void onMouseClickBegin(final int localX, final int localY, MouseButton button);

    void onMouseClickEnd(final int localX, final int localY, MouseButton button);

    void onMouseClickMove(final int localX, final int localY, MouseButton button, final float timeElapsed);

    void onKeyPressed(final int character, final KeyboardKey key);

    void onElementChanged(@NotNull final IUIElement changedElement);
}
