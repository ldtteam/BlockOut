package com.minecolonies.blockout.style.simple;

import com.google.common.collect.ImmutableMap;
import com.minecolonies.blockout.style.core.IStyle;
import com.minecolonies.blockout.style.core.resources.core.IResource;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleStyle implements IStyle
{
    private final ResourceLocation                 id;
    private final Map<ResourceLocation, IResource> resources;

    public SimpleStyle(final ResourceLocation id, final Map<ResourceLocation, IResource> resources)
    {
        this.id = id;
        this.resources = resources;
    }

    /**
     * Returns the id of the {@link IStyle}.
     *
     * @return the id of the {@link IStyle}.
     */
    @NotNull
    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    /**
     * All known {@link IResource} to this {@link IStyle}.
     *
     * @return all known {@link IResource}.
     */
    @NotNull
    @Override
    public ImmutableMap<ResourceLocation, IResource> getResources()
    {
        return ImmutableMap.copyOf(resources);
    }
}
