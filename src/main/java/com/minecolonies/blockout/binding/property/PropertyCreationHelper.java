package com.minecolonies.blockout.binding.property;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.minecolonies.blockout.binding.property.reflective.ReflectiveConsumer;
import com.minecolonies.blockout.binding.property.reflective.ReflectiveSupplier;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class PropertyCreationHelper
{
    //Caches used to minimize heavy reflective look ups, but keep memory footprint relatively low.
    private static final Cache<Tuple<Class, String>, Optional<Method>> GETTER_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();
    private static final Cache<Tuple<Class, String>, Optional<Method>> SETTER_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    private PropertyCreationHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static <S, T> Property<S, T> createFromNonOptional(@NotNull final Function<S, T> getter, @NotNull final BiConsumer<S, T> setter)
    {
        return createFromNonOptional(Optional.of(getter), Optional.of(setter));
    }

    public static <S, T> Property<S, T> createFromNonOptional(@NotNull final Optional<Function<S, T>> getter, @NotNull final Optional<BiConsumer<S, T>> setter)
    {
        return create(getter.map(tSupplier -> s -> Optional.ofNullable(tSupplier.apply(s))),
          setter.map(tConsumer -> (s, t) -> tConsumer.accept(s, t.orElse(null))));
    }

    public static <S, T> Property<S, T> create(@NotNull final Optional<Function<S, Optional<T>>> getter, @NotNull final Optional<BiConsumer<S, Optional<T>>> setter)
    {
        return new Property<>(getter, setter);
    }

    public static <S, T> Property<S, T> create(@NotNull final Function<S, Optional<T>> getter, @NotNull final BiConsumer<S, Optional<T>> setter)
    {
        return create(Optional.of(getter), Optional.of(setter));
    }

    public static <S, T> Property<S, T> createFromMethod(@NotNull final Optional<Method> getter, @NotNull final Optional<Method> setter)
    {
        return create(new ReflectiveSupplier<>(getter), new ReflectiveConsumer<>(setter));
    }

    public static <S, T> Property<S, T> createFromName(
      @NotNull final Class<T> targetClass,
      @NotNull final Optional<String> getMethodName,
      @NotNull final Optional<String> setMethodName)
    {
        return createFromMethod(getMethodName.map(name -> getGetter(targetClass, name).orElse(null))
          , setMethodName.map(name -> getSetter(targetClass, name).orElse(null)));
    }

    public static <S, T> Property<S, T> createFromName(@NotNull final Class<T> targetClass, @NotNull final Optional<String> getSetMethodName)
    {
        return createFromName(targetClass, getSetMethodName.map(name -> "get" + name), getSetMethodName.map(name -> "set" + name));
    }

    private static Optional<Method> getGetter(@Nullable final Class<?> targetClass, @NotNull final String getMethodName)
    {
        try
        {
            return GETTER_CACHE.get(new Tuple<>(targetClass, getMethodName), () -> {
                if (targetClass == null)
                {
                    return Optional.empty();
                }

                return Optional.ofNullable(Arrays.stream(targetClass.getClass().getDeclaredMethods())
                                             .filter(method -> method.getName().equals(getMethodName))
                                             .filter(method -> method.getParameterCount() == 0)
                                             .findFirst()
                                             .orElse(getGetter(targetClass.getSuperclass(), getMethodName).orElse(null)));
            });
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Failed to get a getter for: " + getMethodName, e);
        }
    }

    private static Optional<Method> getSetter(@Nullable final Class<?> targetClass, @NotNull final String setMethodName)
    {
        try
        {
            return SETTER_CACHE.get(new Tuple<>(targetClass, setMethodName), () -> {
                if (targetClass == null)
                {
                    return Optional.empty();
                }

                return Optional.ofNullable(Arrays.stream(targetClass.getClass().getDeclaredMethods())
                                             .filter(method -> method.getName().equals(setMethodName))
                                             .filter(method -> method.getParameterCount() == 1)
                                             .filter(method -> method.getReturnType() == Void.TYPE)
                                             .findFirst()
                                             .orElse(getGetter(targetClass.getSuperclass(), setMethodName).orElse(null)));
            });
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Failed to get a setter for: " + setMethodName, e);
        }
    }
}