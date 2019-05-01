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

    /**
     * Returns a list off super types of from a given class, including the class itself.
     */
    <A> Set<Class<? super A>> getAllSupers(Class<? super A> clz);
}
