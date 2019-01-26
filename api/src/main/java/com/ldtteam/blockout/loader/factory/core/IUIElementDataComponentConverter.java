package com.ldtteam.blockout.loader.factory.core;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIElementDataComponentConverter<T>
{

    /**
     * Checks if the given component is readable by this converter.
     *
     * @param component The component to check.
     * @return True when this converter can read the element, false when not.
     */
    boolean matchesInputTypes(@NotNull final IUIElementDataComponent component);

    /**
     * Creates a new Instance of T from the given component.
     *
     * @param component  The component to read from.
     * @param sourceData The source data that the component is pulled from.
     * @param params     The parameters to read with.
     * @return The instance of T.
     */
    @NotNull
    T readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params);

    /**
     * Creates a new component from the given instance of T.
     *
     * @param value                        The given instance of T
     * @param newComponentInstanceProducer The component Producer.
     * @return The new instance.
     */
    <C extends IUIElementDataComponent> C writeToElement(@NotNull final T value, @NotNull Function<ComponentType, C> newComponentInstanceProducer);
}
