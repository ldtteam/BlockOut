package com.ldtteam.blockout.loader.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.AxisDistanceBuilder;
import com.ldtteam.blockout.element.values.ControlDirection;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents the raw data of a {@link IUIElement}
 */
public interface IUIElementData
{
    /**
     * Returns the metadata of a element.
     *
     * @return The metadata.
     */
    @NotNull
    IUIElementMetaData getMetaData();

    /**
     * Returns a dependency target from raw data with a default value.
     *
     * @param name               The name of the raw data to get.
     * @param typeMatcher        A function checking a {@link IUIElementDataComponent} for the right contained type.
     * @param componentConverter A function converting a {@link IUIElementDataComponent} to the contained type.
     * @param defaultValue       The default value.
     * @param <T>                The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    default <T> IDependencyObject<T> getFromRawDataWithDefault(
      @NotNull final String name,
      @NotNull final Predicate<IUIElementDataComponent> typeMatcher,
      @NotNull final Function<IUIElementDataComponent, T> componentConverter,
      @NotNull final IBindingEngine engine,
      @Nullable final T defaultValue
    )
    {
        return getFromRawDataWithProperty(
          name,
          typeMatcher,
          componentConverter,
          engine,
          PropertyCreationHelper.createNoneSettableFromStaticValue(defaultValue),
          defaultValue
        );
    }

    /**
     * Returns a dependency target from raw data with a default value.
     *
     * @param name               The name of the raw data to get.
     * @param typeMatcher        A function checking a {@link IUIElementDataComponent} for the right contained type.
     * @param componentConverter A function converting a {@link IUIElementDataComponent} to the contained type.
     * @param defaultProperty    The default property.
     * @param <T>                The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    <T> IDependencyObject<T> getFromRawDataWithProperty(
      @NotNull final String name,
      @NotNull final Predicate<IUIElementDataComponent> typeMatcher,
      @NotNull final Function<IUIElementDataComponent, T> componentConverter,
      @NotNull final IBindingEngine engine,
      @NotNull final Property<T> defaultProperty,
      @Nullable final T defaultValue
    );

    /**
     * Returns a list of all information stored under the giving name.
     *
     * @param name The name to look the components up from.
     * @return The components.
     */
    List<IUIElementDataComponent> getComponents(
      @NotNull final String name
    );

