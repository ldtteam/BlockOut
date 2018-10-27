package com.ldtteam.blockout.loader.factory.core;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface IUIElementDataComponentConverter<T>
{

    /**
     * Creates a new Instance of T from the given component.
     *
     * @param component The component to read from.
     * @param sourceData The source data that the component is pulled from.
     * @param params The parameters to read with.
     * @return The instance of T.
     */
    @NotNull
    T readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params);

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
