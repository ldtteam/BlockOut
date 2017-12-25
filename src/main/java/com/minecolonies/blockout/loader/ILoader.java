package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.core.Pane;
import com.minecolonies.blockout.views.View;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a object that can load GUI definitions from file.
 */
public interface ILoader
{

    /**
     * Method used to detect if this {@link ILoader} accepts the file in the {@link ResourceLocation}
     *
     * @param location The location to check.
     * @return True when the loader accepts. false when not.
     */
    boolean accepts(@NotNull final ResourceLocation location);

    /**
     * Method used to detect if this {@link ILoader} accepts the {@link IPaneParams} to load.
     *
     * @param paneParams The {@link IPaneParams} to check.
     * @return True when the loader accepts, false when not.
     */
    boolean accepts(@NotNull final IPaneParams paneParams);

    /**
     * Creates a {@link Pane} from a given {@link IPaneParams}
     *
     * @param params The params to create a pane from.
     * @param parent The parent view.
     * @return The pane.
     */
    Pane createFromPaneParams(@NotNull final IPaneParams params, @NotNull final View parent);

    /**
     * Creates {@link Pane} from a raw string and inserts it into the given view.
     *
     * @param resource The name of the file to create a Pane from.
     * @param parent   The parent view.
     */
    void createFromFile(ResourceLocation resource, View parent);
}
