package com.ldtteam.blockout.loader.object;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.IUIElementMetaDataBuilder;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ObjectUIElementMetaDataBuilder implements IUIElementMetaDataBuilder<ObjectUIElementMetaData>
{
    @NotNull
    private final Map<String, ObjectUIElementDataComponent> componentMap = Maps.newHashMap();
    @NotNull
    private final Injector                                  injector;

    public ObjectUIElementMetaDataBuilder()
    {
        this(Guice.createInjector(BlockOut.getBlockOut().getProxy().getFactoryInjectionModules()));
    }

    public ObjectUIElementMetaDataBuilder(@NotNull final Injector injector)
    {
        this.injector = injector;
    }

    @Override
    public IUIElementMetaDataBuilder withId(@NotNull final String string)
    {
        componentMap.put(Constants.Controls.General.CONST_ID, new ObjectUIElementDataComponent(string, injector));
        return this;
    }

    @Override
    public IUIElementMetaDataBuilder withType(@NotNull final ResourceLocation type)
    {
        final IUIElementDataComponentConverter<ResourceLocation> factory = injector.getInstance(Key.get(new TypeLiteral<IUIElementDataComponentConverter<ResourceLocation>>() {}));
        final ObjectUIElementDataComponent component = factory.writeToElement(type, (c) -> new ObjectUIElementDataComponent(""));

        componentMap.put(Constants.Controls.General.CONST_ID, component);

        return this;
    }

    @Override
    public ObjectUIElementMetaData build()
    {
        return new ObjectUIElementMetaData(componentMap, null, injector);
    }
}
