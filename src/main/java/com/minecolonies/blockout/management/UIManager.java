package com.minecolonies.blockout.management;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.focus.IFocusManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.core.management.input.IScrollManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.management.common.focus.FocusManager;
import com.minecolonies.blockout.management.common.input.ClickManager;
import com.minecolonies.blockout.management.common.input.KeyManager;
import com.minecolonies.blockout.management.common.input.ScrollManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class UIManager implements IUIManager
{
    @NotNull
    private final RootGuiElement  rootGuiElement;
    @NotNull
    private final INetworkManager networkManager;

    @NotNull
    private final IFocusManager  focusManager  = new FocusManager(this);
    @NotNull
    private final IClickManager  clickManager  = new ClickManager(this);
    @NotNull
    private final IScrollManager scrollManager = new ScrollManager(this);
    @NotNull
    private final IKeyManager    keyManager    = new KeyManager(this);
    @NotNull
    private final IUpdateManager updateManager;
    @NotNull
    private final IRenderManager renderManager;

    public UIManager(@NotNull final RootGuiElement rootGuiElement, @NotNull final IGuiKey key)
    {
        this.rootGuiElement = rootGuiElement;
        this.networkManager = BlockOut.getBlockOut().getProxy().generateNewNetworkManagerForGui(key);
        this.updateManager = BlockOut.getBlockOut().getProxy().generateNewUpdateManager(this);
        this.renderManager = BlockOut.getBlockOut().getProxy().generateNewRenderManager();
    }

    @NotNull
    @Override
    public IUIElementHost getHost()
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
    public IUpdateManager getUpdateManager()
    {
        return updateManager;
    }

    @NotNull
    @Override
    @SideOnly(Side.CLIENT)
    public IRenderManager getRenderManager()
    {
        return renderManager;
    }
}
