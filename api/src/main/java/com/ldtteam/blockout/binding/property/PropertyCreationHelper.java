package com.ldtteam.blockout.binding.property;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ldtteam.blockout.util.Log;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Helper class that allows the easy creation of properties using several different methods.
 */
public final class PropertyCreationHelper
{
    //Caches used to minimize heavy reflective look ups, but keep memory footprint relatively low.
    private static final Cache<ITuple<Class, String>, Optional<ITuple<MethodAccess, Integer>>> GETTER_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();
    private static final Cache<ITuple<Class, String>, Optional<ITuple<MethodAccess, Integer>>> SETTER_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    private PropertyCreationHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Creates a property from a static value.
     *
     * @param value The value in question.
     * @param <T>   The type of the property and the value.
     * @return A property that references a static value.
     */
    public static <T> Property<T> createFromStaticValue(@Nullable T value)
    {
        return new StaticValueProperty<>(value);
    }

    /**
     * Creates a property from a getter and a setter directly.
     * This method allows either the getter or the setter to be null, or both.
     *
     * @param getter              The getter.
     * @param setter              The setter.
     * @param requiresDataContext {@code true} when either the getter and/or the setter require the datacontext, {@code false} when not.
     * @param <T>                 The type of the property, the returned object by the getter and the consumed object by the setter.
     * @return A property with a possible getter and setter.
     */
    public static <T> Property<T> createFromOptional(
      @Nullable final Function<Object, T> getter,
      @Nullable final BiConsumer<Object, T> setter,
      @NotNull final boolean requiresDataContext)
    {
        return create(Optional.ofNullable(getter), Optional.ofNullable(setter), requiresDataContext);
    }

    /**
     * Creates a property from an optional getter and an optional setter.
     * Allows the creator to leave out either the getter or the setter.
     *
     * @param getter The optional getter.
     * @param setter The optional setter.
     * @param requiresDataContext {@code true} when either the getter and/or the setter require the datacontext, {@code false} when not.
     * @param <T> The type of the property, the returned object by the optional getter and the consumed object by the optional setter.
     * @return A property that possibly contains a getter and/or a setter.
     */
    public static <T> Property<T> create(
      @NotNull final Optional<Function<Object, T>> getter,
      @NotNull final Optional<BiConsumer<Object, T>> setter,
      @NotNull final boolean requiresDataContext)
    {
        return new Property<>(getter, setter, requiresDataContext);
    }

    /**
     * Creates a property from an a getter and a setter.
     * As opposed to the {@link #createFromNonOptional(Function, BiConsumer, boolean)} method, this method enforces the existence of both the getter and the setter.
     *
     * @param getter The getter
     * @param setter The setter
     * @param requiresDataContext {@code true} when either the getter and/or the setter require the datacontext, {@code false} when not.
     * @param <T> The type of the property, the returned object by the getter and the consumed object by the setter.
     * @return A property with a fixed getter and setter.
     */
    public static <T> Property<T> createFromNonOptional(
      @NotNull final Function<Object, T> getter,
      @NotNull final BiConsumer<Object, T> setter,
      @NotNull final boolean requiresDataContext)
    {
        Validate.notNull(getter);
        Validate.notNull(setter);

        return create(Optional.of(getter), Optional.of(setter), requiresDataContext);
    }

    /**
     * Creates a property from a method name.
     * This property has lazy create and lookup behaviour depending on the passed in data context.
     *
     * This property will try to call the methods: {@code T get<getSetMethodName>()} and {@code void set<getSetMethodName>(T value)} using reflective ASM to generate the accessors.
     * This method needs to be public on the data context.
     *
     * Using this method, the property will always require a data context.
     *
     * @param getSetMethodName The common name of the getter and the setter.
     * @param <T> The type of the property, the returned object by the getter and the consumed object by the setter.
     * @return A property that lazily resolves the method name into a property during runtime.
     */
    public static <T> Property<T> createFromName(@NotNull final Optional<String> getSetMethodName)
    {
        return createFromName(getSetMethodName.map(name -> "get" + name), getSetMethodName.map(name -> "set" + name));
    }

    /**
     * Creates a property from a getter and setter method name
     * This property has lazy create and lookup behaviour depending on the passed in data context.
     *
     * This property will try to call the methods: {@code T <getMethodName>()} and {@code void <setMethodName>(T value)} using reflective ASM to generate the accessors.
     * This method needs to be public on the data context.
     *
     * Using this method, the property will always require a data context.
     *
     * @param getMethodName The name of the getter
     * @param setMethodName The name of the setter
     * @param <T> The type of the property, the returned object by the getter and the consumed object by the setter.
     * @return A property that lazily resolves the getter and setter method name into a property during runtime.
     */
    public static <T> Property<T> createFromName(
      @NotNull final Optional<String> getMethodName,
      @NotNull final Optional<String> setMethodName)
    {
        final Optional<Function<Object, T>> getter = getMethodName.map(name -> o -> {
            final Class<?> clazz = o.getClass();
            final Optional<ITuple<MethodAccess, Integer>> getterMethod = getGetter(clazz, name);
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
            }).orElse(null);
        });

        final Optional<BiConsumer<Object, T>> setter = setMethodName.map(name -> (o, t) -> {
            final Class<?> clazz = o.getClass();
            final Optional<ITuple<MethodAccess, Integer>> setterMethod = getSetter(clazz, name);
            setterMethod.ifPresent(method -> {
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

    private static Optional<ITuple<MethodAccess, Integer>> getGetter(@Nullable final Class<?> targetClass, @NotNull final String getMethodName)
    {
        if (targetClass == null)
        {
            return Optional.empty();
        }

        try
        {
            //TODO: Create constructor for ITuple
            /*return GETTER_CACHE.get(new Tuple<>(targetClass, getMethodName), () -> {
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
            });*/

            return null;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Failed to get a getter for: " + getMethodName, e);
        }
    }

    private static Optional<ITuple<MethodAccess, Integer>> getSetter(@Nullable final Class<?> targetClass, @NotNull final String setMethodName)
    {
        if (targetClass == null)
        {
            return Optional.empty();
        }

        try
        {
            //TODO: Introduce a constructor for ITuple
            /*return SETTER_CACHE.get(new Tuple<>(targetClass, setMethodName), () -> {
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
            });*/

            return null;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Failed to get a setter for: " + setMethodName, e);
        }
    }
}