package com.ldtteam.blockout.binding.dependency;

import com.ldtteam.blockout.element.IUIElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A interface that describes a object that handles binding to a Object of type {@link T}
 *
 * @param <T> The type that is bound too.
 */
public interface IDependencyObject<T>
{

    default T get(@NotNull final IDependencyReceiver element)
    {
        final Object dataContext;
        if (requiresDataContext())
        {
            dataContext = element.getDataContext();
        }
        else
        {
            dataContext = null;
        }

        return get(dataContext);
    }

    /**
     * Indicates if this dependency object requires a data context or not.
     *
     * @return True if required, false if not.
     */
    default boolean requiresDataContext()
    {
        return false;
    }

    /**
     * Method used to get the value of the dependency object.
     *
     * @return The dependency object.
     */
    @Nullable
    T get(@Nullable final Object context);

    default void set(@NotNull final IUIElement element, T value)
    {
        final Object dataContext;
        if (requiresDataContext())
        {
            dataContext = element.getDataContext();
        }
        else
        {
            dataContext = null;
        }

        set(dataContext, value);
    }

    /**
     * Method used to set the value of the dependency object.
     *
     * @param value The new for the dependency object.
     */
    void set(@Nullable final Object context, @Nullable final T value);

    default boolean hasChanged(@NotNull final IDependencyReceiver element)
    {
        final Object dataContext;
        if (requiresDataContext())
        {
            dataContext = element.getDataContext();
        }
        else
        {
            dataContext = null;
        }

        return hasChanged(dataContext);
    }

    /**
     * Indicates if this {@link IDependencyObject} has changed from the last time {@link #get(Object)} was called.
     *
     * @param context The context.
     * @return True when this object has changed, false when not.
     */
    boolean hasChanged(@Nullable final Object context);
}
