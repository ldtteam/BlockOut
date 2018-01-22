package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.core.element.IUIElement;
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
     * Creates a {@link IUIElement} from a given {@link IPaneParams}
     *
     * @param params The params to create a pane from.
     * @param parent The parent {@link IUIElement}.
     * @return The element.
     */
    IUIElement createFromPaneParams(@NotNull final IPaneParams params, @NotNull final IUIElement parent);
}
