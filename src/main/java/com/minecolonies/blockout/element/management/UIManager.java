package com.minecolonies.blockout.element.management;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.focus.IFocusManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.core.management.input.IScrollManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import org.jetbrains.annotations.NotNull;

public class UIManager implements IUIManager
{

    @NotNull
    private final IUIElementHost host;
    @NotNull
    private final INetworkManager networkManager = new NetworkManager();


    public UIManager(@NotNull final IUIElementHost host) {
        this.host = host;
    }

    @NotNull
    @Override
    public IUIElementHost getHost()
    {
        return host;
    }

    @NotNull
    @Override
    public INetworkManager getNetworkManager()
    {
        return networkManager;
    }

    @NotNull
    @Override
    public IFocusManager getFocusManager()
    {
        return focusManager;
    }

    @NotNull
    @Override
    public IClickManager getClickManager()
    {
        return clickManager;
    }

    @NotNull
    @Override
    public IKeyManager getKeyManager()
    {
        return keyManager;
    }

    @NotNull
    @Override
    public IScrollManager getScrollManager()
    {
        return scrollManager;
    }
}
