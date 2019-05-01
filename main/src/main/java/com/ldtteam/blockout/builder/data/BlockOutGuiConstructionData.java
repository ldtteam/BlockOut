package com.ldtteam.blockout.builder.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Proxy;
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

        Map<Class<?>, List<IEventHandler<?, ?>>> eventHandlerMapOfSourceTypeForId = eventHandlerData.get(id).get(sourceClass);
        Map<Class<? super A>, List<IEventHandler<S, A>>> eventHandlerTargetTypeMap = Maps.newHashMap();

        eventHandlerMapOfSourceTypeForId.keySet().forEach(candidateArgumentType -> {
            if (IProxy.getInstance().getReflectionManager().getAllSupers(argumentClass).contains(candidateArgumentType))
            {
                final Class<? super A> argumentTypeSuper = (Class<? super A>) candidateArgumentType;
                final List<IEventHandler<S, A>> eventHandlers = eventHandlerMapOfSourceTypeForId.get(candidateArgumentType)
                    .stream()
                    .map(unwrappedType -> (IEventHandler<S, A>) unwrappedType::handleWithCast)
                  .collect(Collectors.toList());

                eventHandlerTargetTypeMap.put(argumentTypeSuper, eventHandlers);
            }
        });

        return ImmutableList.copyOf(eventHandlerTargetTypeMap.entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toList()));
    }

    public Map<String, IDependencyObject<?>> getDependencyData()
    {
        return dependencyData;
    }

    public Map<String, Map<Class<?>, Map<Class<?>, List<IEventHandler<?, ?>>>>> getEventHandlerData()
    {
        return eventHandlerData;
    }

    @Override
    public boolean hasDependencyData(@NotNull final String name)
    {
        return dependencyData.containsKey(name);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T> IDependencyObject<T> get(
      @NotNull final String name)
    {
        if (!hasDependencyData(name))
        {
            throw new IllegalArgumentException("No known dependency data available for the requested name and type.");
        }

        return (IDependencyObject<T>) dependencyData.get(name);
    }
}
