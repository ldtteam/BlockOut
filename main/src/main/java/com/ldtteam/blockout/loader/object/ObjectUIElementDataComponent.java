package com.ldtteam.blockout.loader.object;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectUIElementDataComponent implements IUIElementDataComponent, Serializable
{
    private Serializable serializable;

    public ObjectUIElementDataComponent()
    {
    }

    public ObjectUIElementDataComponent(final Serializable serializable)
    {
        this.serializable = serializable;
    }

    @Override
    public String getAsString()
    {
        return serializable.toString();
    }

    @Override
    public Boolean getAsBoolean()
    {
        return (Boolean) serializable;
    }

    @Override
    public void setBoolean(@NotNull final Boolean bool) throws IllegalArgumentException
    {
        serializable = bool;
    }

    @Override
    public Integer getAsInteger()
    {
        return (Integer) serializable;
    }

    @Override
    public void setInteger(@NotNull final Integer integer) throws IllegalArgumentException
    {
        serializable = integer;
    }

    @Override
    public Double getAsDouble()
    {
        return (Double) serializable;
    }

    @Override
    public void setDouble(@NotNull final Double d) throws IllegalArgumentException
    {
        serializable = d;
    }

    @Override
    public Float getAsFloat()
    {
        return (Float) serializable;
    }

    @Override
    public void setFloat(@NotNull final Float f) throws IllegalArgumentException
    {
        serializable = f;
    }

    @Override
    public List<IUIElementDataComponent> getAsList()
    {
        return ((List<IUIElementDataComponent>) serializable);
    }

    @Override
    public Map<String, IUIElementDataComponent> getAsMap()
    {
        return ((Map<String, IUIElementDataComponent>) serializable);
    }

    @Override
    public void setMap(@NotNull final Map<String, ? extends IUIElementDataComponent> map) throws IllegalArgumentException
    {
        serializable = (Serializable) map.entrySet()
                                        .stream()
                                        .filter(e -> e.getValue() instanceof ObjectUIElementDataComponent)
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public IUIElementData<?> toIUIElementData(@Nullable final IUIElementHost parent)
    {
        final Map<String, ObjectUIElementDataComponent> map = new HashMap<>();
        getAsMap().forEach((key, value) -> map.put(key, (ObjectUIElementDataComponent) value));

        return new ObjectUIElementData(map, new ObjectUIElementMetaData(map, parent));
    }

    @Override
    public void setString(@NotNull final String string) throws IllegalArgumentException
    {
        serializable = string;
    }

    @Override
    public ComponentType getType()
    {
        if (serializable instanceof String)
        {
            return ComponentType.STRING;
        }

        if (serializable instanceof Number)
        {
            return ComponentType.NUMBER;
        }

        if (serializable instanceof Boolean)
        {
            return ComponentType.BOOL;
        }

        if (serializable instanceof List)
        {
            return ComponentType.LIST;
        }

        if (serializable instanceof Map)
        {
            return ComponentType.COMPLEX;
        }

        return ComponentType.UNKNOWN;
    }

    @Override
    public void setList(@NotNull final List<? extends IUIElementDataComponent> list) throws IllegalArgumentException
    {
        serializable = (Serializable) list.stream().filter(e -> e instanceof ObjectUIElementDataComponent).collect(Collectors.toList());
    }
}
