package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.binding.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A property based implementation of the {@link IDependencyObject}.
 * Uses {@link Property} to store the value.
 *
 * @param <T> The type stored in this {@link IDependencyObject} and in the underlying {@link Property}
 */
public final class PropertyBasedDependencyObject<T> implements IDependencyObject<T>
{

    @NotNull
    private final Property<T> property;
    @Nullable
    private final T           def;
    @Nullable
    private       int         lastResolvedHash;

    PropertyBasedDependencyObject(@NotNull final Property<T> property, @Nullable final T def)
    {
        this.property = property;
        this.def = def;
    }

    @Override
    public boolean requiresDataContext()
    {
        return this.property.requiresDataContext();
    }

    @Nullable
    @Override
    public T get(@Nullable final Object context)
    {
        final T value = property.apply(context).orElse(def);
        lastResolvedHash = value != null ? value.hashCode() : 0;
        return value;
    }

    @Override
    public void set(@Nullable final Object context, @Nullable final T value)
    {
        property.accept(context, value);
    }

    @Override
    public boolean hasChanged(@Nullable final Object context)
    {
        final T resolved = property.apply(context).orElse(def);
        final int currentHash = resolved == null ? 0 : resolved.hashCode();

        return lastResolvedHash != currentHash;
    }
}
