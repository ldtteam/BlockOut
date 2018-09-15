package com.ldtteam.blockout.builder.core;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.dependency.injection.IDependencyDataProvider;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.event.injector.IEventHandlerProvider;

import java.util.List;
import java.util.Map;

public interface IBlockOutGuiConstructionData extends IDependencyDataProvider, IEventHandlerProvider
{

    /**
     * Getter for the contained dependency data.
     *
     * @return The dependency data.
     */
    Map<String, IDependencyObject<?>> getDependencyData();

    /**
     * Getter for the contained event data.
     *
     * @return The event data.
     */
    Map<String, Map<Class<?>, Map<Class<?>, List<IEventHandler<?, ?>>>>> getEventHandlerData();
}
