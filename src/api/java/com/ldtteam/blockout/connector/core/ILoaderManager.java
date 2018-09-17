package com.ldtteam.blockout.connector.core;

import com.ldtteam.blockout.json.loader.ILoader;
import com.ldtteam.blockout.json.loader.core.IUIElementData;
import org.jetbrains.annotations.NotNull;

public interface ILoaderManager
{
    void registerLoader(@NotNull ILoader loader);

    IUIElementData loadData(@NotNull final IGuiDefinitionLoader dataLoader);
}
