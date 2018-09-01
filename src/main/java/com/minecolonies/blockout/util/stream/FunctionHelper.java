package com.minecolonies.blockout.util.stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}