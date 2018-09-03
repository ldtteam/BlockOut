package com.minecolonies.blockout.util.parsing;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class ParsingUtils
{

    private ParsingUtils()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static <T> boolean isParseable(@NotNull final String string, @NotNull final Function<String, T> converter)
    {
        try
        {
            converter.apply(string);
        }
        catch (Exception ignored)
        {
            return false;
        }

        return true;
    }
}