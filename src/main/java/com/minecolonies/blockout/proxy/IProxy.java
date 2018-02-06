package com.minecolonies.blockout.proxy;

import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.connector.core.IUIElementFactoryController;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface IProxy
{
    @NotNull
    IGuiController getGuiController();

    @NotNull
    ILoaderManager getLoaderManager();

    @NotNull
    IUIElementFactoryController getFactoryController();

    @NotNull
    InputStream getResourceStream(@NotNull ResourceLocation location) throws Exception;

    @NotNull
    Vector2d getImageSize(@NotNull final ResourceLocation location);
}
