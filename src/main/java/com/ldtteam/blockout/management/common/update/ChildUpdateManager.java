package com.ldtteam.blockout.management.common.update;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.management.update.IUpdateManager;
import org.jetbrains.annotations.NotNull;

public final class ChildUpdateManager implements IUpdateManager
{

    @NotNull
    private final IUpdateManager updateManager;

    public ChildUpdateManager(@NotNull final IUpdateManager updateManager) {this.updateManager = updateManager;}

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        element.getParent().getUiManager().getProfiler().startSection("Core Update (" +element.getId() + ")");
        element.update(this);
        element.getParent().getUiManager().getProfiler().endSection();

        if (element instanceof IUIElementHost)
        {
            element.getParent().getUiManager().getProfiler().startSection("Children Update (" +element.getId() + ")");
            IUIElementHost iuiElementHost = (IUIElementHost) element;
            iuiElementHost.values().forEach(this::updateElement);

            element.getParent().getUiManager().getProfiler().startSection("Post-Children Update (" +element.getId() + ")");
            iuiElementHost.onPostChildUpdate(this);
            element.getParent().getUiManager().getProfiler().endSection();
            element.getParent().getUiManager().getProfiler().endSection();
        }
    }

    @Override
    public void markDirty()
    {
        updateManager.markDirty();
    }
}
