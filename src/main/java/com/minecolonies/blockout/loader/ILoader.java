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
     * Creates a {@link IUIElement} from a given {@link ResourceLocation}.
     *
     * @param location The location to load the UI from.
     * @return The element.
     */
    IUIElementData createFromFile(@NotNull final ResourceLocation location);
}
