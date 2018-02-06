package com.minecolonies.blockout.loader;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Interface that describes how to create a {@link IUIElementData} in code.
 */
public interface IUIElementDataBuilder
{
    /**
     * Method to set the type.
     *
     * @param type The type stored in the constructed {@link IUIElementData}
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder setType(String type);

    /**
     * Method to add a child {@link IUIElementData} to the constructed {@link IUIElementData} using its {@link IUIElementDataBuilder}.
     *
     * @param builder The {@link IUIElementDataBuilder} used to construct the {@link IUIElementData} of the child.
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder addChild(@NotNull IUIElementDataBuilder builder);

    /**
     * Method to add a child {@link IUIElementData} to the constructed {@link IUIElementData}.
     *
     * @param elementData The {@link IUIElementData} of the child to add.
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder addChild(@NotNull IUIElementData elementData);

    /**
     * Method to add a attribute to the constructed {@link IUIElementData}.
     *
     * @param key       The key of the attribute.
     * @param attribute The value of the attribute. Has to be serializable.
     * @return The instance this was called upon.
     */
    IUIElementDataBuilder addAttribute(@NotNull String key, @NotNull Serializable attribute);

    /**
     * Constructs the {@link IUIElementData} from the data contained in this builder.
     *
     * @return The constructed {@link IUIElementData}.
     */
    IUIElementData build();
}
