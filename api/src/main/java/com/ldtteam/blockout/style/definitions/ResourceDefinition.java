package com.ldtteam.blockout.style.definitions;

import com.google.gson.JsonElement;

public class ResourceDefinition
{

    private final IIdentifier id;
    private final JsonElement data;

    public ResourceDefinition(final IIdentifier id, final JsonElement data)
    {
        this.id = id;
        this.data = data;
    }

    public IIdentifier getId()
    {
        return id;
    }

    public JsonElement getData()
    {
        return data;
    }
}
