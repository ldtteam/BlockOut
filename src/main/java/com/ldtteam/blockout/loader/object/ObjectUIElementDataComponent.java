package com.ldtteam.blockout.loader.object;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectUIElementDataComponent implements IUIElementDataComponent, Serializable
{
    private Serializable serializable;

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
    public void setString(@NotNull final String string) throws IllegalArgumentException
    {
        serializable = string;
    }

    @Override
    public Boolean getAsBoolean()
    {
        return Boolean.parseBoolean(serializable.toString());
    }

    @Override
    public void setBoolean(@NotNull final Boolean bool) throws IllegalArgumentException
    {
        serializable = bool.toString();
    }

    @Override
    public void setInteger(@NotNull final Integer integer) throws IllegalArgumentException
    {
        serializable = integer.toString();
    }

    @Override
    public Double getAsDouble()
    {
        return Double.parseDouble(serializable.toString());
    }

    @Override
    public void setDouble(@NotNull final Double d) throws IllegalArgumentException
    {
        serializable = d.toString();
    }

    @Override
    public void setFloat(@NotNull final Float f) throws IllegalArgumentException
    {
        serializable = f.toString();
    }

    @Override
    public List<IUIElementDataComponent> getAsList()
    {
        return (List<IUIElementDataComponent>) serializable;
    }

    @Override
    public void setList(@NotNull final List<IUIElementDataComponent> list) throws IllegalArgumentException
    {
        serializable = (Serializable) list.stream().filter(e -> e instanceof ObjectUIElementDataComponent).collect(Collectors.toList());
    }

    @Override
    public Map<String, IUIElementDataComponent> getAsMap()
    {
        return (Map<String, IUIElementDataComponent>) serializable;
    }

    @Override
    public void setMap(@NotNull final Map<String, IUIElementDataComponent> map) throws IllegalArgumentException
    {
        serializable = (Serializable) map.entrySet().stream().filter(e -> e.getValue() instanceof ObjectUIElementDataComponent).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public IUIElementData toIUIElementData(@Nullable final IUIElementHost parent)
    {
        return new ObjectUIElementData(object, metaData, injector);
    }

    @Override
    public ComponentType getType()
    {
        return null;
    }
}
