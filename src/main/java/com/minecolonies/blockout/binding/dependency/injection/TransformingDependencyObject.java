package com.minecolonies.blockout.binding.dependency.injection;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TransformingDependencyObject<T, I> implements IDependencyObject<T>
{
    private final Function<I, T> getTransformer;
    private final Function<T, I> setTransformer;

    private final IDependencyObject<I> inputDependency;

    public TransformingDependencyObject(
      final IDependencyObject<I> inputDependency,
      final Function<I, T> getTransformer,
      final Function<T, I> setTransformer)
    {
        this.getTransformer = getTransformer;
        this.setTransformer = setTransformer;
        this.inputDependency = inputDependency;
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
        return getTransformer.apply(inputDependency.get(context));
    }

    /**
     * Method used to set the value of the dependency object.
     *
     * @param value The new for the dependency object.
     */
    @Override
    public void set(@Nullable final Object context, @Nullable final T value)
    {
        inputDependency.set(context, setTransformer.apply(value));
    }
}
