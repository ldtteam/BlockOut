package com.minecolonies.blockout.style.core.resources.core;

import net.minecraft.util.ResourceLocation;
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
    ResourceLocation getDiskLocation();
}
