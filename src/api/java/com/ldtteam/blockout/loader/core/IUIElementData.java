package com.ldtteam.blockout.loader.core;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.binding.property.PropertyCreationHelper;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.AxisDistanceBuilder;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents the raw data of a {@link IUIElement}
 */
public interface IUIElementData<C extends IUIElementDataComponent>
{
    /**
     * Returns the factory injector.
     * @return the factory injector.
     */
    Injector getFactoryInjector();

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
     * @param defaultValue       The default value.
     * @param engine             The binding engine.
     * @param params             The parameters used during conversion from {@link IUIElementDataComponent} to T
     * @param <T>                The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    default <T> IDependencyObject<T> getFromRawDataWithDefault(
      @NotNull final String name,
      @NotNull final IBindingEngine engine,
      @Nullable final T defaultValue,
      @NotNull final Object... params
    )
    {
        return getFromRawDataWithProperty(
          name,
          engine,
          PropertyCreationHelper.createNoneSettableFromStaticValue(defaultValue),
          defaultValue,
          params
        );
    }

    /**
     * Returns a dependency target from raw data with a default value.
     *
     * @param name               The name of the raw data to get.
     * @param defaultProperty    The default property.
     * @param engine             The binding engine.
     * @param params             The parameters used during conversion from {@link IUIElementDataComponent} to T
     * @param <T>                The target type.
     * @return A {@link IDependencyObject} that contains the raw data converted to the target type, or the none changeable default value.
     */
    default <T> IDependencyObject<T> getFromRawDataWithProperty(
      @NotNull final String name,
      @NotNull final IBindingEngine engine,
      @NotNull final Property<T> defaultProperty,
      @Nullable final T defaultValue,
      @NotNull final Object... params
    )
    {
        return getComponentWithName(name)
          .map(targetComponent -> engine.attemptBind(targetComponent, defaultValue).orElseGet(() -> {
              final IUIElementDataComponentConverter<T> componentConverter = getFactoryInjector().getInstance(Key.get(new TypeLiteral<IUIElementDataComponentConverter<T>>(){}));

              if (!componentConverter.matchesInputTypes(targetComponent))
                  return DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue);

              return DependencyObjectHelper.createFromValue(componentConverter.readFromElement(targetComponent, this, params));
          }))
          .orElse(DependencyObjectHelper.createFromProperty(defaultProperty, defaultValue));
    }

    /**
     * Returns a component with the given name.
     * @param name The name of the component.
     * @return The optional containing the component if known.
     */
    @Nullable
    Optional<C> getComponentWithName(@NotNull final String name);

    /**
     * Writes the current element into a data component.
     *
     * @param toWriteInto The component to write the data into.
     * @return The data component containing this element datas data.
     */
    <D extends IUIElementDataComponent> D toDataComponent(@NotNull final D toWriteInto);
}
