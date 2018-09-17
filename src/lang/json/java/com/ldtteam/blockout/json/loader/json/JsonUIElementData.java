package com.ldtteam.blockout.json.loader.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.json.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.json.loader.core.IUIElementData;
import com.ldtteam.blockout.json.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.json.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public class JsonUIElementData implements IUIElementData
{
    @NotNull
    private final JsonObject object;
    @NotNull
    private final IUIElementMetaData metaData;
    @NotNull
    private IBindingEngine currentBindingEngine;

    public JsonUIElementData(
      @NotNull final JsonObject object,
      @Nullable IUIElementHost parent,
      @NotNull final IBindingEngine currentBindingEngine)
    {
        this.object = object;
        this.currentBindingEngine = currentBindingEngine;
        this.metaData = new JsonUIElementMetaData(object, parent);
    }

    @NotNull
    @Override
    public IBindingEngine getBindingEngine()
    {
        return currentBindingEngine;
    }

    @Override
    public void setBindingEngine(@NotNull final IBindingEngine bindingEngine)
    {
        this.currentBindingEngine = bindingEngine;
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
      @NotNull final Property<T> defaultProperty,
      @Nullable final T defaultValue)
    {
        if (!object.has(name))
            DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue);

        final JsonElement element = object.get(name);
        final IUIElementDataComponent targetComponent = new JsonUIElementDataComponent(element, currentBindingEngine);

        return currentBindingEngine.attemptBind(targetComponent, defaultValue).orElseGet(() -> {
            if (!typeMatcher.test(targetComponent))
                return DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue);

            return DependencyObjectHelper.createFromValue(componentConverter.apply(targetComponent));
        });
    }
}
