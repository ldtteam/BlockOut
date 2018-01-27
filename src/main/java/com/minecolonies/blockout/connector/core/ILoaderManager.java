package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface ILoaderManager
{
    void registerLoader(@NotNull ILoader loader);

    IUIElementData loadData(@NotNull ResourceLocation location);
}
