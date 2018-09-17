package com.ldtteam.blockout.json.loader.object.loader;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a class that can load IUIElement data by code.
 * A default public no args constructor is required for use.
 */
public interface IClassBasedUICreator
{
    /**
     * Method called by the loader when the implementing class has been instantiated to load the ui data into the builder.
     * @param builder The builder to load the ui data into.
     */
    void build(@NotNull final IUIElementDataBuilder builder);
}
