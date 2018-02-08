package com.minecolonies.blockout.util.reflection;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class ReflectionUtil
{

    private ReflectionUtil()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static Optional<Method> getMethod(@NotNull final Class<?> clz, @NotNull final String name)
    {
        return Arrays.stream(clz.getDeclaredMethods()).filter(method -> method.getName().equals(name)).findFirst();
    }

    public static Set<Field> getAllFields(@Nullable Class<?> clz)
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
}