package com.minecolonies.blockout.style.definitions;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class StyleDefinition
{

    private final ResourceLocation             styleId;
    private final Collection<ResourceLocation> resourceTypeDefinitionLocations;

    public StyleDefinition(final ResourceLocation styleId, final Collection<ResourceLocation> resourceTypeDefinitions)
    {
        this.styleId = styleId;
        this.resourceTypeDefinitionLocations = resourceTypeDefinitions;
    }

    public ResourceLocation getStyleId()
    {
        return styleId;
    }

    public Collection<ResourceLocation> getResourceTypeDefinitionLocations()
    {
        return resourceTypeDefinitionLocations;
    }
}
