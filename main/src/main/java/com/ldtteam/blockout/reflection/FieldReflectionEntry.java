package com.ldtteam.blockout.reflection;

import com.esotericsoftware.reflectasm.FieldAccess;

import java.lang.reflect.Type;

/**
 * Special reflection helper class to access a field in a given class accessor.
 */
public class FieldReflectionEntry implements IFieldReflectionEntry
{

    private final FieldAccess access;
    private final String      name;
    private final int         index;
    private final Type        type;

    FieldReflectionEntry(final FieldAccess access, final String name, final int index, final Type type)
    {
        this.access = access;
        this.name = name;
        this.index = index;
        this.type = type;
    }

    @Override
    public Object get(final Object from) throws IllegalAccessException
    {
        return access.get(from, index);
    }

    @Override
    public void set(final Object to, final Object value) throws IllegalAccessException
    {
        access.set(to, index, value);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Type getType()
    {
        return type;
    }
}
