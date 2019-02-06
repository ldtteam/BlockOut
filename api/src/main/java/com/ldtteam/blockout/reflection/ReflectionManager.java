package com.ldtteam.blockout.reflection;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.ldtteam.blockout.util.Log;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ReflectionManager
{

    private static ReflectionManager                          ourInstance = new ReflectionManager();
    private final  Cache<Class<?>, Set<FieldReflectionEntry>> FIELD_CACHE = CacheBuilder.newBuilder().maximumSize(1000000000).build();

    private ReflectionManager()
    {
    }

    public static ReflectionManager getInstance()
    {
        return ourInstance;
    }

    /**
     * Returns a list of all accessible fast lookup field reflection entries for a given class.
     *
     * @param clz The class.
     * @return Its accessible fields.
     */
    public Set<FieldReflectionEntry> getFieldsForClass(final Class<?> clz)
    {
        try
        {
            return FIELD_CACHE.get(clz, () -> buildFieldCacheForClass(clz));
        }
        catch (ExecutionException e)
        {
            Log.getLogger().error("Failed to build field cache for class: " + clz.getName() + ", aborted.", e);
            return Sets.newHashSet();
        }
    }

    private Set<FieldReflectionEntry> buildFieldCacheForClass(final Class<?> clz)
    {
        final FieldAccess access = FieldAccess.get(clz);
        final String[] fieldNames = access.getFieldNames();
        final Class[] fieldTypes = access.getFieldTypes();

        final Set<FieldReflectionEntry> entries = Sets.newHashSet();

        for (int i = 0; i < access.getFieldCount(); i++)
        {
            entries.add(new FieldReflectionEntry(access, fieldNames[i], i, fieldTypes[i]));
        }

        return entries;
    }
}
