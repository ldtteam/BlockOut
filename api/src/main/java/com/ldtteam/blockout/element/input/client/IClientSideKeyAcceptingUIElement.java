package com.ldtteam.blockout.element.input.client;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;

public interface IClientSideKeyAcceptingUIElement extends IUIElement
{
    boolean canAcceptKeyInputClient(final int character, final KeyboardKey key);

    default boolean onKeyPressedClient(final int character, final KeyboardKey key)
    {
        return false;
    }

    boolean canAcceptCharacterInputClient(final char character, final int modifier);

    default boolean onCharacterPressed(final char character, final int modifier) { return false; }
}
