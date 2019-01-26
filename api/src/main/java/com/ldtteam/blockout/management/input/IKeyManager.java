package com.ldtteam.blockout.management.input;

import com.ldtteam.blockout.util.keyboard.KeyboardKey;

public interface IKeyManager extends IInputManager
{
    void onKeyPressed(final int character, final KeyboardKey key);
}
