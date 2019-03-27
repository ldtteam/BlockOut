package com.ldtteam.blockout.util.reflection;

import com.google.inject.internal.MoreTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ReflectionUtil
{
    private ReflectionUtil()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static final ParameterizedType createParameterizedType(@Nullable final Type outerType, @NotNull Type targetGenericType, @NotNull final Type... genericArgumentType)
    {
        return new MoreTypes.ParameterizedTypeImpl(outerType, targetGenericType, genericArgumentType);
    }
}