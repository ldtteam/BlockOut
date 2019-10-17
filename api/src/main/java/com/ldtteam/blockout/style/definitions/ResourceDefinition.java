package com.ldtteam.blockout.style.definitions;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;

public class ResourceDefinition
{

    private final ResourceLocation id;
    private final JsonElement data;

    public ResourceDefinition(final ResourceLocation id, final JsonElement data)
    {
        this.id = id;
        this.data = data;
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public JsonElement getData()
    {
        return data;
    }
}
