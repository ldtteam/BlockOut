package com.minecolonies.blockout.loader;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.AxisDistanceBuilder;
import com.minecolonies.blockout.core.element.values.ControlDirection;
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public interface IUIElementData
{
    @NotNull
    Pattern SINGLE_NAME_BINDING_REGEX = Pattern.compile("\\{([Bb])inding:(?<singleName>[a-zA-Z_]+)}");
    //((?<getterName>[a-zA-Z_]+)#(?<setterName>[a-zA-Z_]+))
    @NotNull
    Pattern SPLIT_NAME_BINDING_REGEX  = Pattern.compile("\\{([Bb])inding:((?<getterName>[a-zA-Z_]+)#(?<setterName>[a-zA-Z_]+))}");

    /**
     * Method to get the type name of the Pane that is to be constructed from these {@link IUIElementData}
     *
     * @return The pane type.
     */
    ResourceLocation getType();

    /**
     * Method to get the style id, bound, for this control.
     *
     * @return The bound style id.
     */
    @NotNull
    IDependencyObject<ResourceLocation> getBoundStyleId();

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
    List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren);

    /**
     * Checks if this control has possible children.
     *
     * @return True when this control has children, false when not.
     */
    boolean hasChildren();

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
     * Returns a bound integer attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull final String name)
    {
        return getBoundIntegerAttribute(name, 0);
    }

    /**
     * Returns a bound integer attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull String name, final int def);

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
     * Returns a bound float attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<Float> getBoundFloatAttribute(@NotNull final String name)
    {
        return getBoundFloatAttribute(name, 0f);
    }

    /**
     * Returns a bound float attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<Float> getBoundFloatAttribute(@NotNull String name, final float def);
    
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
     * Returns a bound double attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<Double> getBoundDoubleAttribute(@NotNull final String name)
    {
        return getBoundDoubleAttribute(name, 0d);
    }

    /**
     * Returns a bound double attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<Double> getBoundDoubleAttribute(@NotNull String name, final double def);
    
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

    /**
     * Returns a bound boolean attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull final String name)
    {
        return getBoundBooleanAttribute(name, true);
    }

    /**
     * Returns a bound boolean attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull String name, final boolean def);

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
     * Returns a bound enum attribute. If not found nor bound a static bound to the given default is returned.
     *
     * @param name  The name.
     * @param clazz The class of the enum
     * @param def   The default value if not defined.
     * @param <T>   The enum type.
     * @return The bound enum attribute.
     */
    <T extends Enum<T>> IDependencyObject<T> getBoundEnumAttribute(@NotNull final String name, Class<T> clazz, T def);

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

    /**
     * Returns a bound enum set attribute. If not found nor bound a static bound to an empty enumset is returned.
     *
     * @param name  The name.
     * @param clazz The class of the enum
     * @param <T>   The enum type.
     * @return The bound enum attribute.
     */
    <T extends Enum<T>> IDependencyObject<EnumSet<T>> getBoundEnumSetAttribute(@NotNull final String name, @NotNull final Class<T> clazz);

    /**
     * Returns the id of the control.
     * If not specified a random UUID will be used.
     *
     * @return The id of the control.
     */
    @NotNull
    default String getElementId()
    {
        return getStringAttribute(Constants.Controls.General.CONST_ID, UUID.randomUUID().toString());
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

    /**
     * Returns a bound string attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<String> getBoundStringAttribute(@NotNull final String name)
    {
        return getBoundStringAttribute(name, "");
    }

    /**
     * Returns a bound string attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<String> getBoundStringAttribute(@NotNull String name, final String def);

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
        builder.readFromString(getParentView() != null ? getParentView().getElementSize() : new Vector2d(), attribute);

        return builder.create();
    }

    /**
     * Returns a bound axisDistance attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(@NotNull final String name)
    {
        return getBoundAxisDistanceAttribute(name, new AxisDistance());
    }

    /**
     * Returns a bound axisDistance attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(@NotNull String name, final AxisDistance def);

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

    /**
     * Returns a bound alignment attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(@NotNull final String name)
    {
        return getBoundAlignmentAttribute(name, EnumSet.of(Alignment.LEFT, Alignment.TOP));
    }

    /**
     * Returns a bound alignment attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(@NotNull String name, final EnumSet<Alignment> def);

    @NotNull
    default ControlDirection getControlDirectionAttribute(@NotNull final String name)
    {
        return getControlDirectionAttribute(name, ControlDirection.LEFT_RIGHT);
    }

    @NotNull
    default ControlDirection getControlDirectionAttribute(@NotNull final String name, @NotNull final ControlDirection def)
    {
        @Nullable final String attribute = getStringAttribute(name);
        if (attribute == null || attribute.trim().isEmpty())
        {
            return def;
        }

        return ControlDirection.fromString(attribute);
    }

    /**
     * Returns a bound ControlDirection attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<ControlDirection> getBoundControlDirectionAttribute(@NotNull final String name)
    {
        return getBoundControlDirectionAttribute(name, ControlDirection.LEFT_RIGHT);
    }

    /**
     * Returns a bound ControlDirection attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<ControlDirection> getBoundControlDirectionAttribute(@NotNull String name, final ControlDirection def);


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

    /**
     * Returns a bound vector2d attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<Vector2d> getBoundVector2dAttribute(@NotNull final String name)
    {
        return getBoundVector2dAttribute(name, new Vector2d());
    }

    /**
     * Returns a bound vector2d attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<Vector2d> getBoundVector2dAttribute(@NotNull String name, final Vector2d def);

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

    /**
     * Returns a bound resourceLocation attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<ResourceLocation> getBoundResourceLocationAttribute(@NotNull final String name)
    {
        return getBoundResourceLocationAttribute(name, new ResourceLocation("minecraft:missingno"));
    }

    /**
     * Returns a bound resourceLocation attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<ResourceLocation> getBoundResourceLocationAttribute(@NotNull String name, final ResourceLocation def);

    @NotNull
    default BoundingBox getBoundingBoxAttribute(@NotNull final String name)
    {
        return getBoundingBoxAttribute(name, new BoundingBox());
    }

    @NotNull
    default BoundingBox getBoundingBoxAttribute(@NotNull final String name, @NotNull final BoundingBox def)
    {
        @Nullable final String attribute = getStringAttribute(name);
        if (attribute == null || attribute.trim().isEmpty())
        {
            return def;
        }

        return BoundingBox.fromString(attribute);
    }

    /**
     * Returns a bound boundingBox attibute from a name. If not found or bound 0 is returned as a static bound.
     *
     * @param name The name of the attribute.
     * @return The bound attribute.
     */
    default IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(@NotNull final String name)
    {
        return getBoundBoundingBoxAttribute(name, new BoundingBox());
    }

    /**
     * Returns a bound boundingBox attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(@NotNull String name, final BoundingBox def);

    /**
     * Returns the bound datacontext for the ui element this data belongs to.
     * Only returns a meaningfull value on the server side.
     * On the client side this value will always be bound to a plain Object.
     *
     * @return The bound datacontext.
     */
    IDependencyObject<Object> getBoundDataContext();

    /**
     * Returns the itemstack stored in the attribute with the given name.
     * Converts the NBT data stored in the attribute into a ItemStack.
     *
     * @param name The name to lookup the stack for.
     * @return The stack
     */
    default ItemStack getItemstackAttribute(@NotNull final String name)
    {
        final NBTTagCompound compound = getNBTAttribute(name);
        if (compound == null)
        {
            return ItemStack.EMPTY;
        }

        try
        {
            return new ItemStack(compound);
        }
        catch (Exception ex)
        {
            Log.getLogger().warn(String.format("Failed to deserialize ItemStack from: %s", compound), ex);
            return ItemStack.EMPTY;
        }
    }

    /**
     * Returns the nbt stored in the attribute with the given name.
     * @param name The name to lookup the nbt for.
     * @return The NBT contained in the attribute with the given name.
     */
    <T extends NBTBase> T getNBTAttribute(@NotNull final String name);

    /**
     * Returns the  bound itemstack stored in the attribute with the given name.
     * Converts the NBT data stored in the attribute into a ItemStack.
     *
     * @param name The name to lookup the stack for.
     * @return The stack
     */
    default IDependencyObject<ItemStack> getBoundItemStackAttribute(@NotNull final String name)
    {
        return DependencyObjectHelper.transform(getBoundNBTAttribute(name, ItemStack.EMPTY.serializeNBT()), ItemStack::new, ItemStack::serializeNBT);
    }

    /**
     * Returns the bound nbt stored in the attribute with the given name.
     *
     * @param name The name to lookup the bound nbt for.
     * @param def  The default used incase of binding failure.
     * @param <T>  The type of NBT required.
     * @return The bound nbt.
     */
    <T extends NBTBase> IDependencyObject<T> getBoundNBTAttribute(@NotNull final String name, @NotNull final T def);

    /**
     * Returns the entity stored in the attribute with the given name.
     * Converts the NBT data stored in the attribute into a entity.
     *
     * @param name The name to lookup the stack for.
     * @return The entity
     */
    default Entity getEntityAttribute(@NotNull final String name)
    {
        final NBTTagCompound compound = getNBTAttribute(name);
        if (compound == null)
        {
            return null;
        }

        try
        {
            return EntityList.createEntityFromNBT(compound, null);
        }
        catch (Exception ex)
        {
            Log.getLogger().warn(String.format("Failed to deserialize Entity from: %s", compound), ex);
            return null;
        }
    }

    /**
     * Returns the  bound entity stored in the attribute with the given name.
     * Converts the NBT data stored in the attribute into a entity.
     *
     * @param name The name to lookup the entity for.
     * @return The entity
     */
    default IDependencyObject<Entity> getBoundEntityAttribute(@NotNull final String name)
    {
        return DependencyObjectHelper.transform(getBoundNBTAttribute(name, new NBTTagCompound()), nbt -> {
              if (nbt == null || Objects.equals(nbt, new NBTTagCompound()))
              {
                  return null;
              }

              try
              {
                  return EntityList.createEntityFromNBT(nbt, null);
              }
              catch (Exception ex)
              {
                  Log.getLogger().warn("Failed to tranform Entity dependency.", ex);
                  return null;
              }
          },
          entity -> {
              if (entity == null)
              {
                  return null;
              }

              return entity.serializeNBT();
          });
    }
}
