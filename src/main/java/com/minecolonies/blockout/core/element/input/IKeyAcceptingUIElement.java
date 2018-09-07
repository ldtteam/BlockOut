package com.minecolonies.blockout.core.element.input;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;

public interface IKeyAcceptingUIElement extends IUIElement
{
    boolean canAcceptKeyInput(final int character, final KeyboardKey key);

    void onKeyPressed(final int character, final KeyboardKey key);
}
