package com.ldtteam.blockout.loader.core;

import com.google.inject.Key;
import com.google.inject.internal.MoreTypes;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.proxy.ProxyHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Represents the raw data of a {@link IUIElement}
 */
public interface IUIElementData<C extends IUIElementDataComponent>
{
    /**
     * Returns the metadata of a element.
     *
     * @return The metadata.
     */
    @NotNull
    IUIElementMetaData getMetaData();

    /**
     * Returns a dependency target from raw data with a default property.
     * Uses the given target type to lookup component converters in the injector.
     * <p>
     * Use this method if you are looking for something like {@code List<IUIElementData<?>>}, since those can not be resolved normally.
     *
     * @param name            The name of the raw data to get.
     * @param defaultProperty The default property.
     * @param engine          The binding engine.
     * @param defaultValue    The default value to use when the property fails.
     * @param targetType      The type of the component converter to retrieve needs to produce an instance of T
     * @param params          The parameters used during conversion from {@link IUIElementDataComponent} to T
     * @param <T>             The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    default <T> IDependencyObject<T> getFromRawDataWithProperty(
      @NotNull final String name,
      @NotNull final IBindingEngine engine,
      @NotNull final Property<T> defaultProperty,
      @NotNull final T defaultValue,
      @NotNull final Type targetType,
      @NotNull final Object... params
    )
    {
        return getComponentWithName(name)
                 .map(targetComponent -> engine.attemptBind(targetComponent, defaultValue).orElseGet(() -> {
                     final ParameterizedType type = new MoreTypes.ParameterizedTypeImpl(null, IUIElementDataComponentConverter.class, targetType);
                     final IUIElementDataComponentConverter<T> componentConverter =
                       (IUIElementDataComponentConverter<T>) ProxyHolder.getInstance().getInjector().getInstance(Key.get(type));

                     if (!componentConverter.matchesInputTypes(targetComponent))
                     {
                         return DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue);
                     }

                     return DependencyObjectHelper.createFromValue(componentConverter.readFromElement(targetComponent, this, params));
                 }))
                 .orElse(DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue));
    }

    /**
     * Returns a component with the given name.
     *
     * @param name The name of the component.
     * @return The optional containing the component if known.
     */
    @Nullable
    Optional<C> getComponentWithName(@NotNull final String name);

    /**
     * Returns a dependency target from raw data with a default value.
     * Uses the given target type to lookup component converters in the injector.
     * <p>
     * Use this method if you are looking for something like {@code List<IUIElementData<?>>}, since those can not be resolved normally.
     *
     * @param name         The name of the raw data to get.
     * @param defaultValue The default value.
     * @param engine       The binding engine.
     * @param targetType   The type of the component converter to retrieve needs to produce an instance of T
     * @param params       The parameters used during conversion from {@link IUIElementDataComponent} to T
     * @param <T>          The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    default <T> IDependencyObject<T> getFromRawDataWithDefault(
      @NotNull final String name,
      @NotNull final IBindingEngine engine,
      @NotNull final T defaultValue,
      @NotNull final Type targetType,
      @NotNull final Object... params
    )
    {
        return getFromRawDataWithProperty(
          name,
          engine,
          PropertyCreationHelper.createFromStaticValue(defaultValue),
          defaultValue,
          targetType,
          params
        );
    }

    /**
     * Returns raw data with a default value if not found
     * Uses the given target type to lookup component converters in the injector.
     * <p>
     * Use this method if you are looking for something like {@code List<IUIElementData<?>>}, since those can not be resolved normally.
     *
     * @param name         The name of the raw data to get.
     * @param defaultValue The default value.
     * @param targetType   The type of the component converter to retrieve needs to produce an instance of T
     * @param params       The parameters used during conversion from {@link IUIElementDataComponent} to T
     * @param <T>          The target type.
     * @return The converted raw data.
     */
    default <T> T getRawWithoutBinding(
      @NotNull final String name,
      @Nullable final T defaultValue,
      @NotNull final Type targetType,
      @NotNull final Object... params
    )
    {
        return getComponentWithName(name)
                 .map(targetComponent -> {
                     final ParameterizedType type = new MoreTypes.ParameterizedTypeImpl(null, IUIElementDataComponentConverter.class, targetType);
                     final IUIElementDataComponentConverter<T> componentConverter =
                       (IUIElementDataComponentConverter<T>) ProxyHolder.getInstance().getInjector().getInstance(Key.get(type));

                     if (!componentConverter.matchesInputTypes(targetComponent))
                     {
                         return defaultValue;
                     }

                     return componentConverter.readFromElement(targetComponent, this, params);
                 })
                 .orElse(defaultValue);
    }

    /**
     * Writes the current element into a data component.
     *
     * @param toWriteInto The component to write the data into.
     * @return The data component containing this element datas data.
     */
    <D extends IUIElementDataComponent> D toDataComponent(@NotNull final D toWriteInto);
}
