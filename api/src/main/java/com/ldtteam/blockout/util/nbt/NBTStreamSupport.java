package com.ldtteam.blockout.util.nbt;

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

    public static Stream<INBTBase> streamList(final INBTList list)
    {
        return list.stream();
    }

    public static Stream<Byte> streamByteArray(final INBTByteArray byteArray)
    {
        return IntStream.range(0, byteArray.getValue().length).mapToObj(i -> byteArray.getValue()[i]);
    }

    public static Stream<Map.Entry<String, INBTBase>> streamCompound(final INBTCompound compound)
    {
        return compound.keySet().stream().map(key -> new HashMap.SimpleImmutableEntry<>(key, compound.get(key)));
    }
}
