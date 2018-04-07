package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.AxisDistanceBuilder;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
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
    ResourceLocation getType();

    /**
     * Method used to get the parent {@link IUIElementHost} if it exists.
     */
    @Nullable
    IUIElementHost getParentView();

    /**
     * Method used to get a list of {@link IUIElementData} of children of the Pane that is to be constructed.
     * @return A list with {@link IUIElementData} to construct the children.
     */
    @Nullable
    List<IUIElementData> getChildren();

    /**
     * Get the integer attribute from the name.
     *
     * @param name the name.
     * @return the integer.
     */
    default int getIntegerAttribute(@NotNull final String name)
    {
        return getIntegerAttribute(name, 0);
    }

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
    default float getFloatAttribute(@NotNull final String name)
    {
        return getFloatAttribute(name, 0f);
    }

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
    default double getDoubleAttribute(@NotNull final String name)
    {
        return getDoubleAttribute(name, 0d);
    }

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
    default boolean getBooleanAttribute(@NotNull final String name)
    {
        return getBooleanAttribute(name, false);
    }

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

    default <T extends Enum<T>> EnumSet<T> getEnumSetAttributes(String name, Class<T> clazz)
    {
        final String attr = getStringAttribute(name, "");
        final String[] splitted = attr.split(",");

        final EnumSet<T> result = EnumSet.noneOf(clazz);
        for (String e : splitted)
        {
            result.add(Enum.valueOf(clazz, e.trim()));
        }

        return result;
    }

    @NotNull
    default String getStringAttribute(@NotNull final String name)
    {
        return getStringAttribute(name, "");
    }

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    @NotNull
    String getStringAttribute(@NotNull final String name, @NotNull final String def);

    @NotNull
    default AxisDistance getAxisDistanceAttribute(@NotNull final String name)
    {
        return getAxisDistanceAttribute(name, new AxisDistance());
    }

    @NotNull
    default AxisDistance getAxisDistanceAttribute(@NotNull final String name, @NotNull final AxisDistance def)
    {
        @Nullable final String attribute = getStringAttribute(name);
        if (attribute == null || attribute.trim().isEmpty())
        {
            return def;
        }

        final AxisDistanceBuilder builder = new AxisDistanceBuilder();
        builder.readFromString(getParentView().getElementSize(), attribute);
        
        return builder.create();
    }

    @NotNull
    default EnumSet<Alignment> getAlignmentAttribute(@NotNull final String name)
    {
        return getAlignmentAttribute(name, EnumSet.of(Alignment.NONE));
    }

    @NotNull
    default EnumSet<Alignment> getAlignmentAttribute(@NotNull final String name, @NotNull final EnumSet<Alignment> def)
    {
        @Nullable final String attribute = getStringAttribute(name);
        if (attribute == null || attribute.trim().isEmpty())
        {
            return def;
        }

        return Alignment.fromString(attribute);
    }

    @NotNull
    default Vector2d getVector2dAttribute(@NotNull final String name)
    {
        return getVector2dAttribute(name, new Vector2d());
    }

    @NotNull
    default Vector2d getVector2dAttribute(@NotNull final String name, @NotNull final Vector2d def)
    {
        @Nullable final String attribute = getStringAttribute(name);
        if (attribute == null || attribute.trim().isEmpty())
        {
            return def;
        }

        final String[] components = attribute.split(",");
        if (components.length == 1)
        {
            return new Vector2d(Double.parseDouble(components[0]));
        }
        else if(components.length == 2)
        {
            return new Vector2d(Double.parseDouble(components[0]), Double.parseDouble(components[1]));
        }
        else
        {
            return def;
        }
    }

    @NotNull
    default ResourceLocation getResourceLocationAttribute(@NotNull final String name)
    {
        return getResourceLocationAttribute(name, TextureMap.LOCATION_MISSING_TEXTURE);
    }

    @NotNull
    default ResourceLocation getResourceLocationAttribute(@NotNull final String name, @NotNull final ResourceLocation def)
    {
        @Nullable final String attribute = getStringAttribute(name);
        if (attribute == null || attribute.trim().isEmpty())
        {
            return def;
        }

        return new ResourceLocation(attribute);
    }
    
    
}
