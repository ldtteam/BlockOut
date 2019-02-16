package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.element.values.Orientation;
import com.ldtteam.blockout.loader.factory.BaseValueComponentConverters;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;

import java.util.ArrayList;
import java.util.EnumSet;

public class BaseFactoryInjectionModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<String>>() {}).to(BaseValueComponentConverters.StringConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Boolean>>() {}).to(BaseValueComponentConverters.BooleanConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Double>>() {}).to(BaseValueComponentConverters.DoubleConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Float>>() {}).to(BaseValueComponentConverters.FloatConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Integer>>() {}).to(BaseValueComponentConverters.IntegerConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<IIdentifier>>() {}).to(BaseValueComponentConverters.IdentifierConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<AxisDistance>>() {}).to(BaseValueComponentConverters.AxisDistanceConverter.class);
        bind((Key<IUIElementDataComponentConverter<EnumSet<Alignment>>>) Key.get(Constants.ConverterTypes.ALIGNMENT_ENUMSET_FACTORY_TYPE)).to(BaseValueComponentConverters.AlignmentConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Orientation>>() {}).to(BaseValueComponentConverters.OrientationConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Vector2d>>() {}).to(BaseValueComponentConverters.Vector2dConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<BoundingBox>>() {}).to(BaseValueComponentConverters.BoundingBoxConverter.class);
        bind(new TypeLiteral<IUIElementDataComponentConverter<Dock>>() {}).to(new TypeLiteral<BaseValueComponentConverters.EnumValueConverter<Dock>>() {});
        bind((Key<IUIElementDataComponentConverter<EnumSet<Dock>>>) Key.get(Constants.ConverterTypes.DOCK_ENUMSET_FACTORY_TYPE)).to(new TypeLiteral<BaseValueComponentConverters.EnumSetValueConverter<Dock>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<ArrayList>>() {}).to(BaseValueComponentConverters.DummyListContextConverter.class);
    }
}
