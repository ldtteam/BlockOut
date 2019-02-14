package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.factory.NBTBaseConverter;
import com.ldtteam.blockout.loader.factory.NBTDependingConverters;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.minelaunch.block.state.IBlockState;
import com.ldtteam.minelaunch.entity.IEntity;
import com.ldtteam.minelaunch.item.IItemStack;
import com.ldtteam.minelaunch.util.nbt.*;

public class NBTFactoryInjectionModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTByte>>() {}).to(new TypeLiteral<NBTBaseConverter.ByteConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTByteArray>>() {}).to(new TypeLiteral<NBTBaseConverter.ByteArrayConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTCompound>>() {}).to(new TypeLiteral<NBTBaseConverter.CompoundConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTDouble>>() {}).to(new TypeLiteral<NBTBaseConverter.DoubleConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTFloat>>() {}).to(new TypeLiteral<NBTBaseConverter.FloatConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTInteger>>() {}).to(new TypeLiteral<NBTBaseConverter.IntConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTList>>() {}).to(new TypeLiteral<NBTBaseConverter.ListConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTLong>>() {}).to(new TypeLiteral<NBTBaseConverter.LongConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTShort>>() {}).to(new TypeLiteral<NBTBaseConverter.ShortConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<INBTString>>() {}).to(new TypeLiteral<NBTBaseConverter.StringConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<IItemStack>>() {}).to(new TypeLiteral<NBTDependingConverters.ItemStackConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<IEntity>>() {}).to(new TypeLiteral<NBTDependingConverters.EntityConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<IBlockState>>() {}).to(new TypeLiteral<NBTDependingConverters.BlockStateConverter>() {});
    }
}
