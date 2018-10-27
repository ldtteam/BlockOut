package com.ldtteam.blockout.json;

import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.proxy.ProxyHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.Option;
import java.util.Optional;

public class JsonUIElementData implements IUIElementData
{
    @NotNull
    private final JsonObject object;
    @NotNull
    private final IUIElementMetaData metaData;
    @NotNull
    private final Injector injector;

    public JsonUIElementData(
      @NotNull final JsonObject object,
      @Nullable IUIElementHost parent)
    {
        this(object, parent, Guice.createInjector(ProxyHolder.getInstance().getFactoryInjectionModules()));
    }

    public JsonUIElementData(
      @NotNull final JsonObject object,
      @Nullable IUIElementHost parent,
      @NotNull final Injector injector)
    {
        this.object = object;
        this.metaData = new JsonUIElementMetaData(object, parent);
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
        if (object.has(name))
            return Optional.of(new JsonUIElementDataComponent(object.get(name), injector));

        return Optional.empty();
    }
}
