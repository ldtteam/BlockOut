package com.minecolonies.blockout.binding.dependency.injection;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

public interface IInjectionController
{

    <C, T> IDependencyObject<T> get(@NotNull final IDependencyObject<T> current, @NotNull final IDependencyDataProvider provider);
}
