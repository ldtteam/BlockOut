package com.minecolonies.blockout.util.set;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SetUtils
{

    /**
     * Utility method to check if a given {@link Set} values are all also in a different {@link Set}
     *
     * @param values The values to check.
     * @param in     The {@link Set} all the values needs to be in.
     * @param <T>    The type that is contained in the set.
     * @return {@code true} when all elements values are also in in, {@code false} otherwise.
     */
    public static <T> boolean areAllIn(@NotNull final Set<T> values, @NotNull final Set<T> in)
    {
        return values.stream().allMatch(in::contains);
    }
}
