package com.minecolonies.blockout.builder.data;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

public class BlockOutGuiConstructionData implements IBlockOutGuiConstructionData
{
    @Override
    public boolean hasDependencyData(@NotNull final String name)
    {
        return false;
    }

    @NotNull
    @Override
    public <T> IDependencyObject<T> get(
      @NotNull final String name, @NotNull final IDependencyObject<T> current, @NotNull final Type requestedType)
    {
        return null;
    }

    @NotNull
    @Override
    public <S, A> List<IEventHandler<S, A>> getEventHandlers(
      @NotNull final String id, @NotNull final Class<S> sourceClass, @NotNull final Class<A> argumentClass)
    {
        return null;
    }
}
