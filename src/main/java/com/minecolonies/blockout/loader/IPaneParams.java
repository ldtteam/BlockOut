package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.loader.xml.XMLPaneParams;
import com.minecolonies.blockout.util.SizePair;
import com.minecolonies.blockout.views.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IPaneParams
{
    String getType();

    View getParentView();

    void setParentView(View parent);

    int getParentWidth();

    int getParentHeight();

    @Nullable
    List<XMLPaneParams> getChildren();

    @NotNull
    String getText();

    @Nullable
    String getLocalizedText();

    /**
     * Get the string attribute.
     *
     * @param name the name to search.
     * @return the attribute.
     */
    String getStringAttribute(String name);

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    String getStringAttribute(String name, String def);

    /**
     * Get the localized string attribute from the name.
     *
     * @param name the name.
     * @return the string attribute.
     */
    @Nullable
    String getLocalizedStringAttribute(String name);

    /**
     * Get the localized String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the string.
     */
    @Nullable
    String getLocalizedStringAttribute(String name, String def);

    /**
     * Get the integer attribute from the name.
     *
     * @param name the name.
     * @return the integer.
     */
    int getIntegerAttribute(String name);

    /**
     * Get the integer attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the int.
     */
    int getIntegerAttribute(String name, int def);

    /**
     * Get the float attribute from name.
     *
     * @param name the name.
     * @return the float.
     */
    float getFloatAttribute(String name);

    /**
     * Get the float attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the float.
     */
    float getFloatAttribute(String name, float def);

    /**
     * Get the double attribute from name.
     *
     * @param name the name.
     * @return the double.
     */
    double getDoubleAttribute(String name);

    /**
     * Get the double attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the double.
     */
    double getDoubleAttribute(String name, double def);

    /**
     * Get the boolean attribute from name.
     *
     * @param name the name.
     * @return the boolean.
     */
    boolean getBooleanAttribute(String name);

    /**
     * Get the boolean attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the boolean.
     */
    boolean getBooleanAttribute(String name, boolean def);

    /**
     * Get the boolean attribute from name and class and definition..
     *
     * @param name  the name.
     * @param clazz the class.
     * @param def   the definition.
     * @param <T>   the type of class.
     * @return the enum attribute.
     */
    <T extends Enum<T>> T getEnumAttribute(String name, Class<T> clazz, T def);

    /**
     * Get the scalable integer attribute from name and definition.
     *
     * @param name  the name.
     * @param def   the definition.
     * @param scale the scale.
     * @return the integer.
     */
    int getScalableIntegerAttribute(String name, int def, int scale);

    /**
     * Get the size pair attribute.
     *
     * @param name  the name.
     * @param def   the definition.
     * @param scale the scale.
     * @return the SizePair.
     */
    @Nullable
    SizePair getSizePairAttribute(String name, SizePair def, SizePair scale);

    /**
     * Get the color attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition
     * @return int color value.
     */
    int getColorAttribute(String name, int def);
}
