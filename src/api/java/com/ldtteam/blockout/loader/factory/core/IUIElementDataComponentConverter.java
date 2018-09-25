package com.ldtteam.blockout.loader.factory.core;

import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface IUIElementDataComponentConverter<T>
{

    /**
     * Creates a new Instance of T from the given component
     * @param component
     * @return
     */
    T readFromElement(@NotNull final IUIElementDataComponent component);

    /**
     * Creates a new component from the given instance of T.
     *
     * @param value The given instance of T
     * @param newComponentInstanceProducer The component Producer.
     *
     * @return The new instance.
     */
    IUIElementDataComponent writeToElement(@NotNull final T value, @NotNull Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer);
}
