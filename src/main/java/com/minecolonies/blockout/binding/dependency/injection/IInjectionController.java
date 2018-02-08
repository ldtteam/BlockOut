package com.minecolonies.blockout.binding.dependency.injection;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

public interface IInjectionController
{

    <C, T> IDependencyObject<C, T> get(@NotNull final IDependencyObject<C, T> current, @NotNull final IDependencyDataProvider provider);
}
