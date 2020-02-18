package com.ldtteam.blockout.element.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;

public interface IKeyAcceptingUIElement extends IUIElement
{
    boolean canAcceptKeyInput(final int character, final KeyboardKey key);

    void onKeyPressed(final int character, final KeyboardKey key);

    boolean canAcceptCharacterInput(final char character, final int modifier);

    void onCharacterInput(final char character, final int modifier);
}
