package com.minecolonies.blockout.core.management;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.focus.IFocusManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.core.management.input.IScrollManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import org.jetbrains.annotations.NotNull;

public interface IUIManager
{
    @NotNull
    IUIElementHost getHost();

    @NotNull
    INetworkManager getNetworkManager();

    @NotNull
    IFocusManager getFocusManager();

    @NotNull
    IClickManager getClickManager();

    @NotNull
    IKeyManager getKeyManager();

    @NotNull
    IScrollManager getScrollManager();
}
