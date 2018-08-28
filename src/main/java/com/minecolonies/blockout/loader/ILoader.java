package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.core.element.IUIElement;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a object that can load GUI definitions from file.
 */
public interface ILoader
{
    /**
     * Creates a {@link IUIElement} from a given {@link String}.
     *
     * @param data The data to load the UI from.
     * @return The element.
     */
    @NotNull
    IUIElementData createFromFile(@NotNull final String data) throws Exception;
}
