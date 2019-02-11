package com.ldtteam.blockout.reflection;

import java.lang.reflect.Type;

public interface IFieldReflectionEntry
{
    Object get(Object from) throws IllegalAccessException;

    void set(Object to, Object value) throws IllegalAccessException;

    String getName();

    Type getType();
}
