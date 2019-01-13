package com.ldtteam.blockout.util.stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class FunctionHelper
{

    private FunctionHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    @NotNull
    public static <T> Boolean alwaysTrue(@Nullable final T value)
    {
        return true;
    }

    @Nullable
    public static <K, T, U> Map.Entry<K, U> castMapValue(@Nullable final Map.Entry<K, T> entry)
    {
        if (entry == null)
            return null;

        return new Map.Entry<K, U>()
        {

            @Override
            public K getKey()
            {
                return entry.getKey();
            }

            @Override
            public U getValue()
            {
                return (U) entry.getValue();
            }

            @Override
            public U setValue(final U value)
            {
                throw new IllegalStateException("Readonly map entry");
            }
        };
    }

}