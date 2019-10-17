package com.ldtteam.blockout.util.nbt;

import net.minecraft.nbt.*;

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

    public static Stream<INBT> streamList(final ListNBT list)
    {
        return list.stream();
    }

    public static Stream<Byte> streamByteArray(final ByteArrayNBT byteArray)
    {
        return byteArray.stream().map(ByteNBT::getByte);
    }

    public static Stream<Map.Entry<String, INBT>> streamCompound(final CompoundNBT compound)
    {
        return compound.keySet().stream().map(key -> new HashMap.SimpleImmutableEntry<>(key, compound.get(key)));
    }
}
