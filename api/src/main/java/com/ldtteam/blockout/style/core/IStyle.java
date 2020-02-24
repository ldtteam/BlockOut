package com.ldtteam.blockout.style.core;

import com.google.common.collect.ImmutableMap;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a style inside BlockOut.
 * Different mods that consume the BlockOut library can use the same controls with the same source, with different appears by using different styles.
 */
public interface IStyle
{

    /**
     * Returns the id of the {@link IStyle}.
     *
     * @return the id of the {@link IStyle}.
     */
    @NotNull
    ResourceLocation getId();

    /**
     * Returns a {@link IResource} in an {@link Optional}, if the resource is not known to this style, then {@link Optional#empty()} is returned.
     * Also, if type of resource does not match to {@link T}, then {@link Optional#empty()} is returned.
     *
     * @param id  The id of the {@link IResource} to find.
     * @param <T> The type of {@link IResource} to find.
     * @return The {@link IResource} in an {@link Optional} if it is known and of the right type, else {@link Optional#empty()}.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    default <T extends IResource> Optional<T> getResource(@NotNull final ResourceLocation id)
    {
        final IResource resource = getResources().get(id);
        if (resource == null)
        {
            return Optional.empty();
        }

        try
        {
            return Optional.of((T) resource);
        }
        catch (ClassCastException ex)
        {
            //Seems like wrong resource type.
            return Optional.empty();
        }
    }

    /**
     * All known {@link IResource} to this {@link IStyle}.
     *
     * @return all known {@link IResource}.
     */
    @NotNull
    ImmutableMap<ResourceLocation, IResource> getResources();
}
