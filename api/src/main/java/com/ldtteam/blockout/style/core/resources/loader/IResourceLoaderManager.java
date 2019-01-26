package com.ldtteam.blockout.style.core.resources.loader;

import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.definitions.ResourceTypeDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IResourceLoaderManager
{
    @NotNull
    IResourceLoaderManager registerTypeLoader(@NotNull final IResourceLoader<? extends IResource> resourceLoader);

    @NotNull
    Collection<IResource> loadResources(@NotNull final ResourceTypeDefinition resourceTypeDefinition);
}
