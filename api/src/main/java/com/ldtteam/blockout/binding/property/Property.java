package com.ldtteam.blockout.binding.property;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> implements BiConsumer<Object, T>, Function<Object, Optional<T>>
{
    @NotNull
    final boolean                         requiresDataContext;
    @NotNull
    final Optional<Function<Object, T>>   getter;
    @NotNull
    final Optional<BiConsumer<Object, T>> setter;

    public Property(
      @NotNull final Optional<Function<Object, T>> getter,
      @NotNull final Optional<BiConsumer<Object, T>> setter,
      @NotNull final boolean requiresDataContext)
    {
        this.requiresDataContext = requiresDataContext;
        this.getter = getter;
        this.setter = setter;
    }

    public boolean isRequiresDataContext()
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
