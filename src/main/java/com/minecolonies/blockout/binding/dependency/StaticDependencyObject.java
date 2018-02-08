package com.minecolonies.blockout.binding.dependency;

import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link IDependencyObject} that holds a single value.
 *
 * @param <T> The type on which is depended.
 */
public final class StaticDependencyObject<C, T> implements IDependencyObject<C, T>
{
    @Nullable
    private T value;

    public StaticDependencyObject(@Nullable final T value) {this.value = value;}

    @Nullable
    @Override
    public T get(@Nullable final C context)
    {
        return value;
    }

    @Override
    public void set(@Nullable final C context, @Nullable final T value)
    {
        this.value = value;
    }
}
