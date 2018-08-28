package com.minecolonies.blockout.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IEventHandler<S, A>
{
    void handle(@NotNull final S source, @Nullable final A args);
}
