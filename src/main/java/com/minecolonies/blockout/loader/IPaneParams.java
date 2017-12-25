package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.util.Parsing;
import com.minecolonies.blockout.util.SizePair;
import com.minecolonies.blockout.views.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IPaneParams
{
    /**
     * Method to get the type name of the Pane that is to be constructed from these {@link IPaneParams}
     *
     * @return The pane type.
     */
    String getType();

    /**
     * Method used to get the parent {@link View} if it exists.
     */
    @Nullable
    View getParentView();

    /**
     * Method used to set the parent {@link View} if it exists.
     * @param parent The parent {@link View} for the Pane that is to be constructed, null if none exists.
     */
    void setParentView(@Nullable final View parent);

    /**
     * Method used to get the parents width if it exists.
     *
     * @return The parents width, if no parents exists 0 is returned.
     */
    int getParentWidth();

    /**
     * Method used to get the parents height if it exists.
     * @return The parents height, if no parents exists 0 is returned.
     */
    int getParentHeight();

    /**
     * Method used to get a list of {@link IPaneParams} of children of the Pane that is to be constructed.
     * @return A list with {@link IPaneParams} to construct the children.
     */
    @Nullable
    List<IPaneParams> getChildren();

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

    default <T extends Enum<T>> T getEnumAttribute(String name, Class<T> clazz, T def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Enum.valueOf(clazz, attr);
        }
        return def;
    }

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    @Nullable
    String getStringAttribute(@NotNull final String name, @Nullable final String def);

    /**
     * Get the scalable integer attribute from name and definition.
     *
     * @param name  the name.
     * @param def   the definition.
     * @param scale the scale.
     * @return the integer.
     */
    default int getScalableIntegerAttribute(@NotNull final String name, final int def, final int scale)
    {
        final String attr = getStringAttribute(name, null);
        return Parsing.parseScalableInteger(attr, def, scale);
    }

    /**
     * Get the size pair attribute.
     *
     * @param name  the name.
     * @param def   the definition.
     * @param scale the scale.
     * @return the SizePair.
     */
    @Nullable
    default SizePair getSizePairAttribute(@NotNull final String name, @Nullable final SizePair def, @Nullable final SizePair scale)
    {
        final String attr = getStringAttribute(name, null);
        return Parsing.parseSizePairAttribute(attr, def, scale);
    }


    /**
     * Get the color attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition
     * @return int color value.
     */
    default int getColorAttribute(@NotNull final String name, final int def)
    {
        final String attr = getStringAttribute(name, null);
        return Parsing.parseColor(attr, def);
    }
}
