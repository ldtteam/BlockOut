package com.ldtteam.blockout.util.reflection;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import com.google.inject.internal.MoreTypes;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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