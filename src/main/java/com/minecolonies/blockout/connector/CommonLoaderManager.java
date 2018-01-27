package com.minecolonies.blockout.connector;

import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
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
    public IUIElementData loadData(@NotNull final ResourceLocation location)
    {
        try
        {
            return loaders.stream()
                     .filter(l -> l.accepts(location))
                     .findFirst()
                     .map(l -> l.createFromFile(location))
                     .orElseThrow(() -> new Exception("No loader can load params of type: " + location));
        }
        catch (Exception e)
        {
            Log.getLogger().warn(e);
        }

        return null;
    }
}
