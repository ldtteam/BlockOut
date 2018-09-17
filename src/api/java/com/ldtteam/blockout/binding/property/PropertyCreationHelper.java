package com.ldtteam.blockout.binding.property;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ldtteam.blockout.util.Log;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class PropertyCreationHelper
{
    //Caches used to minimize heavy reflective look ups, but keep memory footprint relatively low.
    private static final Cache<Tuple<Class, String>, Optional<Tuple<MethodAccess, Integer>>> GETTER_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();
    private static final Cache<Tuple<Class, String>, Optional<Tuple<MethodAccess, Integer>>>       SETTER_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    private PropertyCreationHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static <T> Property<T> createNoneSettableFromStaticValue(@Nullable T value)
    {
        return create(c -> Optional.ofNullable(value), null, false);
    }

    public static <T> Property<T> createFromNonOptional(@NotNull final Function<Object, T> getter, @NotNull final BiConsumer<Object, T> setter, @NotNull final boolean requiresDataContext)
    {
        return createFromNonOptional(Optional.of(getter), Optional.of(setter), requiresDataContext);
    }

    public static <T> Property<T> createFromNonOptional(@NotNull final Optional<Function<Object, T>> getter, @NotNull final Optional<BiConsumer<Object, T>> setter, @NotNull final boolean requiresDataContext)
    {
        return create(getter.map(tSupplier -> s -> Optional.ofNullable(tSupplier.apply(s))),
          setter.map(tConsumer -> (s, t) -> tConsumer.accept(s, t.orElse(null))),
          requiresDataContext);
    }

    public static <T> Property<T> create(@NotNull final Optional<Function<Object, Optional<T>>> getter, @NotNull final Optional<BiConsumer<Object, Optional<T>>> setter, @NotNull final boolean requiresDataContext)
    {
        return new Property<T>(getter, setter, requiresDataContext);
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
            final Optional<Tuple<MethodAccess, Integer>> getterMethod = getGetter(clazz, name);
            return getterMethod.map(method -> {
                try
                {
                    return (T) method.getFirst().invoke(o, method.getSecond());
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
            final Optional<Tuple<MethodAccess, Integer>> settterMethod = getSetter(clazz, name);
            settterMethod.ifPresent(method -> {
                try
                {
                    method.getFirst().invoke(o, method.getSecond(), t);
                }
                catch (Exception e)
                {
                    Log.getLogger().error("Failed to reflectively access property setter.", e);
                }
            });
        });

        return create(getter, setter, true);
    }

    public static <T> Property<T> create(@Nullable final Function<Object, Optional<T>> getter, @Nullable final BiConsumer<Object, Optional<T>> setter, @NotNull final boolean requiresDataContext)
    {
        return create(Optional.ofNullable(getter), Optional.ofNullable(setter), requiresDataContext);
    }

    private static Optional<Tuple<MethodAccess, Integer>> getGetter(@Nullable final Class<?> targetClass, @NotNull final String getMethodName)
    {
        if (targetClass == null)
        {
            return Optional.empty();
        }

        try
        {
            return GETTER_CACHE.get(new Tuple<>(targetClass, getMethodName), () -> {
                try
                {
                    final MethodAccess methodAccess = MethodAccess.get(targetClass);
                    final Integer accessIndex = methodAccess.getIndex(getMethodName);

                    return Optional.of(new Tuple<>(methodAccess, accessIndex));
                }
                catch (Exception ex)
                {
                    return Optional.empty();
                }
            });
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Failed to get a getter for: " + getMethodName, e);
        }
    }

    private static Optional<Tuple<MethodAccess, Integer>> getSetter(@Nullable final Class<?> targetClass, @NotNull final String setMethodName)
    {
        if (targetClass == null)
        {
            return Optional.empty();
        }

        try
        {
            return SETTER_CACHE.get(new Tuple<>(targetClass, setMethodName), () -> {
                try
                {
                    final MethodAccess methodAccess = MethodAccess.get(targetClass);
                    final Integer accessIndex = methodAccess.getIndex(setMethodName, 1);

                    return Optional.of(new Tuple<>(methodAccess, accessIndex));
                }
                catch (Exception ex)
                {
                    return Optional.empty();
                }
            });
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Failed to get a setter for: " + setMethodName, e);
        }
    }
}