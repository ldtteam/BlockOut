package com.ldtteam.blockout.loader.core;

import java.util.function.Consumer;

public interface IUIElementDataBuilder<D extends IUIElementData>
{

    IUIElementDataBuilder<D> copyFrom(final IUIElementData<?> elementData);

    IUIElementDataBuilder<D> withMetaData(Consumer<IUIElementMetaDataBuilder<?>> builder);

    IUIElementDataBuilder<D> addChild(D elementData);

    <T> IUIElementDataBuilder<D> addComponent(String componentName, T value);

    D build();
}
