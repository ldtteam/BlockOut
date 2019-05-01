package com.ldtteam.blockout.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IEventHandler<S, A>
{
    void handle(@NotNull final S source, @Nullable final A args);

    default void handleWithCast(@NotNull final Object source, @NotNull final Object args)
    {
        this.handle((S) source, (A) args);
    }
}
