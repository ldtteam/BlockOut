package com.minecolonies.blockout.binding.dependency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link IDependencyObject} that holds a single value.
 *
 * @param <T> The type on which is depended.
 */
public final class StaticDependencyObject<T> implements IDependencyObject<T>
{
    @Nullable
    private T value;
    @NotNull
    private boolean hasChanged = false;

    public StaticDependencyObject(@Nullable final T value) {this.value = value;}

    @Nullable
    @Override
    public T get(@Nullable final Object context)
    {
        this.hasChanged = false;
        return value;
    }

    @Override
    public void set(@Nullable final Object context, @Nullable final T value)
    {
        this.value = value;
        this.hasChanged = true;
    }

    @Override
    public boolean hasChanged(@Nullable final Object context)
    {
        return hasChanged;
    }
}
