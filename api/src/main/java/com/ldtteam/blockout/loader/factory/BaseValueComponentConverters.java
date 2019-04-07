package com.ldtteam.blockout.loader.factory;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.AxisDistanceBuilder;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class BaseValueComponentConverters
{

    public static final class StringConverter implements IUIElementDataComponentConverter<String>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public String readFromElement(@NotNull final IUIElementDataComponent component, final IUIElementData sourceData, @NotNull final Object... params)
        {
            if (!component.isString())
            {
                throw new IllegalArgumentException("Required a component of type string.");
            }

            return component.getAsString();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final String value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value);

            return newInstance;
        }
    }

    public static final class BooleanConverter implements IUIElementDataComponentConverter<Boolean>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isBool();
        }

        @NotNull
        @Override
        public Boolean readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            if (!component.isBool())
            {
                throw new IllegalArgumentException("Required a component of type boolean.");
            }

            return component.getAsBoolean();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Boolean value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.BOOL);
            newInstance.setBoolean(value);

            return newInstance;
        }
    }

    public static final class DoubleConverter implements IUIElementDataComponentConverter<Double>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isNumber();
        }

        @NotNull
        @Override
        public Double readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            if (!component.isNumber())
            {
                throw new IllegalArgumentException("Required a component of type double.");
            }

            return component.getAsDouble();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Double value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setDouble(value);

            return newInstance;
        }
    }

    public static final class FloatConverter implements IUIElementDataComponentConverter<Float>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isNumber();
        }

        @NotNull
        @Override
        public Float readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            if (!component.isNumber())
            {
                throw new IllegalArgumentException("Required a component of type float.");
            }

            return component.getAsFloat();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Float value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setFloat(value);

            return newInstance;
        }
    }

    public static final class IntegerConverter implements IUIElementDataComponentConverter<Integer>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isNumber();
        }

        @NotNull
        @Override
        public Integer readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            if (!component.isNumber())
            {
                throw new IllegalArgumentException("Required a component of type integer.");
            }

            return component.getAsInteger();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Integer value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setInteger(value);

            return newInstance;
        }
    }

    public static final class IdentifierConverter implements IUIElementDataComponentConverter<IIdentifier>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public IIdentifier readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            return IIdentifier.create(component.getAsString());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final IIdentifier value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.toString());

            return newInstance;
        }
    }

    public static final class EnumValueConverter<E extends Enum<E>> implements IUIElementDataComponentConverter<E>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public E readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            final Class<E> clz = (Class<E>) params[0];
            return E.valueOf(clz, component.getAsString());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final E value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.name());

            return newInstance;
        }
    }

    public static final class EnumSetValueConverter<E extends Enum<E>> implements IUIElementDataComponentConverter<EnumSet<E>>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public EnumSet<E> readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            final Class<E> clz = (Class<E>) params[0];
            final String contents = component.getAsString();

            return Arrays.stream(contents.split(",")).map(s -> E.valueOf(clz, s)).collect(Collectors.toCollection(() -> EnumSet.noneOf(clz)));
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final EnumSet<E> value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.stream().map(Enum::name).reduce((s1, s2) -> s1 + "," + s2).orElse(""));

            return newInstance;
        }
    }

    public static final class AxisDistanceConverter implements IUIElementDataComponentConverter<AxisDistance>
    {
        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public AxisDistance readFromElement(@NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
        {
            final AxisDistanceBuilder builder = new AxisDistanceBuilder();
            builder.readFromString(sourceData.getMetaData().getParent().map(IUIElement::getElementSize).orElse(new Vector2d()), component.getAsString());

            return builder.build();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final AxisDistance value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.toString());

            return newInstance;
        }
    }

    public static final class AlignmentConverter implements IUIElementDataComponentConverter<EnumSet<Alignment>>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isNumber() || component.isString();
        }

        @NotNull
        @Override
        public EnumSet<Alignment> readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            return component.isNumber()
                     ? Alignment.fromInt(component.getAsInteger())
                     : Alignment.fromString(component.getAsString());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final EnumSet<Alignment> value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setInteger(Alignment.toInt(value));

            return newInstance;
        }
    }

    public static final class OrientationConverter implements IUIElementDataComponentConverter<Orientation>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public Orientation readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            return Orientation.fromString(component.getAsString());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Orientation value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.toString());

            return newInstance;
        }
    }

    public static final class Vector2dConverter implements IUIElementDataComponentConverter<Vector2d>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public Vector2d readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            return Vector2d.fromString(component.getAsString());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final Vector2d value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.toString());

            return newInstance;
        }
    }

    public static final class BoundingBoxConverter implements IUIElementDataComponentConverter<BoundingBox>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isString();
        }

        @NotNull
        @Override
        public BoundingBox readFromElement(@NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            return BoundingBox.fromString(component.getAsString());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final BoundingBox value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value.toString());

            return newInstance;
        }
    }

    public static final class DummyListContextConverter implements IUIElementDataComponentConverter<ArrayList>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isList();
        }

        @NotNull
        @Override
        public ArrayList readFromElement(
          @NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
        {
            return Lists.newArrayList();
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final ArrayList value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            return newComponentInstanceProducer.apply(ComponentType.LIST);
        }
    }

    private BaseValueComponentConverters() {}
}
