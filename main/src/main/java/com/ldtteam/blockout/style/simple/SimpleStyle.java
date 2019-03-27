package com.ldtteam.blockout.style.simple;

import com.google.common.collect.ImmutableMap;
import com.ldtteam.blockout.style.core.IStyle;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleStyle implements IStyle
{
    private final IIdentifier                 id;
    private final Map<IIdentifier, IResource> resources;

    public SimpleStyle(final IIdentifier id, final Map<IIdentifier, IResource> resources)
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
    public IIdentifier getId()
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
    public ImmutableMap<IIdentifier, IResource> getResources()
    {
        return ImmutableMap.copyOf(resources);
    }
}
