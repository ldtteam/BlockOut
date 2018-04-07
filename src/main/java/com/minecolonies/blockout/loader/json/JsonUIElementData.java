package com.minecolonies.blockout.loader.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonUIElementData implements IUIElementData
{
    private final JsonObject     object;
    private final IUIElementHost parent;

    public JsonUIElementData(final JsonObject object, final IUIElementHost parent) {this.object = object;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(object.get("type").getAsString());
    }

    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return parent;
    }

    @Nullable
    @Override
    public List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren)
    {
        if (!object.has("children"))
        {
            return ImmutableList.of();
        }

        return StreamSupport.stream(object.get("children").getAsJsonArray().spliterator(), false)
                 .filter(JsonElement::isJsonObject)
                 .map(JsonElement::getAsJsonObject)
                 .map(childData -> new JsonUIElementData(childData, parentOfChildren))
                 .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public String getStringAttribute(@NotNull final String name, @NotNull final String def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsString();
    }

    @Override
    public int getIntegerAttribute(@NotNull final String name, final int def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsInt();
    }

    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsFloat();
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsDouble();
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name, final boolean def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsBoolean();
    }
}
