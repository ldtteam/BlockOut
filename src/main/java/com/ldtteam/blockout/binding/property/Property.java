package com.ldtteam.blockout.binding.property;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> implements BiConsumer<Object, Optional<T>>, Function<Object, Optional<T>>
{
    @NotNull
    final Optional<Function<Object, Optional<T>>>   getter;
    @NotNull
    final Optional<BiConsumer<Object, Optional<T>>> setter;

    public Property(@NotNull final Optional<Function<Object, Optional<T>>> getter, @NotNull final Optional<BiConsumer<Object, Optional<T>>> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void accept(final Object s, final Optional<T> t)
    {
        setter.ifPresent(sOptionalBiConsumer -> sOptionalBiConsumer.accept(s, t));
    }

    @Override
    public Optional<T> apply(final Object s)
    {
        return getter.flatMap(sOptionalFunction -> sOptionalFunction.apply(s));
    }
}
