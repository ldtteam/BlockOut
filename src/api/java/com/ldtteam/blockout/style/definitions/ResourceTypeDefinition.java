package com.ldtteam.blockout.style.definitions;

import java.util.Collection;

public class ResourceTypeDefinition
{

    private final String                         typeId;
    private final Collection<ResourceDefinition> resources;

    public ResourceTypeDefinition(final String typeId, final Collection<ResourceDefinition> resources)
    {
        this.typeId = typeId;
        this.resources = resources;
    }

    public String getTypeId()
    {
        return typeId;
    }

    public Collection<ResourceDefinition> getResources()
    {
        return resources;
    }
}
