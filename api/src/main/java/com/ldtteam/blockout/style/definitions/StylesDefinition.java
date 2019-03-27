package com.ldtteam.blockout.style.definitions;

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
