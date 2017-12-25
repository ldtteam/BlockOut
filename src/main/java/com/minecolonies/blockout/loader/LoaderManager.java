package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.core.Pane;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.views.View;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class LoaderManager
{

    public static final Set<ILoader> loaders = new HashSet<>();

    public static void registerLoader(@NotNull final ILoader loader)
    {
        loaders.add(loader);
    }

    public static Pane createFromPaneParams(@NotNull final IPaneParams params, @NotNull final View parent)
    {
        try
        {
            return loaders.stream()
                     .filter(l -> l.accepts(params))
                     .findFirst()
                     .map(l -> l.createFromPaneParams(params, parent))
                     .orElseThrow(() -> new Exception("No loader can load params of type: " + params.getClass().getName()));
        }
        catch (Exception e)
        {
            Log.getLogger().warn(e);
        }

        return null;
    }

    public static void createFromFile(final String resource, final View parent)
    {
        createFromFile(new ResourceLocation(resource), parent);
    }

    public static void createFromFile(final ResourceLocation resource, final View parent)
    {
        try
        {
            loaders.stream()
              .filter(l -> l.accepts(resource))
              .findFirst()
              .orElseThrow(() -> new Exception("No loader can load resource: " + resource))
              .createFromFile(resource, parent);
        }
        catch (Exception e)
        {
            Log.getLogger().warn(e);
        }
    }
}