    /**
     * Returns a dependable StringConverter loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the string from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable string.
     */
    default IDependencyObject<String> getString(@NotNull final String name, @NotNull final String value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          IUIElementDataComponent::getAsString,
          engine,
          value);
    }

    /**
     * Returns a dependable Boolean loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the boolean from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boolean.
     */
    default IDependencyObject<Boolean> getBoolean(@NotNull final String name, @NotNull final Boolean value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isBool,
          IUIElementDataComponent::getAsBoolean,
          engine,
          value
        );
    }

    /**
     * Returns a dependable Integer loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the integer from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable integer.
     */
    default IDependencyObject<Integer> getInteger(@NotNull final String name, @NotNull final Integer value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isNumber,
          IUIElementDataComponent::getAsInteger,
          engine,
          value
        );
    }

    /**
     * Returns a dependable Double loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the double from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable double.
     */
    default IDependencyObject<Double> getDouble(@NotNull final String name, @NotNull final Double value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isNumber,
          IUIElementDataComponent::getAsDouble,
          engine,
          value
        );
    }

    /**
     * Returns a dependable Float loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the float from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable float.
     */
    default IDependencyObject<Float> getFloat(@NotNull final String name, @NotNull final Float value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isNumber,
          IUIElementDataComponent::getAsFloat,
          engine,
          value
        );
    }

    /**
     * Returns a dependable ResourceLocation loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the resourceLocation from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable resourceLocation.
     */
    default IDependencyObject<ResourceLocation> getResourceLocation(@NotNull final String name, @NotNull final ResourceLocation value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> new ResourceLocation(iuiElementDataComponent.getAsString()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable Enum loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the enum from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable enum.
     */
    default <E extends Enum<E>> IDependencyObject<E> getEnumOfTypeE(@NotNull final String name, @NotNull final Class<E> enumClass, @NotNull final E value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> E.valueOf(enumClass, iuiElementDataComponent.getAsString().trim()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable Enumset loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the enumset from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable enumset.
     */
    @SuppressWarnings("unchecked")
    default <E extends Enum<E>> IDependencyObject<EnumSet<E>> getEnumSetOfTypeE(@NotNull final String name, @NotNull final Class<E> enumClass, @NotNull final EnumSet<E> value,
      @NotNull final IBindingEngine engine)
    {
        final EnumSet dummyInstance = EnumSet.noneOf(enumClass);

        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> {
              final String elementContents = iuiElementDataComponent.getAsString();
              final String[] elementParts = elementContents.split(",");

              for (final String elementPart :
                elementParts)
              {
                  dummyInstance.add(E.valueOf(enumClass, elementPart.trim()));
              }

              return dummyInstance;
          },
          engine,
          value
        );
    }

    /**
     * Returns a dependable AxisDistance loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the axisDistance from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable axisDistance.
     */
    default IDependencyObject<AxisDistance> getAxisDistance(@NotNull final String name, @NotNull final AxisDistance value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> new AxisDistanceBuilder().readFromString(
            getMetaData().getParent() != null ? getMetaData().getParent().getElementSize() : new Vector2d(Double.MAX_VALUE, Double.MAX_VALUE),
            iuiElementDataComponent.getAsString()
          ).create(),
          engine,
          value
        );
    }

    /**
     * Returns a dependable Alignment loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the alignment from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable alignment.
     */
    default IDependencyObject<EnumSet<Alignment>> getAlignment(@NotNull final String name, @NotNull final EnumSet<Alignment> value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> iuiElementDataComponent.isNumber()
                                       ? Alignment.fromInt(iuiElementDataComponent.getAsInteger())
                                       : Alignment.fromString(iuiElementDataComponent.getAsString()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable ControlDirection loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the controlDirection from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable controlDirection.
     */
    default IDependencyObject<ControlDirection> getControlDirection(@NotNull final String name, @NotNull final ControlDirection value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> ControlDirection.fromString(iuiElementDataComponent.getAsString()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable Vector2d loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the vector2d from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable vector2d.
     */
    default IDependencyObject<Vector2d> getVector2d(@NotNull final String name, @NotNull final Vector2d value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> Vector2d.fromString(iuiElementDataComponent.getAsString()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable BoundingBox loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the boundingBox from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boundingBox.
     */
    default IDependencyObject<BoundingBox> getBoundingBox(@NotNull final String name, @NotNull final BoundingBox value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> BoundingBox.fromString(iuiElementDataComponent.getAsString()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable NBTTagCompound loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the nBTTagCompound from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable nBTTagCompound.
     */
    default IDependencyObject<NBTTagCompound> getNBTTagCompound(@NotNull final String name, @NotNull final NBTTagCompound value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isComplex,
          iuiElementDataComponent -> (NBTTagCompound) iuiElementDataComponent.getAsNBT(),
          engine,
          value
        );
    }

    /**
     * Returns a dependable ItemStack loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the itemStack from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable itemStack.
     */
    default IDependencyObject<ItemStack> getItemStack(@NotNull final String name, @NotNull final ItemStack value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isComplex,
          iuiElementDataComponent -> new ItemStack((NBTTagCompound) iuiElementDataComponent.getAsNBT()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable IBlockState loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the iBlockState from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable iBlockState.
     */
    default IDependencyObject<IBlockState> getIBlockState(@NotNull final String name, @NotNull final IBlockState value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isComplex,
          iuiElementDataComponent -> NBTUtil.readBlockState((NBTTagCompound) iuiElementDataComponent.getAsNBT()),
          engine,
          value
        );
    }

    /**
     * Returns a dependable Entity loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the entity from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable entity.
     */
    default IDependencyObject<Entity> getEntity(@NotNull final String name, @NotNull final Entity value,
      @NotNull final IBindingEngine engine)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementDataComponent::isComplex,
          iuiElementDataComponent -> EntityList.createEntityFromNBT((NBTTagCompound) iuiElementDataComponent.getAsNBT(), null),
          engine,
          value
        );
    }


}
