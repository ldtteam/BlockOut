package com.ldtteam.blockout.loader.binding.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Handles all binding for BlockOut.
 */
public interface IBindingEngine
{

    /**
     * Attempts a bind with the data stored in the component.
     * If no {@link IBindingCommand} was found that could successfully bind this component,
     * then {@link Optional#empty()} will be returned.
     *
     * @param component The {@link IUIElementDataComponent} to attempt binding with.
     * @param defaultValue The defaulvalue used incase binding fails because of syntax or other errors.
     * @param <T> The Type to bind to.
     * @return An optional possibly containing the  successful bind.
     */
    <T> Optional<IDependencyObject<T>> attemptBind(@NotNull final IUIElementDataComponent component, @Nullable T defaultValue);

    /**
     * Allows for the registration of new binding commands during runtime.
     *
     * @param commands The binding commands.
     * @return The engine this was called upon.
     */
    @NotNull
    IBindingEngine registerBindingCommand(@NotNull IBindingCommand... commands);

    /**
     * Allows for the registration of new binding transformers during runtime.
     *
     * @param transformers The binding transformers.
     * @return The engine this was called upon.
     */
    @NotNull
    IBindingEngine registerBindingTransformer(@NotNull IBindingTransformer... transformers);
}
