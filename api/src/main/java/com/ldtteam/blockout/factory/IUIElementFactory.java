package com.ldtteam.blockout.factory;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementDataBuilder;
import com.ldtteam.blockout.loader.core.IUIElementData;
import org.jetbrains.annotations.NotNull;

/**
 * Interface describing classes that can build a new {@link IUIElement} from a {@link IUIElementData} instance.
 *
 * @param <U> The type of {@link IUIElement} it creates
 */
public interface IUIElementFactory<U extends IUIElement>
{

    /**
     * The class of the element produced. Used during setup to detect dependency objects as well as events.
     *
     * @return The class of the element produced.
     */
    @NotNull
    Class<U> getProducedElementClass();

    /**
     * Returns the type that this factory builds.
     *
     * @return The type.
     */
    @NotNull
    String getTypeName();

    /**
     * Creates a new {@link U} from the given {@link IUIElementData}.
     *
     * @param elementData The {@link IUIElementData} which contains the data that is to be constructed.
     * @return The {@link U} that is stored in the {@link IUIElementData}.
     */
    @NotNull
    U readFromElementData(@NotNull final IUIElementData<?> elementData, @NotNull final IBindingEngine engine);

    /**
     * Populates the given {@link IUIElementDataBuilder} with the data from {@link U} so that
     * the given {@link U} can be reconstructed with {@link #readFromElementData(IUIElementData, IBindingEngine)} created by
     * the given {@link IUIElementDataBuilder}.
     *
     * @param element The {@link U} to write into the {@link IUIElementDataBuilder}
     * @param builder The {@link IUIElementDataBuilder} to write the {@link U} into.
     */
    void writeToElementData(@NotNull final U element, @NotNull final IUIElementDataBuilder<?> builder);
}
