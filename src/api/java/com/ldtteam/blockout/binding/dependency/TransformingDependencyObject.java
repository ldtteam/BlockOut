package com.ldtteam.blockout.binding.dependency;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class TransformingDependencyObject<T, I> implements IDependencyObject<T>
{
    private final Function<I, T> getTransformer;
    private final Function<T, I> setTransformer;

    private final IDependencyObject<I> inputDependency;

    @Nullable
    private int lastResolvedHash = 0;

    TransformingDependencyObject(
      final IDependencyObject<I> inputDependency,
      final Function<I, T> getTransformer,
      final Function<T, I> setTransformer)
    {
        this.getTransformer = getTransformer;
        this.setTransformer = setTransformer;
        this.inputDependency = inputDependency;
    }

    @Override
    public boolean requiresDataContext()
    {
        return inputDependency.requiresDataContext();
    }

    @Nullable
    @Override
    public T get(@Nullable final Object context)
    {
        final T value = getTransformer.apply(inputDependency.get(context));
        lastResolvedHash = value.hashCode();

        return value;
    }

    @Override
    public void set(@Nullable final Object context, @Nullable final T value)
    {
        inputDependency.set(context, setTransformer.apply(value));
    }

    @Override
    public boolean hasChanged(@Nullable final Object context)
    {
        final T resolved = getTransformer.apply(inputDependency.get(context));
        final int resolvedHash = resolved == null ? 0 : resolved.hashCode();

        return resolvedHash != lastResolvedHash;
    }
}
