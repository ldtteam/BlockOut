package com.ldtteam.blockout.loader.factory.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.loader.factory.NBTBaseConverter;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import net.minecraft.nbt.*;

public class NBTFactoryInjectionModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTBase>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTBase>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagByte>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagByte>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagByteArray>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagByteArray>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagCompound>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagCompound>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagDouble>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagDouble>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagFloat>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagFloat>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagInt>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagInt>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagList>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagList>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagLong>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagLong>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagShort>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagShort>>() {});
        bind(new TypeLiteral<IUIElementDataComponentConverter<NBTTagString>>() {}).to(new TypeLiteral<NBTBaseConverter<NBTTagString>>() {});
    }
}
