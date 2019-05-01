package com.ldtteam.blockout.builder.data.builder;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.core.builder.IBlockOutUIElementConstructionDataBuilder;
import com.ldtteam.blockout.builder.data.BlockOutGuiConstructionData;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
      @NotNull final IEventHandler<? super S, ? super A> eventHandler)
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
    public <T extends IUIElement, B extends IBlockOutUIElementConstructionDataBuilder<B, T>> IBlockOutGuiConstructionDataBuilder withControl(
      @NotNull final String controlId,
      @NotNull final Class<B> builderClass,
      @NotNull final Consumer<B> builderInstanceConsumer)
    {
        try
        {
            final Constructor<B> constructor = builderClass.getConstructor(String.class, IBlockOutGuiConstructionDataBuilder.class);
            final B builder = constructor.newInstance(controlId, this);

            builderInstanceConsumer.accept(builder);

            return this;
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("The given builder class does not have a constructor which takes a ControlId and this builder!", ex);
        }
    }

    @Override
    public IBlockOutGuiConstructionDataBuilder copyFrom(@NotNull final IBlockOutGuiConstructionData data)
    {
        this.dependencyData.putAll(data.getDependencyData());
        this.eventHandlerData.putAll(data.getEventHandlerData());

        return this;
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionData build()
    {
        return new BlockOutGuiConstructionData(dependencyData, eventHandlerData);
    }
}
