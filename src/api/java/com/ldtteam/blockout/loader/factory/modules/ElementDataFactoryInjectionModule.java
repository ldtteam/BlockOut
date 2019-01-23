package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.factory.ElementDataComponentConverters;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;

import java.util.List;

import static com.ldtteam.blockout.util.Constants.ConverterTypes.CHILDREN_LIST_FACTORY_TYPE;

public class ElementDataFactoryInjectionModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<IUIElementData<?>>>() {}).to(ElementDataComponentConverters.ElementDataConverter.class);
        bind((Key<IUIElementDataComponentConverter<List<IUIElementData<?>>>>) Key.get(CHILDREN_LIST_FACTORY_TYPE)).to(ElementDataComponentConverters.ListElementDataConverter.class);
    }
}
