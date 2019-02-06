package com.ldtteam.blockout.connector.common;

import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.ILoaderManager;
import com.ldtteam.blockout.loader.ILoader;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CommonLoaderManager implements ILoaderManager
{

    private final Set<ILoader> loaders = new HashSet<>();

    @Override
    public void registerLoader(@NotNull final ILoader loader)
    {
        loaders.add(loader);
    }

    @Override
    public IUIElementData loadData(@NotNull final IGuiDefinitionLoader dataLoader)
    {
        try
        {
            return loaders.stream()
                     .map(l -> {
                         try
                         {
                             return l.createFromDataAndBindingEngine(dataLoader.getGuiDefinition());
                         }
                         catch (Exception e)
                         {
                             //TODO: Improve exception logging. Handle format errors somehow gracefully!
                             Log.getLogger().debug(String.format("Failed to load definition using loader: %s", l.getClass().getName()), e);
                             return null;
                         }
                     })
                     .filter(Objects::nonNull)
                     .findFirst()
                     .orElseThrow(() -> new Exception("No loader can load the given data into params."));
        }
        catch (Exception e)
        {
            Log.getLogger().warn(e);
        }

        return null;
    }
}
