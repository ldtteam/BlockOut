package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.factory.ElementDataComponentConverters;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;

import java.util.List;

public class ElementDataFactoryInjectionModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<IUIElementData<?>>>() {}).to(ElementDataComponentConverters.ElementDataConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<List<IUIElementData<?>>>>() {}).to(ElementDataComponentConverters.ListElementDataConverter.class);
    }
}
