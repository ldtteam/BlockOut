package com.ldtteam.blockout.json.loader;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.json.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.json.loader.core.IUIElementData;
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
     * @param engine The {@link IBindingEngine} the data is loaded with.
     * @return The element.
     */
    @NotNull
    IUIElementData createFromDataAndBindingEngine(@NotNull final String data, @NotNull final IBindingEngine engine) throws Exception;
}
