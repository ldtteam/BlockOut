package com.minecolonies.blockout.loader.core;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.binding.property.Property;
import com.minecolonies.blockout.binding.property.PropertyCreationHelper;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.core.component.IUIElementDataComponent;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Returns a dependable String loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the string from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable string.
     */
    default IDependencyObject<String> getString(@NotNull final String name, @NotNull final String value)
    {
        return getFromRawDataWithDefault(
          name,
          String.class,
          IUIElementDataComponent::isString,
          IUIElementDataComponent::getAsString,
          value);
    }

    /**
     * Returns a dependency target from raw data with a default value.
     *
     * @param name               The name of the raw data to get.
     * @param targetClass        The target type of the dependency.
     * @param typeMatcher        A function checking a {@link IUIElementDataComponent} for the right contained type.
     * @param componentConverter A function converting a {@link IUIElementDataComponent} to the contained type.
     * @param defaultValue       The default value.
     * @param <T>                The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    default <T> IDependencyObject<T> getFromRawDataWithDefault(
      @NotNull final String name,
      @NotNull final Class<T> targetClass,
      @NotNull final Predicate<IUIElementDataComponent> typeMatcher,
      @NotNull final Function<IUIElementDataComponent, T> componentConverter,
      @Nullable final T defaultValue
    )
    {
        return getFromRawDataWithProperty(
          name,
          targetClass,
          typeMatcher,
          componentConverter,
          PropertyCreationHelper.createNoneSettableFromStaticValue(defaultValue)
        );
    }

    /**
     * Returns a dependency target from raw data with a default value.
     *
     * @param name               The name of the raw data to get.
     * @param targetClass        The target type of the dependency.
     * @param typeMatcher        A function checking a {@link IUIElementDataComponent} for the right contained type.
     * @param componentConverter A function converting a {@link IUIElementDataComponent} to the contained type.
     * @param defaultProperty    The default property.
     * @param <T>                The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    <T> IDependencyObject<T> getFromRawDataWithProperty(
      @NotNull final String name,
      @NotNull final Class<T> targetClass,
      @NotNull final Predicate<IUIElementDataComponent> typeMatcher,
      @NotNull final Function<IUIElementDataComponent, T> componentConverter,
      @NotNull final Property<T> defaultProperty
    );

    /**
     * Returns a dependable Boolean loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the boolean from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boolean.
     */
    default IDependencyObject<Boolean> getBoolean(@NotNull final String name, @NotNull final Boolean value)
    {
        return getFromRawDataWithDefault(
          name,
          Boolean.class,
          IUIElementDataComponent::isBool,
          IUIElementDataComponent::getAsBoolean,
          value
        );
    }

    /**
     * Returns a dependable Integer loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the integer from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boolean.
     */
    default IDependencyObject<Integer> getInteger(@NotNull final String name, @NotNull final Integer value)
    {
        return getFromRawDataWithDefault(
          name,
          Integer.class,
          IUIElementDataComponent::isNumber,
          IUIElementDataComponent::getAsInteger,
          value
        );
    }

    /**
     * Returns a dependable Double loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the double from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boolean.
     */
    default IDependencyObject<Double> getDouble(@NotNull final String name, @NotNull final Double value)
    {
        return getFromRawDataWithDefault(
          name,
          Double.class,
          IUIElementDataComponent::isNumber,
          IUIElementDataComponent::getAsDouble,
          value
        );
    }

    /**
     * Returns a dependable Float loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the float from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boolean.
     */
    default IDependencyObject<Float> getFloat(@NotNull final String name, @NotNull final Float value)
    {
        return getFromRawDataWithDefault(
          name,
          Float.class,
          IUIElementDataComponent::isNumber,
          IUIElementDataComponent::getAsFloat,
          value
        );
    }

    /**
     * Returns a dependable ResourceLocation loaded from the entry with the key: name.
     * If not found the given default value will be used.
     *
     * @param name  The key to load the resourceLocation from.
     * @param value The default used when no entry with the given name is found.
     * @return The dependable boolean.
     */
    default IDependencyObject<ResourceLocation> getResourceLocation(@NotNull final String name, @NotNull final ResourceLocation value)
    {
        return getFromRawDataWithDefault(
          name,
          ResourceLocation.class,
          IUIElementDataComponent::isString,
          iuiElementDataComponent -> new ResourceLocation(iuiElementDataComponent.getAsString()),
          value
        );
    }

    /**
     * Returns a dependable Boolean loaded from the entry with the key: name.
     * If not found a static null will be returned.
     *
     * @param name The key to load the boolean from.
     * @return The dependable boolean.
     */
    default IDependencyObject<IUIElementData> getIUIElementData(@NotNull final String name, @Nullable final IUIElementHost parent)
    {
        return getFromRawDataWithDefault(
          name,
          IUIElementData.class,
          IUIElementDataComponent::isComplex,
          iuiElementDataComponent -> iuiElementDataComponent.toIUIElementData(parent),
          null
        );
    }
}
