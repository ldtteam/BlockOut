package com.ldtteam.blockout.lang.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.lang.json.util.JSONStreamSupport;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUIElementDataComponent implements IUIElementDataComponent
{
    @NotNull
    private JsonElement element;

    JsonUIElementDataComponent(@NotNull final JsonElement element)
    {
        this.element = element;
    }

    @Override
    public String getAsString()
    {
        return element.isJsonPrimitive() ? element.getAsString() : "";
    }

    @Override
    public Boolean getAsBoolean()
    {
        return element.getAsBoolean();
    }

    @Override
    public void setBoolean(@NotNull final Boolean bool) throws IllegalArgumentException
    {
        element = new JsonPrimitive(bool);
    }

    @Override
    public Integer getAsInteger()
    {
        return element.getAsInt();
    }

    @Override
    public void setInteger(@NotNull final Integer integer) throws IllegalArgumentException
    {
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
        element = new JsonPrimitive(d);
    }

    @Override
    public Float getAsFloat()
    {
        return element.getAsFloat();
    }

    @Override
    public void setFloat(@NotNull final Float f) throws IllegalArgumentException
    {
        element = new JsonPrimitive(f);
    }

    @Override
    public List<IUIElementDataComponent> getAsList()
    {
        return JSONStreamSupport.streamChildData(element).map(element1 -> new JsonUIElementDataComponent(element1)).collect(Collectors.toList());
    }

    @Override
    public Map<String, IUIElementDataComponent> getAsMap()
    {
        return JSONStreamSupport.streamObject(element)
                 .collect(Collectors.toMap(Map.Entry::getKey, e -> new JsonUIElementDataComponent(e.getValue())));
    }

    @Override
    public void setMap(@NotNull final Map<String, ? extends IUIElementDataComponent> map) throws IllegalArgumentException
    {
        element = new JsonObject();

        map.entrySet().stream()
          .filter(e -> e.getValue() instanceof JsonUIElementDataComponent)
          .forEach(e -> ((JsonObject) element).add(e.getKey(), ((JsonUIElementDataComponent) e.getValue()).element));
    }

    @Override
    public IUIElementData<?> toIUIElementData(@Nullable final IUIElementHost parent)
    {
        if (!isComplex())
        {
            throw new IllegalStateException("Need complex Json Object for UIElementData.");
        }

        return new JsonUIElementData(element.getAsJsonObject(), parent);
    }

    @Override
    public void setString(@NotNull final String string) throws IllegalArgumentException
    {
        element = new JsonPrimitive(string);
    }

    @Override
    public ComponentType getType()
    {
        if (element.isJsonObject())
        {
            return ComponentType.COMPLEX;
        }

        if (element.isJsonArray())
        {
            return ComponentType.LIST;
        }

        if (element.isJsonPrimitive())
        {
            final JsonPrimitive primitive = (JsonPrimitive) element;

            if (primitive.isBoolean())
            {
                return ComponentType.BOOL;
            }

            if (primitive.isNumber())
            {
                return ComponentType.NUMBER;
            }

            return ComponentType.STRING;
        }

        return ComponentType.UNKNOWN;
    }

    @Override
    public void setList(@NotNull final List<? extends IUIElementDataComponent> list) throws IllegalArgumentException
    {
        element = new JsonArray();
        list.stream()
          .filter(e -> e instanceof JsonUIElementDataComponent)
          .forEach(e -> ((JsonArray) element).add(((JsonUIElementDataComponent) e).element));
    }
}
