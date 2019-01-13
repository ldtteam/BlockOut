package com.ldtteam.blockout.loader.object;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.proxy.ProxyHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.Transient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectUIElementData implements IUIElementData<ObjectUIElementDataComponent>, Serializable
{
    @NotNull
    private final Map<String, ObjectUIElementDataComponent>         object;
    @NotNull
    private final ObjectUIElementMetaData metaData;
    @NotNull
    private final transient Injector           injector;

    public ObjectUIElementData(@NotNull final Map<String, ObjectUIElementDataComponent> object, @NotNull final ObjectUIElementMetaData metaData) {
        this(object, metaData, Guice.createInjector(ProxyHolder.getInstance().getFactoryInjectionModules()));
    }

    public ObjectUIElementData(@NotNull final Map<String, ObjectUIElementDataComponent> object, @NotNull final ObjectUIElementMetaData metaData, @NotNull final Injector injector) {
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
    public Optional<ObjectUIElementDataComponent> getComponentWithName(@NotNull final String name)
    {
        if (object.containsKey(name))
            return Optional.of(new ObjectUIElementDataComponent(object.get(name), injector));

        return Optional.empty();
    }

    @Override
    public <D extends IUIElementDataComponent> D toDataComponent(@NotNull final D toWriteInto)
    {
        final Map<String, ObjectUIElementDataComponent> dataMap = new HashMap<>(this.object);
        this.metaData.mergeData(dataMap);

        toWriteInto.setMap(dataMap);

        return toWriteInto;
    }
}
