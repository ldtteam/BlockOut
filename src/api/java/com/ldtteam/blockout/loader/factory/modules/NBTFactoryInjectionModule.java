package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.factory.NBTBaseConverter;
import com.ldtteam.blockout.loader.factory.NBTDependingConverters;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;


public class NBTFactoryInjectionModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagByte>>() {}).to(new TypeLiteral<NBTBaseConverter.ByteConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagByteArray>>() {}).to(new TypeLiteral<NBTBaseConverter.ByteArrayConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagCompound>>() {}).to(new TypeLiteral<NBTBaseConverter.CompoundConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagDouble>>() {}).to(new TypeLiteral<NBTBaseConverter.DoubleConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagFloat>>() {}).to(new TypeLiteral<NBTBaseConverter.FloatConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagInt>>() {}).to(new TypeLiteral<NBTBaseConverter.IntConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagList>>() {}).to(new TypeLiteral<NBTBaseConverter.ListConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagLong>>() {}).to(new TypeLiteral<NBTBaseConverter.LongConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagShort>>() {}).to(new TypeLiteral<NBTBaseConverter.ShortConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagString>>() {}).to(new TypeLiteral<NBTBaseConverter.StringConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<ItemStack>>() {}).to(new TypeLiteral<NBTDependingConverters.ItemStackConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<Entity>>() {}).to(new TypeLiteral<NBTDependingConverters.EntityConverter>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<IBlockState>>() {}).to(new TypeLiteral<NBTDependingConverters.BlockStateConverter>() {});
    }
}
