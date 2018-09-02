package com.minecolonies.blockout.binding.dependency;

import com.minecolonies.blockout.binding.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class DependencyObjectHelper
{

    private DependencyObjectHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static <T> IDependencyObject<T> createFromValue(@Nullable T value)
    {
        return new StaticDependencyObject<>(value);
    }

    public static <T> IDependencyObject<T> createFromProperty(@NotNull Property<T> property, @Nullable T def)
    {
        return new PropertyBasedDependencyObject<>(property, def);
    }

    public static <T, I> IDependencyObject<T> transform(
      final IDependencyObject<I> inputDependency,
      final Function<I, T> getTransformer,
      final Function<T, I> setTransformer)
    {
        return new TransformingDependencyObject<>(inputDependency, getTransformer, setTransformer);
    }
}