package com.ldtteam.blockout.connector.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.ldtteam.blockout.connector.core.IUIElementFactoryController;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.loader.binding.engine.SimpleBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementDataBuilder;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaDataBuilder;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.object.ObjectUIElementBuilder;
import com.ldtteam.blockout.loader.object.ObjectUIElementData;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CommonFactoryController implements IUIElementFactoryController
{
    private final BiMap<ResourceLocation, IUIElementFactory<?>> factoryBiMap = HashBiMap.create();

    @Override
    public IUIElementFactoryController registerFactory(@NotNull final IUIElementFactory<?> factory)
    {
        factoryBiMap.forcePut(factory.getType(), factory);
        return this;
    }

    @NotNull
    @Override
    public IUIElement getElementFromData(@NotNull final IUIElementData data)
    {
        final ResourceLocation type = data.getMetaData().getType();

        if (!factoryBiMap.containsKey(type))
        {
            throw new IllegalArgumentException("Unknown type is contained in the given data.");
        }

        return factoryBiMap.get(type).readFromElementData(data, SimpleBindingEngine.getInstance());
    }

    @NotNull
    @Override
    public <T extends IUIElement> IUIElementData getDataFromElement(@NotNull final T element)
    {
        return getDataFromElementWithBuilder(element, new ObjectUIElementBuilder());
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T extends IUIElement, C extends IUIElementDataComponent, D extends IUIElementData<C>, B extends IUIElementDataBuilder<D>> D getDataFromElementWithBuilder(
      @NotNull final T element, @NotNull final B builder)
    {
        final ResourceLocation type = element.getType();

        if (!factoryBiMap.containsKey(type))
        {
            throw new IllegalArgumentException("Unknown type is contained in the given data.");
        }

        final IUIElementFactory<T> factory = (IUIElementFactory<T>) factoryBiMap.get(type);

        builder.withMetaData(buildMetaDataBuilder(element));
        factory.writeToElementData(element, builder);

        return builder.build();
    }

    private <T extends IUIElement> Consumer<IUIElementMetaDataBuilder<?>> buildMetaDataBuilder(@NotNull final T element)
    {
        return (metaDataBuilder) -> metaDataBuilder.withId(element.getId()).withType(element.getType());
    }
}
