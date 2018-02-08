package com.minecolonies.blockout.binding.dependency;

import com.minecolonies.blockout.binding.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DependencyObjectHelper
{

    private DependencyObjectHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static <C, T> IDependencyObject<C, T> createFromValue(@Nullable T value)
    {
        return new StaticDependencyObject<>(value);
    }

    public static <C, T> IDependencyObject<C, T> createFromProperty(@NotNull Property<C, T> property, @Nullable T def)
    {
        return new PropertyBasedDependencyObject<>(property, def);
    }
}