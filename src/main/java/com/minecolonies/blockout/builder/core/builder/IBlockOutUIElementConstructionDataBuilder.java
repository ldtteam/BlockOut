package com.minecolonies.blockout.builder.core.builder;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutUIElementConstructionDataBuilder<C extends IBlockOutUIElementConstructionDataBuilder<C, T>, T extends IUIElement>
{

    @NotNull
    C withDependency(@NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency);

    @NotNull
    <A> C withEventHandler(
      @NotNull final String eventName, @NotNull final Class<A> argumentTypeClass, @NotNull final
    IEventHandler<T, A> eventHandler);

    @NotNull
    IBlockOutGuiConstructionDataBuilder done();
}
