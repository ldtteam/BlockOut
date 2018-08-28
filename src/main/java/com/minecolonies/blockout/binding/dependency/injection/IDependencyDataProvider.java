package com.minecolonies.blockout.binding.dependency.injection;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface IDependencyDataProvider
{

    boolean hasDependencyData(@NotNull final String name, @NotNull final Class<? extends IDependencyObject> searchedType);

    @NotNull
    <T> IDependencyObject<T> get(@NotNull final String name, @NotNull final IDependencyObject<T> current, @NotNull final Type requestedType);
}
