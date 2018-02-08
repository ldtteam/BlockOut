package com.minecolonies.blockout.binding.property;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<S, T> implements BiConsumer<S, Optional<T>>, Function<S, Optional<T>>
{
    @NotNull
    final Optional<Function<S, Optional<T>>>   getter;
    @NotNull
    final Optional<BiConsumer<S, Optional<T>>> setter;

    public Property(@NotNull final Optional<Function<S, Optional<T>>> getter, @NotNull final Optional<BiConsumer<S, Optional<T>>> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void accept(final S s, final Optional<T> t)
    {
        setter.ifPresent(sOptionalBiConsumer -> sOptionalBiConsumer.accept(s, t));
    }

    @Override
    public Optional<T> apply(final S s)
    {
        return getter.flatMap(sOptionalFunction -> sOptionalFunction.apply(s));
    }
}
