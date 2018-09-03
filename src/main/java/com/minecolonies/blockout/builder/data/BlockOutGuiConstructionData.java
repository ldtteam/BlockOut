package com.minecolonies.blockout.builder.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockOutGuiConstructionData implements IBlockOutGuiConstructionData
{
    private final Map<String, IDependencyObject<?>>                                    dependencyData;
    private final Map<String, Map<Class<?>, Map<Class<?>, List<IEventHandler<?, ?>>>>> eventHandlerData;

    public BlockOutGuiConstructionData()
    {
        this(Maps.newHashMap(), Maps.newHashMap());
    }

    public BlockOutGuiConstructionData(
      final Map<String, IDependencyObject<?>> dependencyData,
      final Map<String, Map<Class<?>, Map<Class<?>, List<IEventHandler<?, ?>>>>> eventHandlerData)
    {
        this.dependencyData = dependencyData;
        this.eventHandlerData = eventHandlerData;

    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <S, A> List<IEventHandler<S, A>> getEventHandlers(
      @NotNull final String id, @NotNull final Class<S> sourceClass, @NotNull final Class<A> argumentClass)
    {
        if (!eventHandlerData.containsKey(id))
        {
            return ImmutableList.of();
        }

        if (!eventHandlerData.get(id).containsKey(sourceClass))
        {
            return ImmutableList.of();
        }

        if (!eventHandlerData.get(id).get(sourceClass).containsKey(argumentClass))
        {
            return ImmutableList.of();
        }

        return ImmutableList.copyOf(eventHandlerData.get(id).get(sourceClass).get(argumentClass).stream().map(e -> (IEventHandler<S, A>) e).collect(Collectors.toList()));
    }

    @Override
    public boolean hasDependencyData(@NotNull final String name, @NotNull final Class<? extends IDependencyObject> searchedType)
    {
        return dependencyData.containsKey(name) && searchedType.isInstance(dependencyData.get(name));
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T> IDependencyObject<T> get(
      @NotNull final String name, @NotNull final IDependencyObject<T> current, @NotNull final Type requestedType)
    {
        if (!hasDependencyData(name, (Class<? extends IDependencyObject>) requestedType))
        {
            throw new IllegalArgumentException("No known dependency data available for the requested name and type.");
        }

        return (IDependencyObject<T>) dependencyData.get(name);
    }

    public Map<String, IDependencyObject<?>> getDependencyData()
    {
        return dependencyData;
    }

    public Map<String, Map<Class<?>, Map<Class<?>, List<IEventHandler<?, ?>>>>> getEventHandlerData()
    {
        return eventHandlerData;
    }
}
