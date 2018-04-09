package com.minecolonies.blockout.builder.core.builder;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutGuiConstructionDataBuilder
{

    @NotNull
    IBlockOutGuiConstructionDataBuilder withDependency(@NotNull final String controlId, @NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency);

    @NotNull
    <S, A> IBlockOutGuiConstructionDataBuilder withEventHandler(
      @NotNull final String controlId, @NotNull final String eventName, @NotNull final Class<S> controlTypeClass, @NotNull final Class<A> argumentTypeClass, @NotNull final
    IEventHandler<S, A> eventHandler);

    @NotNull
    <T extends IUIElement, B extends IBlockOutUIElementConstructionDataBuilder<B, T>> B withControl(@NotNull final String controlId, @NotNull final Class<B> builderClass);

    @NotNull
    IBlockOutGuiConstructionData build();
}
