package com.ldtteam.blockout.style.core.resources.core;

import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a resource in BlockOut.
 */
public interface IResource
{

    /**
     * Returns the ID of the {@link IResource}.
     *
     * @return The ID of the {@link IResource}.
     */
    @NotNull
    IIdentifier getId();
}
