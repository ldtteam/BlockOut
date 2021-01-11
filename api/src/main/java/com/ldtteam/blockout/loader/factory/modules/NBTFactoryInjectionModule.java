package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.factory.NBTBaseConverter;
import com.ldtteam.blockout.loader.factory.NBTDependingConverters;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

public class NBTFactoryInjectionModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<ByteNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.ByteConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<ByteArrayNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.ByteArrayConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<CompoundNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.CompoundConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<DoubleNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.DoubleConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<FloatNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.FloatConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<IntNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.IntConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<ListNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.ListConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<LongNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.LongConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<ShortNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.ShortConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<StringNBT>>() {}).to(new TypeLiteral<NBTBaseConverter.StringConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<ItemStack>>() {}).to(new TypeLiteral<NBTDependingConverters.ItemStackConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<Entity>>() {}).to(new TypeLiteral<NBTDependingConverters.EntityConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<BlockState>>() {}).to(new TypeLiteral<NBTDependingConverters.BlockStateConverter>() {});
    }
}
