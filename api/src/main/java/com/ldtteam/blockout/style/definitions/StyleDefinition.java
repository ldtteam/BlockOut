package com.ldtteam.blockout.style.definitions;

import java.util.Collection;

public class StyleDefinition
{

    private final IIdentifier             styleId;
    private final Collection<IIdentifier> resourceTypeDefinitionLocations;

    public StyleDefinition(final IIdentifier styleId, final Collection<IIdentifier> resourceTypeDefinitions)
    {
        this.styleId = styleId;
        this.resourceTypeDefinitionLocations = resourceTypeDefinitions;
    }

    public IIdentifier getStyleId()
    {
        return styleId;
    }

    public Collection<IIdentifier> getResourceTypeDefinitionLocations()
    {
        return resourceTypeDefinitionLocations;
    }
}
