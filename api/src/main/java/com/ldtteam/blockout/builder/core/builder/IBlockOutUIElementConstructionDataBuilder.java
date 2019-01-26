package com.ldtteam.blockout.builder.core.builder;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutUIElementConstructionDataBuilder<C extends IBlockOutUIElementConstructionDataBuilder<C, T>, T extends IUIElement>
{

    @NotNull
    C withDependency(@NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency);

    @NotNull
    <A> C withEventHandler(
      @NotNull final String eventName, @NotNull final Class<A> argumentTypeClass, @NotNull final
    IEventHandler<T, A> eventHandler);
}
