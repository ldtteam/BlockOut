package com.ldtteam.blockout.binding.dependency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A interface that describes a object that handles binding to a Object of type {@link T}
 *
 * @param <T> The type that is bound too.
 */
public interface IDependencyObject<T>
{

    /**
     * Returns the value stored in the dependency object using the data context of the element, if needed.
     *
     * @param receiver The receiver with the datacontext.
     * @return The value stored in the object.
     */
    default T get(@NotNull final IDependencyReceiver receiver)
    {
        final Object dataContext;
        if (requiresDataContext())
        {
            dataContext = receiver.getDataContext();
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

    /**
     * Sets the value of this object using the receiver.
     * Querying the context from {@link IDependencyReceiver#getDataContext()} if needed.
     *
     * @param receiver The receiver that holds the context.
     * @param value    The value that this object should be set to.
     */
    default void set(@NotNull final IDependencyReceiver receiver, T value)
    {
        final Object dataContext;
        if (requiresDataContext())
        {
            dataContext = receiver.getDataContext();
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

    /**
     * Checks if the value stored by this object has changed.
     *
     * @param receiver The receiver in question.
     * @return {@code True} if changed, {@code false} otherwise.
     */
    default boolean hasChanged(@NotNull final IDependencyReceiver receiver)
    {
        final Object dataContext;
        if (requiresDataContext())
        {
            dataContext = receiver.getDataContext();
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
