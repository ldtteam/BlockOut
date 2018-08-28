package com.minecolonies.blockout.management.common.update;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import org.jetbrains.annotations.NotNull;

public final class ChildUpdateManager implements IUpdateManager
{

    @NotNull
    private final IUpdateManager updateManager;

    public ChildUpdateManager(@NotNull final IUpdateManager updateManager) {this.updateManager = updateManager;}

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        element.update(this);

        if (element instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) element;
            iuiElementHost.values().forEach(this::updateElement);
            iuiElementHost.onPostChildUpdate(this);
        }
    }

    @Override
    public void markDirty()
    {
        updateManager.markDirty();
    }
}
