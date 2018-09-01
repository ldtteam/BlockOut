package com.minecolonies.blockout.binding.property;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.minecolonies.blockout.binding.property.reflective.ReflectiveConsumer;
import com.minecolonies.blockout.binding.property.reflective.ReflectiveSupplier;
import com.minecolonies.blockout.util.Log;
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

    public static <T> Property<T> createNoneSettableFromStaticValue(@Nullable T value)
    {
        return create(c -> Optional.ofNullable(value), null);
    }

    public static <T> Property<T> createFromNonOptional(@NotNull final Function<Object, T> getter, @NotNull final BiConsumer<Object, T> setter)
    {
        return createFromNonOptional(Optional.of(getter), Optional.of(setter));
    }

    public static <T> Property<T> createFromNonOptional(@NotNull final Optional<Function<Object, T>> getter, @NotNull final Optional<BiConsumer<Object, T>> setter)
    {
        return create(getter.map(tSupplier -> s -> Optional.ofNullable(tSupplier.apply(s))),
          setter.map(tConsumer -> (s, t) -> tConsumer.accept(s, t.orElse(null))));
    }

    public static <T> Property<T> create(@NotNull final Optional<Function<Object, Optional<T>>> getter, @NotNull final Optional<BiConsumer<Object, Optional<T>>> setter)
    {
        return new Property<>(getter, setter);
    }

    public static <T> Property<T> createFromName(@NotNull final Optional<String> getSetMethodName)
    {
        return createFromName(getSetMethodName.map(name -> "get" + name), getSetMethodName.map(name -> "set" + name));
    }

    public static <T> Property<T> createFromName(
      @NotNull final Optional<String> getMethodName,
      @NotNull final Optional<String> setMethodName)
    {
        final Optional<Function<Object, Optional<T>>> getter = getMethodName.map((name) -> (Function<Object, Optional<T>>) o -> {
            final Class<?> clazz = o.getClass();
            final Optional<Method> getterMethod = getGetter(clazz, name);
            return getterMethod.map(method -> {
                method.setAccessible(true);

                try
                {
                    return (T) method.invoke(o);
                }
                catch (Exception e)
                {
                    Log.getLogger().error("Failed to reflectively access property getter.", e);
                    return null;
                }
            });
        });

        final Optional<BiConsumer<Object, Optional<T>>> setter = setMethodName.map((name) -> (BiConsumer<Object, Optional<T>>) (o, t) -> {
            final Class<?> clazz = o.getClass();
            final Optional<Method> settterMethod = getSetter(clazz, name);
            settterMethod.ifPresent(method -> {
                method.setAccessible(true);

                try
                {
                    method.invoke(o, t);
                }
                catch (Exception e)
                {
                    Log.getLogger().error("Failed to reflectively access property setter.", e);
                }
            });
        });

        return create(getter, setter);
    }

    public static <T> Property<T> createFromMethod(@NotNull final Optional<Method> getter, @NotNull final Optional<Method> setter)
    {
        return create(new ReflectiveSupplier<>(getter), new ReflectiveConsumer<>(setter));
    }

    public static <T> Property<T> create(@Nullable final Function<Object, Optional<T>> getter, @Nullable final BiConsumer<Object, Optional<T>> setter)
    {
        return create(Optional.ofNullable(getter), Optional.ofNullable(setter));
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