package com.ldtteam.blockout.reflection;

import java.util.Set;

/**
 * A manager that allows the extraction of the fields from class.
 */
public interface IReflectionManager
{
    /**
     * Returns a list of all accessible fast lookup field reflection entries for a given class.
     *
     * @param clz The class.
     * @return Its accessible fields.
     */
    Set<IFieldReflectionEntry> getFieldsForClass(Class<?> clz);
}
