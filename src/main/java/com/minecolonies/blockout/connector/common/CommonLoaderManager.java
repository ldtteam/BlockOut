package com.minecolonies.blockout.connector.common;

import com.minecolonies.blockout.connector.core.IGuiDefinitionLoader;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
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
                             return l.createFromFile(dataLoader.getGuiDefinition());
                         }
                         catch (Exception e)
                         {
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
