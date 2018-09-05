package com.minecolonies.blockout.binding.dependency.injection;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

public interface IDependencyDataProvider
{

    boolean hasDependencyData(@NotNull String name);

    @NotNull
    <T> IDependencyObject<T> get(@NotNull final String name);
}
