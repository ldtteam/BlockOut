package com.ldtteam.blockout.style.simple;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoader;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.ldtteam.blockout.style.definitions.ResourceTypeDefinition;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleResourceLoaderManager implements IResourceLoaderManager
{
    private static SimpleResourceLoaderManager                       ourInstance = new SimpleResourceLoaderManager();
    private final  Map<String, IResourceLoader<? extends IResource>> loaders     = new HashMap<>();

    private SimpleResourceLoaderManager()
    {
    }

    public static SimpleResourceLoaderManager getInstance()
    {
        return ourInstance;
    }

    @NotNull
    @Override
    public IResourceLoaderManager registerTypeLoader(@NotNull final IResourceLoader<? extends IResource> resourceLoader)
    {
        loaders.putIfAbsent(resourceLoader.getTypeId(), resourceLoader);
        return this;
    }

    @NotNull
    @Override
    public Collection<IResource> loadResources(@NotNull final ResourceTypeDefinition resourceTypeDefinition)
    {
        final IResourceLoader<? extends IResource> loader = loaders.get(resourceTypeDefinition.getTypeId());
        if (loader == null)
        {
            Log.getLogger()
              .warn("Attempted to load resource of type: " + resourceTypeDefinition.getTypeId() + " but no loader was known: " + Arrays.toString(loaders.keySet().toArray()));
            return Lists.newArrayList();
        }

        return resourceTypeDefinition
                 .getResources()
                 .stream()
                 .map(resourceDefinition -> loader.load(resourceDefinition.getId(), resourceDefinition.getData()))
                 .collect(Collectors.toList());
    }
}
