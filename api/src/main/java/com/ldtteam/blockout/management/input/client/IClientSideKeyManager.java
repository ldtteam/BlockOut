package com.ldtteam.blockout.management.input.client;

import com.ldtteam.blockout.management.input.IInputManager;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;

public interface IClientSideKeyManager
{
    boolean onKeyPressed(final int character, final KeyboardKey key);
}
