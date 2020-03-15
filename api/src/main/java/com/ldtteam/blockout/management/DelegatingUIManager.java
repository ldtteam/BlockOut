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
import net.minecraft.profiler.Profiler;
import org.jetbrains.annotations.NotNull;

public class DelegatingUIManager implements IUIManager {

    private final IUIManager manager;

    public DelegatingUIManager(final IUIManager manager) {
        this.manager = manager;
    }

    @NotNull
    @Override
    public IUIElementHost getHost() {
        return manager.getHost();
    }

    @NotNull
    @Override
    public INetworkManager getNetworkManager() {
        return manager.getNetworkManager();
    }

    @NotNull
    @Override
    public IFocusManager getFocusManager() {
        return manager.getFocusManager();
    }

    @NotNull
    @Override
    public IClickManager getClickManager() {
        return manager.getClickManager();
    }

    @NotNull
    @Override
    public IKeyManager getKeyManager() {
        return manager.getKeyManager();
    }

    @NotNull
    @Override
    public IScrollManager getScrollManager() {
        return manager.getScrollManager();
    }

    @NotNull
    @Override
    public IClientSideClickManager getClientSideClickManager() {
        return manager.getClientSideClickManager();
    }

    @NotNull
    @Override
    public IClientSideKeyManager getClientSideKeyManager() {
        return manager.getClientSideKeyManager();
    }

    @NotNull
    @Override
    public IClientSideScrollManager getClientSideScrollManager() {
        return manager.getClientSideScrollManager();
    }

    @NotNull
    @Override
    public IUpdateManager getUpdateManager() {
        return manager.getUpdateManager();
    }

    @NotNull
    @Override
    public IRenderManager getRenderManager() {
        return manager.getRenderManager();
    }

    @NotNull
    @Override
    public Profiler getProfiler() {
        return manager.getProfiler();
    }
}
