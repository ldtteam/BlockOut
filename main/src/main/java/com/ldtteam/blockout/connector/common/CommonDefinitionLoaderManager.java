package com.ldtteam.blockout.connector.common;

import com.google.common.collect.Maps;
import com.ldtteam.blockout.connector.core.IDefinitionLoaderManager;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;

import java.util.Map;

public class CommonDefinitionLoaderManager implements IDefinitionLoaderManager {

    private final Map<Class<?>, IGuiDefinitionLoader<?>> loaders = Maps.newConcurrentMap();

    @Override
    public <T> void registerDefinitionLoader(final Class<T> loadableClass, final IGuiDefinitionLoader<T> loader) {
        loaders.put(loadableClass, loader);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> IGuiDefinitionLoader<T> getLoaderFor(final T toLoadFrom) {
        return (IGuiDefinitionLoader<T>) loaders.get(toLoadFrom.getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> IGuiDefinitionLoader<T> getLoaderForLoaderClass(final Class<T> toLoaderClass) {
        return (IGuiDefinitionLoader<T>) loaders.get(toLoaderClass);
    }
}
