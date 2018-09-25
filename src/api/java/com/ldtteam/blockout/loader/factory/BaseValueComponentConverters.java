package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public final class BaseValueComponentConverters
{

    private BaseValueComponentConverters() {}

    public static final class StringConverter implements IUIElementDataComponentConverter<String>
    {

        @Override
        public String readFromElement(@NotNull final IUIElementDataComponent component)
        {
            if(!component.isString())
                throw new IllegalArgumentException("Required a component of type string.");
            
            return component.getAsString();
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final String value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final IUIElementDataComponent newInstance = newComponentInstanceProducer.apply(ComponentType.STRING);
            newInstance.setString(value);
            
            return newInstance;
        }
    }

    public static final class BooleanConverter implements IUIElementDataComponentConverter<Boolean>
    {

        @Override
        public Boolean readFromElement(@NotNull final IUIElementDataComponent component)
        {
            if(!component.isBool())
                throw new IllegalArgumentException("Required a component of type boolean.");

            return component.getAsBoolean();
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final Boolean value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final IUIElementDataComponent newInstance = newComponentInstanceProducer.apply(ComponentType.BOOL);
            newInstance.setBoolean(value);

            return newInstance;
        }
    }

    public static final class DoubleConverter implements IUIElementDataComponentConverter<Double>
    {

        @Override
        public Double readFromElement(@NotNull final IUIElementDataComponent component)
        {
            if(!component.isNumber())
                throw new IllegalArgumentException("Required a component of type double.");

            return component.getAsDouble();
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final Double value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final IUIElementDataComponent newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setDouble(value);

            return newInstance;
        }
    }

    public static final class FloatConverter implements IUIElementDataComponentConverter<Float>
    {

        @Override
        public Float readFromElement(@NotNull final IUIElementDataComponent component)
        {
            if(!component.isNumber())
                throw new IllegalArgumentException("Required a component of type float.");

            return component.getAsFloat();
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final Float value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final IUIElementDataComponent newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setFloat(value);

            return newInstance;
        }
    }

    public static final class IntegerConverter implements IUIElementDataComponentConverter<Integer>
    {

        @Override
        public Integer readFromElement(@NotNull final IUIElementDataComponent component)
        {
            if(!component.isNumber())
                throw new IllegalArgumentException("Required a component of type integer.");

            return component.getAsInteger();
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final Integer value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final IUIElementDataComponent newInstance = newComponentInstanceProducer.apply(ComponentType.NUMBER);
            newInstance.setInteger(value);

            return newInstance;
        }
    }

    public static final class ListConverter implements IUIElementDataComponentConverter<List<IUIElementDataComponent>>
    {

        @Override
        public List<IUIElementDataComponent> readFromElement(@NotNull final IUIElementDataComponent component)
        {
            if(!component.isList())
                throw new IllegalArgumentException("Required a component of type list.");

            return component.getAsList();
        }

        @Override
        public IUIElementDataComponent writeToElement(
          @NotNull final List<IUIElementDataComponent> value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
        {
            final IUIElementDataComponent newInstance = newComponentInstanceProducer.apply(ComponentType.LIST);
            newInstance.setList(value);

            return newInstance;
        }
    }
}
