package com.ldtteam.blockout.loader.object.loader;

import com.ldtteam.blockout.loader.IUIElementDataBuilder;
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
