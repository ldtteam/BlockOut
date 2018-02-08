package com.minecolonies.blockout.binding.dependency;

import org.jetbrains.annotations.Nullable;

/**
 * A interface that describes a object that handles binding to a Object of type {@link T}
 *
 * @param <T> The type that is bound too.
 */
public interface IDependencyObject<C, T>
{
    /**
     * Method used to get the value of the dependency object.
     *
     * @return The dependency object.
     */
    @Nullable
    T get(@Nullable final C context);

    /**
     * Method used to set the value of the dependency object.
     *
     * @param value The new for the dependency object.
     */
    void set(@Nullable final C context, @Nullable final T value);
}
