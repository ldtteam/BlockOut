package com.minecolonies.blockout.core.management;

import com.minecolonies.blockout.core.management.focus.IFocusManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.core.management.input.IScrollManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.root.RootGuiElement;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public interface IUIManager
{
    @NotNull
    RootGuiElement getHost();

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
    IUpdateManager getUpdateManager();

    @SideOnly(Side.CLIENT)
    @NotNull
    IRenderManager getRenderManager();

    Profiler getProfiler();
}
