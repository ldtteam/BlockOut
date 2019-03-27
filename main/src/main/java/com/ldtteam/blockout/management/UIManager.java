package com.ldtteam.blockout.management;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.management.client.input.ClientSideClickManager;
import com.ldtteam.blockout.management.client.input.ClientSideKeyManager;
import com.ldtteam.blockout.management.client.input.ClientSideScrollManager;
import com.ldtteam.blockout.management.common.focus.FocusManager;
import com.ldtteam.blockout.management.common.input.ClickManager;
import com.ldtteam.blockout.management.common.input.KeyManager;
import com.ldtteam.blockout.management.common.input.ScrollManager;
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
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class UIManager implements IUIManager
{
    @NotNull
    private final INetworkManager          networkManager;
    @NotNull
    private final IFocusManager            focusManager            = new FocusManager(this);
    @NotNull
    private final IClickManager            clickManager            = new ClickManager(this);
    @NotNull
    private final IKeyManager              keyManager              = new KeyManager(this);
    @NotNull
    private final IScrollManager           scrollManager           = new ScrollManager(this);
    @NotNull
    private final IClientSideClickManager  clientSideClickManager  = new ClientSideClickManager(this);
    @NotNull
    private final IClientSideKeyManager    clientSideKeyManager    = new ClientSideKeyManager(this);
    @NotNull
    private final IClientSideScrollManager clientSideScrollManager = new ClientSideScrollManager(this);
    @NotNull
    private final IUpdateManager           updateManager;
    @NotNull
    private final IRenderManager           renderManager;
    @NotNull
    private final IProfiler                profiler;
    @NotNull
    private       RootGuiElement           rootGuiElement;

    public UIManager(@NotNull final RootGuiElement rootGuiElement, @NotNull final IGuiKey key)
    {
        this.rootGuiElement = rootGuiElement;
        this.networkManager = BlockOut.getBlockOut().getProxy().generateNewNetworkManagerForGui(key);
        this.updateManager = BlockOut.getBlockOut().getProxy().generateNewUpdateManager(this);
        this.renderManager = IDistributionExecutor.on(() -> BlockOut.getBlockOut().getProxy().generateNewRenderManager(), () -> null);
        this.profiler = IProfiler.create();
        this.profiler.enableProfiling();
        this.profiler.setProfilingMap(new LinkedHashMap<>());
    }

    @NotNull
    @Override
    public RootGuiElement getHost()
    {
        return rootGuiElement;
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

    @NotNull
    @Override
    public IClientSideClickManager getClientSideClickManager()
    {
        return clientSideClickManager;
    }

    @NotNull
    @Override
    public IClientSideKeyManager getClientSideKeyManager()
    {
        return clientSideKeyManager;
    }

    @NotNull
    @Override
    public IClientSideScrollManager getClientSideScrollManager()
    {
        return clientSideScrollManager;
    }

    @NotNull
    @Override
    public IUpdateManager getUpdateManager()
    {
        return updateManager;
    }

    @NotNull
    @Override
    public IRenderManager getRenderManager()
    {
        return renderManager;
    }

    @Override
    public IProfiler getProfiler()
    {
        return profiler;
    }

    public void setRootGuiElement(@NotNull final RootGuiElement rootGuiElement)
    {
        this.rootGuiElement = rootGuiElement;
    }
}
