package com.ldtteam.blockout.style.core;

import com.google.common.collect.ImmutableMap;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IStyleManager
{

    /**
     * Searched in the {@link IStyle} with the given id for a {@link IResource} with the given id.
     * If not found it will search any other {@link IStyle} with a {@link IResource} of that id.
     *
     * @param styleId    The id of the primary {@link IStyle}.
     * @param resourceId The id of the searched {@link IResource}.
     * @param <T>        The type of the resource.
     * @return The resource.
     *
     * @throws IllegalArgumentException when no {@link IStyle} holds a {@link IResource} with the given id.
     */
    @NotNull
    default <T extends IResource> T getResource(@NotNull final IIdentifier styleId, @NotNull final IIdentifier resourceId) throws IllegalArgumentException
    {
        final IStyle primaryStyle = getStyles().get(styleId);

        if (primaryStyle != null)
        {
            final Optional<T> resource = primaryStyle.getResource(resourceId);
            if (resource.isPresent())
            {
                return resource.get();
            }
        }

        return getStyles()
                 .entrySet()
                 .stream()
                 .filter(e -> !e.getKey().equals(styleId))
                 .<Optional<T>>map(e -> e.getValue().getResource(resourceId))
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException(String.format("No style has a resource with the given id: %s", resourceId)));
    }

    /**
     * All known {@link IStyle} that are registered to this {@link IStyleManager}.
     *
     * @return All known {@link IStyle}.
     */
    @NotNull
    ImmutableMap<IIdentifier, IStyle> getStyles();

    /**
     * Loads the styles during post init.
     */
    void loadStyles();
}
