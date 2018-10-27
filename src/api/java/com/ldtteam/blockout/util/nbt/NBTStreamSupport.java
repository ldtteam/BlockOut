package com.ldtteam.blockout.util.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class NBTStreamSupport
{

    private NBTStreamSupport()
    {
        throw new IllegalStateException("Tried to initialize: NBTStreamSupport but this is a Utility class.");
    }

    public static Stream<NBTBase> streamList(final NBTTagList list)
    {
        return IntStream.range(0, list.tagCount()).mapToObj(list::get);
    }

    public static Stream<Byte> streamByteArray(final NBTTagByteArray byteArray)
    {
        return IntStream.range(0, byteArray.getByteArray().length).mapToObj(i -> byteArray.getByteArray()[i]);
    }

    public static Stream<Map.Entry<String, NBTBase>> streamCompound(final NBTTagCompound compound)
    {
        return compound.getKeySet().stream().map(key -> new HashMap.SimpleImmutableEntry<>(key, compound.getTag(key)));
    }
}
