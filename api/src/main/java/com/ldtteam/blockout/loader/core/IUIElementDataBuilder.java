package com.ldtteam.blockout.loader.core;

import com.ldtteam.blockout.element.IUIElement;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public interface IUIElementDataBuilder<D extends IUIElementData>
{

    IUIElementDataBuilder<D> copyFrom(final IUIElementData<?> elementData);

    IUIElementDataBuilder<D> withMetaData(Consumer<IUIElementMetaDataBuilder<?>> builder);

    IUIElementDataBuilder<D> addChild(IUIElement elementToWrite);

    default <T> IUIElementDataBuilder<D> addComponent(String componentName, T value)
    {
        return this.addComponent(componentName, value, value.getClass());
    }

    <T> IUIElementDataBuilder<D> addComponent(String componentName, T value, Type typeToken);

    D build();
}
