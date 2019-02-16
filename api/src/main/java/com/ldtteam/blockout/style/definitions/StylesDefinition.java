package com.ldtteam.blockout.style.definitions;

import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;

import java.util.Collection;

public class StylesDefinition
{

    private final Collection<IIdentifier> styleLocations;

    public StylesDefinition(final Collection<IIdentifier> styleLocations) {this.styleLocations = styleLocations;}

    public Collection<IIdentifier> getStyleLocations()
    {
        return styleLocations;
    }
}
