package com.ldtteam.blockout.style.definitions;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class StylesDefinition
{

    private final Collection<ResourceLocation> styleLocations;

    public StylesDefinition(final Collection<ResourceLocation> styleLocations) {this.styleLocations = styleLocations;}

    public Collection<ResourceLocation> getStyleLocations()
    {
        return styleLocations;
    }
}
