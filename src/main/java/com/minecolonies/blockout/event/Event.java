package com.minecolonies.blockout.event;

import com.minecolonies.blockout.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Event<S, A>
{
    private final Class<S> sourceClass;
    private final Class<A> argumentClass;
    private final List<IEventHandler<S, A>> handlers = new ArrayList<>();

    public Event(final Class<S> sourceClass, final Class<A> argumentClass)
    {
        this.sourceClass = sourceClass;
        this.argumentClass = argumentClass;
    }

    public void raise(@NotNull final S source, @Nullable final A args)
    {
        handlers.forEach(handler -> handler.handle(source, args));
    }

    public void registerHandler(final IEventHandler<?, ?> iEventHandler)
    {
        final IEventHandler<S, A> typedEventHandler;
        try
        {
            typedEventHandler = (IEventHandler<S, A>) iEventHandler;
        }
        catch (ClassCastException e)
        {
            Log.getLogger()
              .warn("Failed to register event handler. Event handler either does not take sourcetype: " + getSourceClass().getName() + " or argument type: "
                      + getArgumentClass().getName());
            return;
        }

        handlers.add(typedEventHandler);
    }

    public Class<S> getSourceClass()
    {
        return sourceClass;
    }

    public Class<A> getArgumentClass()
    {
        return argumentClass;
    }
}
