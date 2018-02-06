package com.minecolonies.blockout.util.set;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SetUtils
{

    public static <T> boolean areAllIn(@NotNull final Set<T> values, @NotNull final Set<T> in)
    {
        return values.stream().allMatch(in::contains);
    }
}
