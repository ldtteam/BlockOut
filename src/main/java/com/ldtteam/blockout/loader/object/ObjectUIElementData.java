package com.ldtteam.blockout.loader.object;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ldtteam.blockout.json.JsonUIElementDataComponent;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.proxy.ProxyHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public class ObjectUIElementData implements IUIElementData, Serializable
{
    @NotNull
    private final Map<String, IUIElementDataComponent>         object;
    @NotNull
    private final IUIElementMetaData metaData;
    @NotNull
    private final Injector           injector;

    public ObjectUIElementData(@NotNull final Map<String, IUIElementDataComponent> object, @NotNull final IUIElementMetaData metaData) {
        this(object, metaData, Guice.createInjector(ProxyHolder.getInstance().getFactoryInjectionModules()));
    }

    public ObjectUIElementData(@NotNull final Map<String, IUIElementDataComponent> object, @NotNull final IUIElementMetaData metaData, @NotNull final Injector injector) {
        this.object = object;
        this.metaData = metaData;
        this.injector = injector;
    }

    @Override
    public Injector getFactoryInjector()
    {
        return injector;
    }

    @NotNull
    @Override
    public IUIElementMetaData getMetaData()
    {
        return metaData;
    }

    @Nullable
    @Override
    public Optional<IUIElementDataComponent> getComponentWithName(@NotNull final String name)
    {
        if (object.containsKey(name))
            return Optional.of(new ObjectUIElementDataComponent(object.get(name)));

        return Optional.empty();
    }
}
