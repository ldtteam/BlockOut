package com.ldtteam.blockout.util;

import com.ldtteam.minelaunch.util.nbt.INBTBase;
import com.ldtteam.minelaunch.util.nbt.INBTByte;

public enum NBTType
{
    TAG_BYTE,
    TAG_SHORT,
    TAG_INT,
    TAG_LONG,
    TAG_FLOAT,
    TAG_DOUBLE,
    TAG_BYTE_ARRAY,
    TAG_STRING,
    TAG_LIST,
    TAG_COMPOUND;

    public static NBTType fromNBTBase(INBTBase base)
    {
        switch (base.getType())
        {
            case 1:
                return TAG_BYTE;
            case 2:
                return TAG_SHORT;
            case 3:
                return TAG_INT;
            case 4:
                return TAG_LONG;
            case 5:
                return TAG_FLOAT;
            case 6:
                return TAG_DOUBLE;
            case 7:
                return TAG_BYTE_ARRAY;
            case 8:
                return TAG_STRING;
            case 9:
                return TAG_LIST;
            case 10:
                return TAG_COMPOUND;
            default:
                throw new IllegalArgumentException("Unknown NBTBase type with Id: " + base.getType());
        }
    }
}
