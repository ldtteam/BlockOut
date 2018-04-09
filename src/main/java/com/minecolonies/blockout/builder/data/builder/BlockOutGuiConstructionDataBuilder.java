package com.minecolonies.blockout.builder.data.builder;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.builder.core.builder.IBlockOutUIElementConstructionDataBuilder;
import com.minecolonies.blockout.builder.data.BlockOutGuiConstructionData;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockOutGuiConstructionDataBuilder implements IBlockOutGuiConstructionDataBuilder
{
    private final Map<String, IDependencyObject<?>>                                    dependencyData   = new HashMap<>();
    private final Map<String, Map<Class<?>, Map<Class<?>, List<IEventHandler<?, ?>>>>> eventHandlerData = new HashMap<>();

    @NotNull
    @Override
    public IBlockOutGuiConstructionDataBuilder withDependency(
      @NotNull final String controlId, @NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency)
    {
        dependencyData.put(String.format("%s#%s", controlId, fieldName), dependency);
        return this;
    }

    @NotNull
    @Override
    public <S, A> IBlockOutGuiConstructionDataBuilder withEventHandler(
      @NotNull final String controlId,
      @NotNull final String eventName,
      @NotNull final Class<S> controlTypeClass,
      @NotNull final Class<A> argumentTypeClass,
      @NotNull final IEventHandler<S, A> eventHandler)
    {
        final String eventId = String.format("%s#%s", controlId, eventName);

        if (!eventHandlerData.containsKey(eventId))
        {
            eventHandlerData.put(eventId, new HashMap<>());
        }

        if (!eventHandlerData.get(eventId).containsKey(controlTypeClass))
        {
            eventHandlerData.get(eventId).put(controlTypeClass, new HashMap<>());
        }

        if (!eventHandlerData.get(eventId).get(controlTypeClass).containsKey(argumentTypeClass))
        {
            eventHandlerData.get(eventId).get(controlTypeClass).put(argumentTypeClass, new ArrayList<>());
        }

        eventHandlerData.get(eventId).get(controlTypeClass).get(argumentTypeClass).add(eventHandler);
        return this;
    }

    @NotNull
    @Override
    public <T extends IUIElement, B extends IBlockOutUIElementConstructionDataBuilder<B, T>> B withControl(
      @NotNull final String controlId, @NotNull final Class<B> builderClass)
    {
        try
        {
            final Constructor<B> constructor = builderClass.getConstructor(String.class, IBlockOutGuiConstructionDataBuilder.class);
            return constructor.newInstance(controlId, this);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("The given builder class does not have a constructor which takes a ControlId and this builder!", ex);
        }
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionData build()
    {
        return new BlockOutGuiConstructionData(dependencyData, eventHandlerData);
    }
}
