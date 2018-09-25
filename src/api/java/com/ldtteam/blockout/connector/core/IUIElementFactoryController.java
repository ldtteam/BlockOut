package com.ldtteam.blockout.connector.core;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.loader.core.IUIElementData;
import org.jetbrains.annotations.NotNull;

public interface IUIElementFactoryController
{

    /**
     * Registers a factory for a {@link IUIElement} to this controller.
     *
     * @param factory The factory.
     * @return The controller.
     */
    IUIElementFactoryController registerFactory(@NotNull final IUIElementFactory<?> factory);

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
    <T extends IUIElement> IUIElementData getDataFromElement(@NotNull final T element);
}
