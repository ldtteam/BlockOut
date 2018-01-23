package com.minecolonies.blockout.core.element.input;

import com.minecolonies.blockout.util.keyboard.KeyboardKey;

public interface IKeyAcceptingUIElement
{
    boolean canAcceptKeyInput(final int character, final KeyboardKey key);

    void onKeyPressed(final int character, final KeyboardKey key);
}
