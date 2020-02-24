package com.ldtteam.blockout.loader.object;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.core.IUIElementMetaDataBuilder;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ObjectUIElementMetaDataBuilder implements IUIElementMetaDataBuilder<ObjectUIElementMetaData>
{
    @NotNull
    private final Map<String, ObjectUIElementDataComponent> componentMap = Maps.newHashMap();

    public ObjectUIElementMetaDataBuilder()
    {

    }

    @Override
    public IUIElementMetaDataBuilder<ObjectUIElementMetaData> withId(@NotNull final String string)
    {
        componentMap.put(Constants.Controls.General.CONST_ID, new ObjectUIElementDataComponent(string));
        return this;
    }

    @Override
    public IUIElementMetaDataBuilder<ObjectUIElementMetaData> withType(@NotNull final String type)
    {
        final IUIElementDataComponentConverter<String> factory =
          ProxyHolder.getInstance().getInjector().getInstance(Key.get(new TypeLiteral<IUIElementDataComponentConverter<String>>() {}));
        final ObjectUIElementDataComponent component = factory.writeToElement(type, (c) -> new ObjectUIElementDataComponent(""));

        componentMap.put(Constants.Controls.General.CONST_TYPE, component);

        return this;
    }

    @Override
    public ObjectUIElementMetaData build()
    {
        return new ObjectUIElementMetaData(componentMap, null);
    }
}
