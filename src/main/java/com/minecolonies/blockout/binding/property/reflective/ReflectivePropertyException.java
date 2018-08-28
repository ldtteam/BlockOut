package com.minecolonies.blockout.binding.property.reflective;

import com.minecolonies.blockout.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Optional;

public class ReflectivePropertyException extends RuntimeException
{
    private static final long serialVersionUID = Constants.SERIAL_VAR_ID;

    public ReflectivePropertyException(@NotNull final Object target, @NotNull final Optional<Method> method, @NotNull final String actionMessage)
    {
        super("Failed to " + actionMessage + " from: " + target.getClass().getName() + " using: " + method.map(Method::getName).orElse("<NOT EXISTING>"));
    }

    public ReflectivePropertyException(@NotNull final Object target, @NotNull final Optional<Method> method, @NotNull final String actionMessage, @NotNull final Throwable cause)
    {
        super("Failed to " + actionMessage + " from: " + target.getClass().getName() + " using: " + method.map(Method::getName).orElse("<NOT EXISTING>"), cause);
    }
}
