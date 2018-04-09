package com.minecolonies.blockout.connector.common;

import com.google.common.io.CharStreams;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
    public IUIElementData loadData(@NotNull final ResourceLocation location)
    {
        try
        {
            final InputStream stream = BlockOut.getBlockOut().getProxy().getResourceStream(location);
            String data = null;
            try (final Reader reader = new InputStreamReader(stream))
            {
                data = CharStreams.toString(reader);
            }

            return loadData(data);
        }
        catch (Exception e)
        {
            Log.getLogger().warn("Failed to read data from location: " + location, e);
        }

        return null;
    }

    @Override
    public IUIElementData loadData(@NotNull final String data)
    {
        try
        {
            return loaders.stream()
                     .map(l -> {
                         try
                         {
                             return l.createFromFile(data);
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
