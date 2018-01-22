package com.minecolonies.blockout.core.management.input;

import com.minecolonies.blockout.util.keyboard.KeyboardKey;

public interface IKeyManager extends IInputManager
{
    void onKeyPressed(final int character, final KeyboardKey key);
}
