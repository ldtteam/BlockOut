package com.ldtteam.blockout.json.loader.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.json.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.json.loader.core.IUIElementData;
import com.ldtteam.blockout.json.loader.core.component.ComponentType;
import com.ldtteam.blockout.json.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.json.util.JSONStreamSupport;
import com.ldtteam.blockout.json.util.JSONToNBT;
import net.minecraft.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class JsonUIElementDataComponent implements IUIElementDataComponent
{
    @NotNull
    private final JsonElement element;
    @NotNull
    private final IBindingEngine currentBindingEngine;

    JsonUIElementDataComponent(@NotNull final JsonElement element, @NotNull final IBindingEngine currentBindingEngine)
    {
        this.element = element;
        this.currentBindingEngine = currentBindingEngine;
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
    public Double getAsDouble()
    {
        return element.getAsDouble();
    }

    @Override
    public List<IUIElementDataComponent> getAsList()
    {
        return JSONStreamSupport.streamChildData(element).map(e -> new JsonUIElementDataComponent(e, currentBindingEngine)).collect(Collectors.toList());
    }

    @Override
    public NBTBase getAsNBT()
    {
        return JSONToNBT.fromJSON(element);
    }

    @Override
    public IUIElementData toIUIElementData(@Nullable final IUIElementHost parent)
    {
        if (!isComplex())
            throw new IllegalStateException("Need complex Json Object for UIElementData.");

        return new JsonUIElementData(element.getAsJsonObject(), parent, currentBindingEngine);
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
