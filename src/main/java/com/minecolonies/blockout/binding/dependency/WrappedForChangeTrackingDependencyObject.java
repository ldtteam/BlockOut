package com.minecolonies.blockout.binding.dependency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class WrappedForChangeTrackingDependencyObject<T> implements IDependencyObject<T>
{

    @NotNull
    private final Supplier<IDependencyObject<T>> watchedSupplier;

    @NotNull
    private int lastResolvedHash = 0;

    public WrappedForChangeTrackingDependencyObject(
      @NotNull final Supplier<IDependencyObject<T>> watchedSupplier)
    {
        this.watchedSupplier = watchedSupplier;
    }

    /**
     * Method used to get the value of the dependency object.
     *
     * @return The dependency object.
     */
    @Nullable
    @Override
    public T get(@Nullable final Object context)
    {
        final IDependencyObject<T> dependencyObject = watchedSupplier.get();
        final T instance = dependencyObject.get(context);

        lastResolvedHash = instance == null ? 0 : instance.hashCode();

        return instance;
    }

    /**
     * Method used to set the value of the dependency object.
     *
     * @param value The new for the dependency object.
     */
    @Override
    public void set(@Nullable final Object context, @Nullable final T value)
    {
        watchedSupplier.get().set(context, value);
    }

    /**
     * Indicates if this {@link IDependencyObject} has changed from the last time {@link #get(Object)} was called.
     *
     * @param context The context.
     * @return True when this object has changed, false when not.
     */
    @Override
    public boolean hasChanged(@Nullable final Object context)
    {
        final IDependencyObject<T> dependencyObject = watchedSupplier.get();
        final T instance = dependencyObject.get(context);

        return lastResolvedHash != (instance == null ? 0 : instance.hashCode());
    }
}
