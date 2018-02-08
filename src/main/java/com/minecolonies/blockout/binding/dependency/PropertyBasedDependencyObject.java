package com.minecolonies.blockout.binding.dependency;

import com.minecolonies.blockout.binding.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PropertyBasedDependencyObject<C, T> implements IDependencyObject<C, T>
{
    @NotNull
    private final Property<C, T> property;
    @Nullable
    private final T              def;

    public PropertyBasedDependencyObject(@NotNull final Property<C, T> property)
    {
        this(property, null);
    }

    public PropertyBasedDependencyObject(@NotNull final Property<C, T> property, final T def)
    {
        this.property = property;
        this.def = def;
    }

    @Nullable
    @Override
    public T get(@Nullable final C context)
    {
        if (context == null)
        {
            return def;
        }

        return property.apply(context).orElse(def);
    }

    @Override
    public void set(@Nullable final C context, @Nullable final T value)
    {
        if (context == null)
        {
            return;
        }

        property.accept(context, Optional.ofNullable(value));
    }
}
