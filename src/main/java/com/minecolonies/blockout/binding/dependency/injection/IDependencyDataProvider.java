package com.minecolonies.blockout.binding.dependency.injection;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface IDependencyDataProvider
{

    boolean has(@NotNull final String name);

    @NotNull
    <C, T> IDependencyObject<C, T> get(@NotNull final String name, @NotNull final IDependencyObject<C, T> current, @NotNull final Type requestedType);
}
