package com.ldtteam.blockout.loader.object;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.loader.core.IUIElementDataBuilder;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaDataBuilder;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.stream.CollectorHelper;
import com.ldtteam.blockout.util.stream.FunctionHelper;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ObjectUIElementBuilder implements IUIElementDataBuilder<ObjectUIElementData>
{
    private ObjectUIElementMetaData metaData;
    private Map<String, ObjectUIElementDataComponent> attributes = new HashMap<>();
    private final List<ObjectUIElementData>                 children = Lists.newArrayList();
    private final Injector                                  injector;

    public ObjectUIElementBuilder()
    {
        this(Guice.createInjector(BlockOut.getBlockOut().getProxy().getFactoryInjectionModules()));
    }

    public ObjectUIElementBuilder(final Injector injector) {this.injector = injector;}

    @Override
    public IUIElementDataBuilder<ObjectUIElementData> copyFrom(final IUIElementData<?> elementData)
    {
        final ObjectUIElementDataComponent elementDataComponent = elementData.toDataComponent(new ObjectUIElementDataComponent());

        this.attributes = elementDataComponent.getAsMap().entrySet().stream().map(FunctionHelper::<String, IUIElementDataComponent, ObjectUIElementDataComponent>castMapValue).collect(CollectorHelper.entryToMapCollector());
        this.metaData = new ObjectUIElementMetaData(attributes, null);

        this.children.clear();

        return this;
    }

    @Override
    public IUIElementDataBuilder<ObjectUIElementData> withMetaData(final Consumer<IUIElementMetaDataBuilder<?>> builder)
    {
        final ObjectUIElementMetaDataBuilder metaDataBuilder = new ObjectUIElementMetaDataBuilder();
        builder.accept(metaDataBuilder);

        this.metaData = metaDataBuilder.build();

        return this;
    }

    @Override
    public IUIElementDataBuilder<ObjectUIElementData> addChild(final ObjectUIElementData elementData)
    {
        this.children.add(elementData);
        return this;
    }

    @Override
    public <T> IUIElementDataBuilder<ObjectUIElementData> addComponent(final String componentName, final T value)
    {
        final IUIElementDataComponentConverter<T> factory = injector.getInstance(Key.get(new TypeLiteral<IUIElementDataComponentConverter<T>>() {}));
        final ObjectUIElementDataComponent component = factory.writeToElement(value, c-> new ObjectUIElementDataComponent());

        attributes.put(componentName, component);

        return this;
    }

    @Override
    public ObjectUIElementData build()
    {
        final ObjectUIElementDataComponent childrenComponent = new ObjectUIElementDataComponent();
        final List<IUIElementDataComponent> childrenData = children.stream().map(objectUIElementData -> objectUIElementData.toDataComponent(new ObjectUIElementDataComponent())).collect(Collectors.toList());

        if (attributes.containsKey(Constants.Controls.General.CONST_CHILDREN))
            childrenData.addAll(attributes.get(Constants.Controls.General.CONST_CHILDREN).getAsList());

        childrenComponent.setList(childrenData);
        attributes.put(Constants.Controls.General.CONST_CHILDREN, childrenComponent);

        return new ObjectUIElementData(attributes, metaData);
    }
}