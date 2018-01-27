package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import org.jetbrains.annotations.NotNull;

public interface IUIElementFactoryController
{

    /**
     * Creates a {@link IUIElement} from the given {@link IUIElementData}.
     * @param data The {@link IUIElementData} to create the {@link IUIElement} from.
     * @return The {@link IUIElement} that has been created using the given {@link IUIElementData}.
     */
    @NotNull
    IUIElement getElementFromData(@NotNull final IUIElementData data);

    /**
     * Creates a {@link IUIElementData} from a given {@link IUIElement}. The given {@link IUIElementData} is a {@link java.io.Serializable}.
     * @param element The {@link IUIElement} to get the {@link IUIElementData} from.
     * @return A {@link java.io.Serializable} {@link IUIElementData} created from the {@link IUIElement}.
     */
    @NotNull
    IUIElementData getDataFromElement(@NotNull final IUIElement element);
}
