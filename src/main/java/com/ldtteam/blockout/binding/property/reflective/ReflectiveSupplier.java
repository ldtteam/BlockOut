package com.ldtteam.blockout.binding.property.reflective;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

public class ReflectiveSupplier<S, T> implements Function<S, Optional<T>>
{
    @NotNull
    private final Optional<Method> getter;

    public ReflectiveSupplier(@NotNull final Optional<Method> getter)
    {
        this.getter = getter;

        this.getter.ifPresent(method -> method.setAccessible(true));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> apply(final S s)
    {
        try
        {
            return getter.map(method -> {
                try
                {
                    return (T) method.invoke(s);
                }
                catch (Exception e)
                {
                    throw new ReflectivePropertyException(s, getter, "get", e);
                }
            });
        }
        catch (Exception e)
        {
            throw new ReflectivePropertyException(s, getter, "get", e);
        }
    }
}
