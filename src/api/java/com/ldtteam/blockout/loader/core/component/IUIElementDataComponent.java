package com.ldtteam.blockout.loader.core.component;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import net.minecraft.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents a single entry in the {@link IUIElementData}
 */
public interface IUIElementDataComponent
{

    /**
     * Returns the string representation of the component.
     *
     * @return The string.
     */
    String getAsString();

    /**
     * Sets the string on the current IUIElementData.
     * @param string The string to set.
     * @throws IllegalArgumentException when current component is not a String.              
     */
    void setString(@NotNull final String string) throws IllegalArgumentException;

    /**
     * Returns the boolean representation of the component.
     *
     * @return The boolean.
     */
    Boolean getAsBoolean();

    /**
     * Sets the boolean on the current IUIElementData.
     * @param bool The boolean to set.
     * @throws IllegalArgumentException when current component is not a Boolean.              
     */
    void setBoolean(@NotNull final Boolean bool) throws IllegalArgumentException;

    /**
     * Returns the integer representation of the component.
     *
     * @return The integer.
     */
    Integer getAsInteger();

    /**
     * Sets the integer on the current IUIElementData.
     * @param integer The integer to set.
     * @throws IllegalArgumentException when current component is not a Integer.              
     */
    void setInteger(@NotNull final Integer integer) throws IllegalArgumentException;

    /**
     * Returns the double representation of the component.
     *
     * @return The double.
     */
    Double getAsDouble();

    /**
     * Sets the double on the current IUIElementData.
     * @param d The double to set.
     * @throws IllegalArgumentException when current component is not a Double.              
     */
    void setDouble(@NotNull final Double d) throws IllegalArgumentException;

    /**
     * Returns the float representation of the component.
     *
     * @return The float
     */
    Float getAsFloat();

    /**
     * Sets the float on the current IUIElementData.
     * @param f The float to set.
     * @throws IllegalArgumentException when current component is not a Float.              
     */
    void setFloat(@NotNull final Float f) throws IllegalArgumentException;

    /**
     * Returns the list representation of the component.
     *
     * @return The list.
     */
    List<IUIElementDataComponent> getAsList();

    /**
     * Sets the list on the current IUIElementData.
     * @param list The list to set.
     * @throws IllegalArgumentException when current component is not a List.              
     */
    void setList(@NotNull final List<? extends IUIElementDataComponent> list) throws IllegalArgumentException;

    /**
     * Returns the map based representation of this component.
     *
     * @return The map based representation of this component.
     */
    Map<String, IUIElementDataComponent> getAsMap();

    /**
     * Sets the map based representation of this component.
     *
     * @param map The map to set.
     * @throws IllegalArgumentException when current component is not complex.
     */
    void setMap(@NotNull final Map<String, ? extends IUIElementDataComponent> map) throws IllegalArgumentException;

    /**
     * Turns this {@link IUIElementDataComponent} into a fullblown {@link IUIElementData} by parsing the {@link IUIElementMetaData}
     *
     * @param parent The parent control of the target.
     * @return The {@link IUIElementData}.
     */
    IUIElementData<?> toIUIElementData(@Nullable final IUIElementHost parent);

    /**
     * Returns the type of data stored in this data component.
     *
     * @return The type of component data.
     */
    ComponentType getType();

    default boolean isString()
    {
        return true;
    }

    default boolean isBool()
    {
        return getType() == ComponentType.BOOL;
    }

    default boolean isNumber()
    {
        return getType() == ComponentType.NUMBER;
    }

    default boolean isList()
    {
        return getType() == ComponentType.LIST;
    }

    default boolean isComplex()
    {
        return getType() == ComponentType.COMPLEX;
    }
}
