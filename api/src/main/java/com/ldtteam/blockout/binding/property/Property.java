package com.ldtteam.blockout.binding.property;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents access to a field in a class, or a lonely variable.
 * Combines the setter and getter into one class.
 * <p>
 * Also keeps track if the data context is required for this property to function.
 *
 * @param <T> The type stored in the property.
 */
public class Property<T> implements BiConsumer<Object, T>, Function<Object, Optional<T>>
{
    @NotNull
    private final boolean                         requiresDataContext;
    @NotNull
    private final Optional<Function<Object, T>>   getter;
    @NotNull
    private final Optional<BiConsumer<Object, T>> setter;

    public Property(
      @NotNull final Optional<Function<Object, T>> getter,
      @NotNull final Optional<BiConsumer<Object, T>> setter,
      @NotNull final boolean requiresDataContext)
    {
        this.requiresDataContext = requiresDataContext;
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * Indicates if this property requires a data context or not.
     *
     * @return {@code true} when a data context is required.
     */
    public boolean requiresDataContext()
    {
        return requiresDataContext;
    }

    @Override
    public void accept(final Object s, final T t)
    {
        setter.ifPresent(sOptionalBiConsumer -> sOptionalBiConsumer.accept(s, t));
    }

    @Override
    public Optional<T> apply(final Object s)
    {
        return getter.flatMap(f -> Optional.ofNullable(f.apply(s)));
    }
}
