package com.ldtteam.blockout.event.injector;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.event.Event;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

public class EventHandlerInjector
{

    private EventHandlerInjector()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static void inject(@NotNull final IUIElement target, @NotNull final IEventHandlerProvider provider)
    {
        ProxyHolder.getInstance().getReflectionManager().getFieldsForClass(target.getClass())
          .stream()
          .filter(field -> field.getType().equals(Event.class))
          .forEach(eventField -> {
              final Event<?, ?> event;
              try
              {
                  event = (Event<?, ?>) eventField.get(target);
              }
              catch (IllegalAccessException e)
              {
                  Log.getLogger().error("Failed to get event instance. Needs to be either protected, public, or package private. Private field is not supported.");
                  return;
              }
              provider.getEventHandlers(String.format("%s#%s", target.getId(), eventField.getName()), event.getSourceClass(), event.getArgumentClass())
                .forEach(event::registerHandler);
          });
    }
}
