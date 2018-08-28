package com.minecolonies.blockout.style.core.resources.loader;

import com.minecolonies.blockout.style.core.resources.core.IResource;
import com.minecolonies.blockout.style.definitions.ResourceTypeDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IResourceLoaderManager
{
    @NotNull
    IResourceLoaderManager registerTypeLoader(@NotNull final IResourceLoader<? extends IResource> resourceLoader);

    @NotNull
    Collection<IResource> loadResources(@NotNull final ResourceTypeDefinition resourceTypeDefinition);
}
