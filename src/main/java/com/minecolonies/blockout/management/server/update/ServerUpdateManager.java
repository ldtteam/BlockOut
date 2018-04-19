package com.minecolonies.blockout.management.server.update;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

public class ServerUpdateManager implements IUpdateManager
{

    @NotNull
    private final IUIManager manager;
    private boolean dirty = false;

    public ServerUpdateManager(@NotNull final IUIManager manager) {this.manager = manager;}

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        if (element instanceof RootGuiElement)
        {
            RootGuiElement rootGuiElement = (RootGuiElement) element;
            rootGuiElement.update(new ChildUpdateManager(this));

            if (dirty)
            {
                manager.getNetworkManager().onElementChanged(element);
                dirty = false;
            }
        }
        else
        {
            Log.getLogger().warn("Somebody tried to update a none root element.");
        }
    }

    private final class ChildUpdateManager implements IUpdateManager
    {

        @NotNull
        private final IUpdateManager updateManager;

        private ChildUpdateManager(@NotNull final IUpdateManager updateManager) {this.updateManager = updateManager;}

        @Override
        public void updateElement(@NotNull final IUIElement element)
        {
            element.update(this);

            if (element instanceof IUIElementHost)
            {
                IUIElementHost iuiElementHost = (IUIElementHost) element;
                iuiElementHost.values().forEach(this::updateElement);
            }
        }

        @Override
        public void markDirty()
        {
            updateManager.markDirty();
        }
    }

    @Override
    public void markDirty()
    {
        this.dirty = true;
    }
}
