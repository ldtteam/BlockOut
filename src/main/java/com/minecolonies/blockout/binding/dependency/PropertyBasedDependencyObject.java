package com.minecolonies.blockout.binding.dependency;

import com.minecolonies.blockout.binding.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class PropertyBasedDependencyObject<T> implements IDependencyObject<T>
{
    @NotNull
    private final Property<T> property;
    @Nullable
    private final T           def;
    @Nullable
    private       int         lastResolvedHash;

    public PropertyBasedDependencyObject(@NotNull final Property<T> property)
    {
        this(property, null);
    }

    public PropertyBasedDependencyObject(@NotNull final Property<T> property, final T def)
    {
        this.property = property;
        this.def = def;
    }

    @Nullable
    @Override
    public T get(@Nullable final Object context)
    {
        final T value;
        if (context == null)
        {
            value = def;
        }
        else
        {
            value = property.apply(context).orElse(def);
        }

        lastResolvedHash = value.hashCode();
        return value;
    }

    @Override
    public void set(@Nullable final Object context, @Nullable final T value)
    {
        if (context == null)
        {
            return;
        }

        property.accept(context, Optional.ofNullable(value));
    }

    @Override
    public boolean hasChanged(@Nullable final Object context)
    {
        final T resolved = context == null ? def : property.apply(context).orElse(def);
        final int currentHash = resolved == null ? 0 : resolved.hashCode();

        return lastResolvedHash == currentHash;
    }
}
