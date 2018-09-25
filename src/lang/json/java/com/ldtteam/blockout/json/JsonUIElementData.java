package com.ldtteam.blockout.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.json.util.JSONStreamSupport;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JsonUIElementData implements IUIElementData
{
    @NotNull
    private final JsonObject object;
    @NotNull
    private final IUIElementMetaData metaData;

    public JsonUIElementData(
      @NotNull final JsonObject object,
      @Nullable IUIElementHost parent)
    {
        this.object = object;
        this.metaData = new JsonUIElementMetaData(object, parent);
    }

    @NotNull
    @Override
    public IUIElementMetaData getMetaData()
    {
        return metaData;
    }

    @Override
    public <T> IDependencyObject<T> getFromRawDataWithProperty(
      @NotNull final String name,
      @NotNull final Predicate<IUIElementDataComponent> typeMatcher,
      @NotNull final Function<IUIElementDataComponent, T> componentConverter,
      @NotNull final IBindingEngine engine,
      @NotNull final Property<T> defaultProperty,
      @Nullable final T defaultValue)
    {
        if (!object.has(name))
            DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue);

        final JsonElement element = object.get(name);
        final IUIElementDataComponent targetComponent = new JsonUIElementDataComponent(element);

        return engine.attemptBind(targetComponent, defaultValue).orElseGet(() -> {
            if (!typeMatcher.test(targetComponent))
                return DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue);

            return DependencyObjectHelper.createFromValue(componentConverter.apply(targetComponent));
        });
    }

    @Override
    public List<IUIElementDataComponent> getComponents(@NotNull final String name)
    {
        if (!object.has(name))
            return ImmutableList.of();

        final JsonElement listCandidate = object.get(name);

        return JSONStreamSupport.streamChildData(listCandidate).map(JsonUIElementDataComponent::new).collect(Collectors.toList());
    }
}
