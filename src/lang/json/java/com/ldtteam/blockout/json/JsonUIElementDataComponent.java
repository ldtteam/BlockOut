package com.ldtteam.blockout.json;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Injector;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.json.util.JSONStreamSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUIElementDataComponent implements IUIElementDataComponent
{
    @NotNull
    private JsonElement element;
    @NotNull
    private final Injector    injector;

    JsonUIElementDataComponent(@NotNull final JsonElement element, @NotNull final Injector injector)
    {
        this.element = element;
        this.injector = injector;
    }

    @Override
    public String getAsString()
    {
        return element.isJsonPrimitive() ? element.getAsString() : "";
    }

    @Override
    public void setString(@NotNull final String string) throws IllegalArgumentException
    {
        if (!element.isJsonPrimitive())
            throw new IllegalArgumentException("Can not set string.");

        element = new JsonPrimitive(string);
    }

    @Override
    public Boolean getAsBoolean()
    {
        return element.getAsBoolean();
    }

    @Override
    public void setBoolean(@NotNull final Boolean bool) throws IllegalArgumentException
    {
        if (!element.isJsonPrimitive())
            throw new IllegalArgumentException("Can not set boolean.");

        element = new JsonPrimitive(bool);
    }

    @Override
    public void setInteger(@NotNull final Integer integer) throws IllegalArgumentException
    {
        if (!element.isJsonPrimitive())
            throw new IllegalArgumentException("Can not set integer.");

        element = new JsonPrimitive(integer);
    }

    @Override
    public Double getAsDouble()
    {
        return element.getAsDouble();
    }

    @Override
    public void setDouble(@NotNull final Double d) throws IllegalArgumentException
    {
        if (!element.isJsonPrimitive())
            throw new IllegalArgumentException("Can not set double.");

        element = new JsonPrimitive(d);
    }

    @Override
    public void setFloat(@NotNull final Float f) throws IllegalArgumentException
    {
        if (!element.isJsonPrimitive())
            throw new IllegalArgumentException("Can not set float.");

        element = new JsonPrimitive(f);
    }

    @Override
    public List<IUIElementDataComponent> getAsList()
    {
        return JSONStreamSupport.streamChildData(element).map(element1 -> new JsonUIElementDataComponent(element1, injector)).collect(Collectors.toList());
    }

    @Override
    public void setList(@NotNull final List<IUIElementDataComponent> list) throws IllegalArgumentException
    {
        if (!element.isJsonArray())
            throw new IllegalArgumentException("Can not set list.");

        element = new JsonArray();
        list.stream()
          .filter(e -> e instanceof JsonUIElementDataComponent)
          .forEach(e -> ((JsonArray) element).add(((JsonUIElementDataComponent) e).element));
    }

    @Override
    public Map<String, IUIElementDataComponent> getAsMap()
    {
        return JSONStreamSupport.streamObject(element)
          .collect(Collectors.toMap(Map.Entry::getKey, e -> new JsonUIElementDataComponent(e.getValue(), injector)));
    }

    @Override
    public void setMap(@NotNull final Map<String, IUIElementDataComponent> map) throws IllegalArgumentException
    {
        if (!element.isJsonObject())
            throw new IllegalArgumentException("Can not set map");

        element = new JsonObject();

        map.entrySet().stream()
          .filter(e -> e.getValue() instanceof JsonUIElementDataComponent)
          .forEach(e -> ((JsonObject) element).add(e.getKey(), ((JsonUIElementDataComponent) e.getValue()).element));
    }

    @Override
    public IUIElementData toIUIElementData(@Nullable final IUIElementHost parent)
    {
        if (!isComplex())
            throw new IllegalStateException("Need complex Json Object for UIElementData.");

        return new JsonUIElementData(element.getAsJsonObject(), parent, injector);
    }

    @Override
    public ComponentType getType()
    {
        if (element.isJsonObject())
            return ComponentType.COMPLEX;

        if (element.isJsonArray())
            return ComponentType.LIST;

        if (element.isJsonPrimitive())
        {
            final JsonPrimitive primitive = (JsonPrimitive) element;

            if (primitive.isBoolean())
                return ComponentType.BOOL;

            if (primitive.isNumber())
                return ComponentType.NUMBER;

            return ComponentType.STRING;
        }

        return ComponentType.UNKNOWN;
    }
}
