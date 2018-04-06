package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public interface IUIElementData
{
    /**
     * Method to get the type name of the Pane that is to be constructed from these {@link IUIElementData}
     *
     * @return The pane type.
     */
    String getType();

    /**
     * Method used to get the parent {@link IUIElementHost} if it exists.
     */
    @Nullable
    IUIElementHost getParent();

    /**
     * Method used to set the parent {@link IUIElementHost} if it exists.
     * @param parent The parent {@link IUIElementHost} for the Pane that is to be constructed, null if none exists.
     */
    void setParentView(@Nullable final IUIElementHost parent);

    /**
     * Method used to get the parents width if it exists.
     *
     * @return The parents width, if no parents exists 0 is returned.
     */
    double getParentWidth();

    /**
     * Method used to get the parents height if it exists.
     * @return The parents height, if no parents exists 0 is returned.
     */
    double getParentHeight();

    /**
     * Method used to get a list of {@link IUIElementData} of children of the Pane that is to be constructed.
     * @return A list with {@link IUIElementData} to construct the children.
     */
    @Nullable
    List<IUIElementData> getChildren();

    /**
     * Method used to get the text for the pane.
     *
     * @return The text.
     */
    @Nullable
    String getText();

    /**
     * Method used to get a translated version of {@link #getText()}.
     *
     * @return The localised version of the text.
     */
    @Nullable
    String getLocalizedText();

    /**
     * Get the string attribute.
     *
     * @param name the name to search.
     * @return the attribute.
     */
    @Nullable
    String getStringAttribute(@NotNull final String name);

    /**
     * Get the localized string attribute from the name.
     *
     * @param name the name.
     * @return the string attribute.
     */
    @Nullable
    String getLocalizedStringAttribute(@NotNull final String name);

    /**
     * Get the localized String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the string.
     */
    @Nullable
    String getLocalizedStringAttribute(@NotNull final String name, @Nullable final String def);

    /**
     * Get the integer attribute from the name.
     *
     * @param name the name.
     * @return the integer.
     */
    int getIntegerAttribute(@NotNull final String name);

    /**
     * Get the integer attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the int.
     */
    int getIntegerAttribute(@NotNull final String name, final int def);

    /**
     * Get the float attribute from name.
     *
     * @param name the name.
     * @return the float.
     */
    float getFloatAttribute(@NotNull final String name);

    /**
     * Get the float attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the float.
     */
    float getFloatAttribute(@NotNull final String name, final float def);

    /**
     * Get the double attribute from name.
     *
     * @param name the name.
     * @return the double.
     */
    double getDoubleAttribute(@NotNull final String name);

    /**
     * Get the double attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the double.
     */
    double getDoubleAttribute(@NotNull final String name, final double def);

    /**
     * Get the boolean attribute from name.
     *
     * @param name the name.
     * @return the boolean.
     */
    boolean getBooleanAttribute(@NotNull final String name);

    /**
     * Get the boolean attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the boolean.
     */
    boolean getBooleanAttribute(@NotNull final String name, final boolean def);

    /**
     * Get the dock attribute from the name.
     *
     * @param name The name of the attribute.
     * @return The value stored in the dock attribute with the given name.
     */
    Dock getDockAttribute(@NotNull final String name);

    /**
     * Get the dock attribute from the name and the default value.
     *
     * @param name The name of the attribute.
     * @param def  The default value.
     * @return The value stored in the dock attribute with the given name.
     */
    Dock getDockAttribute(@NotNull final String name, final Dock def);

    /**
     * Get the axis distance attribute from the name and the default value.
     *
     * @param name The name of the attribute.
     * @return The value stored in the axis distance attribute with the given name.
     */
    AxisDistance getAxisDistanceAttribute(@NotNull final String name);

    /**
     * Get the axis distance attribute from the name and the default value.
     *
     * @param name The name of the attribute.
     * @param def  The default value.
     * @return The value stored in the axis distance attribute with the given name.
     */
    AxisDistance getAxisDistanceAttribute(@NotNull final String name, @NotNull final AxisDistance def);

    /**
     * Get the alignment attribute from the name.
     *
     * @param name The name of the attribute.
     * @return The value stored in the alignment attribute with the given name.
     */
    EnumSet<Alignment> getAlignmentAttribute(@NotNull final String name);

    /**
     * Get the alignment attribute from the name and the default value.
     *
     * @param name The name of the attribute.
     * @param def  The default value.
     * @return The value stored in the alignment attribute with the given name.
     */
    EnumSet<Alignment> getAlignmentAttribute(@NotNull final String name, final Alignment def);

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    @Nullable
    String getStringAttribute(@NotNull final String name, @Nullable final String def);


}
