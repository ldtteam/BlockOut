package com.minecolonies.blockout.proxy;

import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.connector.core.IUIElementFactoryController;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;


public interface IProxy
{

    @NotNull
    IThreadListener getTaskExecutorForMessage(@NotNull final MessageContext ctx);

    @NotNull
    IGuiController getGuiController();

    @NotNull
    ILoaderManager getLoaderManager();

    @NotNull
    IUIElementFactoryController getFactoryController();
}
