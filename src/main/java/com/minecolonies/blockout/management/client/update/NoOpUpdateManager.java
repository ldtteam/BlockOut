package com.minecolonies.blockout.management.client.update;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import org.jetbrains.annotations.NotNull;

public class NoOpUpdateManager implements IUpdateManager
{

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        //NOOP
        return;
    }

    @Override
    public void markDirty()
    {
        //NOOP
    }
}
