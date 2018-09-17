package com.ldtteam.blockout.factory;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.json.loader.core.IUIElementBuilder;
import com.ldtteam.blockout.json.loader.core.IUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Interface describing classes that can create a new {@link IUIElement} from a {@link IUIElementData} instance.
 *
 * @param <U> The type of {@link IUIElement} it creates
 */
public interface IUIElementFactory<U extends IUIElement>
{

    /**
     * Returns the type that this factory builds.
     * @return The type.
     */
    @NotNull
    ResourceLocation getType();

    /**
     * Creates a new {@link U} from the given {@link IUIElementData}.
     * @param elementData The {@link IUIElementData} which contains the data that is to be constructed.
     * @return The {@link U} that is stored in the {@link IUIElementData}.
     */
    @NotNull
    U readFromElementData(@NotNull final IUIElementData elementData);

    /**
     * Populates the given {@link IUIElementBuilder} with the data from {@link U} so that
     * the given {@link U} can be reconstructed with {@link #readFromElementData(IUIElementData)} created by
     * the given {@link IUIElementBuilder}.
     *
     * @param element The {@link U} to write into the {@link IUIElementBuilder}
     * @param builder The {@link IUIElementBuilder} to write the {@link U} into.
     */
    void writeToElementData(@NotNull final U element, @NotNull final IUIElementBuilder builder);
}
