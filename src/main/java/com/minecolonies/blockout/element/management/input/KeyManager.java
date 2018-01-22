package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.element.input.IKeyAcceptingUIElement;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;

public class KeyManager extends AbstractInputManager<IKeyAcceptingUIElement> implements IKeyManager
{
    protected KeyManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        attemptInputInteraction();
    }
}
