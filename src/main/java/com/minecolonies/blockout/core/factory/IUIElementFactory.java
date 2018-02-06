package com.minecolonies.blockout.core.factory;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Interface describing classes that can create a new {@link IUIElement} from a {@link IUIElementData} instance.
 *
 * @param <U> The type of {@link IUIElement} it creates
 */
public interface IUIElementFactory<U extends IUIElement>
{

    /**
     * Creates a new {@link U} from the given {@link IUIElementData}.
     * @param elementData The {@link IUIElementData} which contains the data that is to be constructed.
     * @return The {@link U} that is stored in the {@link IUIElementData}.
     */
    @NotNull
    U readFromElementData(@NotNull final IUIElementData elementData);

    /**
     * Populates the given {@link IUIElementDataBuilder} with the data from {@link U} so that
     * the given {@link U} can be reconstructed with {@link #readFromElementData(IUIElementData)} created by
     * the given {@link IUIElementDataBuilder}.
     *
     * @param element The {@link U} to write into the {@link IUIElementDataBuilder}
     * @param builder The {@link IUIElementDataBuilder} to write the {@link U} into.
     */
    void writeToElementData(@NotNull final U element, @NotNull final IUIElementDataBuilder builder);
}
