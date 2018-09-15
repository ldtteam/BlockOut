package com.ldtteam.blockout.core.management;

import com.ldtteam.blockout.core.management.focus.IFocusManager;
import com.ldtteam.blockout.core.management.input.IClickManager;
import com.ldtteam.blockout.core.management.input.IKeyManager;
import com.ldtteam.blockout.core.management.input.IScrollManager;
import com.ldtteam.blockout.core.management.network.INetworkManager;
import com.ldtteam.blockout.core.management.render.IRenderManager;
import com.ldtteam.blockout.core.management.update.IUpdateManager;
import com.ldtteam.blockout.element.root.RootGuiElement;
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
