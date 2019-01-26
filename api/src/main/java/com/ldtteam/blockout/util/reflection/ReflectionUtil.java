package com.ldtteam.blockout.util.reflection;

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

    private static final Cache<Class<?>, Set<Field>> FIELD_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    private ReflectionUtil()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static Optional<Method> getMethod(@NotNull final Class<?> clz, @NotNull final String name)
    {
        return Arrays.stream(clz.getDeclaredMethods()).filter(method -> method.getName().equals(name)).findFirst();
    }

    private static Set<Field> getAllFields(@Nullable Class<?> clz)
    {
        if (clz == null)
        {
            return ImmutableSet.of();
        }

        final ImmutableSet.Builder<Field> fieldBuilder = ImmutableSet.builder();

        fieldBuilder.add(clz.getDeclaredFields());
        fieldBuilder.addAll(getAllFields(clz.getSuperclass()));

        return fieldBuilder.build();
    }

    public static final Set<Field> getFields(@NotNull final Class<?> targetClass)
    {
        try
        {
            return FIELD_CACHE.get(targetClass, () -> getAllFields(targetClass));
        }
        catch (ExecutionException e)
        {
            Log.getLogger().error("Failed to retrieve fields from: " + targetClass.getName(), e);
            return ImmutableSet.of();
        }
    }

    public static final ParameterizedType createParameterizedType(@Nullable final Type outerType, @NotNull Type targetGenericType, @NotNull final Type... genericArgumentType)
    {
        return new MoreTypes.ParameterizedTypeImpl(outerType, targetGenericType, genericArgumentType);
    }
}