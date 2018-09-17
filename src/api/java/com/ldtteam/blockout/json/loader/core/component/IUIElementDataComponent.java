package com.ldtteam.blockout.json.loader.core.component;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.json.loader.core.IUIElementData;
import com.ldtteam.blockout.json.loader.core.IUIElementMetaData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
     * Returns the boolean representation of the component.
     *
     * @return The boolean.
     */
    Boolean getAsBoolean();

    /**
     * Returns the integer representation of the component.
     *
     * @return The integer.
     */
    default Integer getAsInteger()
    {
        return (int) (double) getAsDouble();
    }

    /**
     * Returns the double representation of the component.
     *
     * @return The double.
     */
    Double getAsDouble();

    /**
     * Returns the float representation of the component.
     *
     * @return The float
     */
    default Float getAsFloat()
    {
        return (float) (double) getAsDouble();
    }

    /**
     * Returns the list representation of the component.
     *
     * @return The list.
     */
    List<IUIElementDataComponent> getAsList();

    /**
     * Returns the contained data as NBT.
     * @return The NBT.
     */
    NBTBase getAsNBT();

    /**
     * Turns this {@link IUIElementDataComponent} into a fullblown {@link IUIElementData} by parsing the {@link IUIElementMetaData}
     *
     * @param parent The parent control of the target.
     * @return The {@link IUIElementData}.
     */
    IUIElementData toIUIElementData(@Nullable final IUIElementHost parent);

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
