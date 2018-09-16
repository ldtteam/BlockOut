package com.ldtteam.blockout.binding.property.reflective;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ReflectiveConsumer<S, T> implements BiConsumer<S, Optional<T>>
{

    @NotNull
    private final Optional<Method> setter;

    public ReflectiveConsumer(@NotNull final Optional<Method> setter)
    {
        this.setter = setter;

        this.setter.ifPresent(method -> method.setAccessible(true));
    }

    @Override
    public void accept(final S s, final Optional<T> t)
    {
        try
        {
            setter.ifPresent(tConsumer -> {
                try
                {
                    tConsumer.invoke(s, t.orElse(null));
                }
                catch (Exception e)
                {
                    throw new ReflectivePropertyException(s, setter, "set", e);
                }
            });
        }
        catch (Exception ex)
        {
            throw new ReflectivePropertyException(s, setter, "set", ex);
        }
    }
}
