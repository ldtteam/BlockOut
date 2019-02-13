package com.ldtteam.blockout.style.core.resources.core;

import com.ldtteam.minelaunch.util.IIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link IResource} that exists on disk.
 */
public interface IDiskResource extends IResource
{

    /**
     * Returns the location of the {@link IDiskResource} on disk.
     *
     * @return The location of the {@link IDiskResource} on disk.
     */
    @NotNull
    IIdentifier getDiskLocation();
}
