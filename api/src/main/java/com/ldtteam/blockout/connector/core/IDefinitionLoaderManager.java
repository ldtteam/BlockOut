package com.ldtteam.blockout.connector.core;

public interface IDefinitionLoaderManager {

    <T> void registerDefinitionLoader(final Class<T> loadableClass, final IGuiDefinitionLoader<T> loader);

    <T> IGuiDefinitionLoader<T> getLoaderFor(final T toLoadFrom);

    <T> IGuiDefinitionLoader<T> getLoaderForLoaderClass(final Class<T> toLoaderClass);
}
