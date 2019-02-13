package com.ldtteam.blockout.management;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.management.focus.IFocusManager;
import com.ldtteam.blockout.management.input.IClickManager;
import com.ldtteam.blockout.management.input.IKeyManager;
import com.ldtteam.blockout.management.input.IScrollManager;
import com.ldtteam.blockout.management.input.client.IClientSideClickManager;
import com.ldtteam.blockout.management.input.client.IClientSideKeyManager;
import com.ldtteam.blockout.management.input.client.IClientSideScrollManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.minelaunch.profiler.IProfiler;
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

    @NotNull
    IClientSideClickManager getClientSideClickManager();

    @NotNull
    IClientSideKeyManager getClientSideKeyManager();

    @NotNull
    IClientSideScrollManager getClientSideScrollManager();

    @NotNull
    IUpdateManager getUpdateManager();

    @NotNull
    IRenderManager getRenderManager();

    @NotNull
    IProfiler getProfiler();
}
