package com.ldtteam.blockout.reflection;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.ldtteam.blockout.util.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Manager that handles cached reflection.
 */
public class ReflectionManager implements IReflectionManager
{
    private static ReflectionManager                           ourInstance = new ReflectionManager();
    private final  Cache<Class<?>, Set<IFieldReflectionEntry>> FIELD_CACHE = CacheBuilder.newBuilder().maximumSize(1000000).build();
    private final  Cache<Class<?>, Set<Class<?>>> SUPERS_CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    private ReflectionManager()
    {
    }

    /**
     * The instance of the manager.
     *
     * @return The instance.
     */
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
    @Override
    public Set<IFieldReflectionEntry> getFieldsForClass(final Class<?> clz)
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

    /**
     * Returns a list off super types of from a given class, including the class itself.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <A> Set<Class<? super A>> getAllSupers(final Class<? super A> clz)
    {
        try
        {
            return SUPERS_CACHE.get(clz, () -> this.getAllSupersInternal(clz)).stream().map(c -> (Class<? super A>) c).collect(Collectors.toSet());
        }
        catch (ExecutionException e)
        {
            Log.getLogger().error("Failed to retrieve all supers of: " + clz.getName());
            return ImmutableSet.of(clz, Object.class);
        }
    }

    private Set<Class<?>> getAllSupersInternal(final Class<?> clz)
    {
        if (clz == Object.class)
            return ImmutableSet.of(Object.class);

        final Set<Class<?>> supers = Sets.newHashSet();
        supers.addAll(this.getAllSupersInternal(clz.getSuperclass()));
        Arrays.stream(clz.getInterfaces()).forEach(i -> {
            supers.addAll(this.getAllSupersInternal(i));
        });

        supers.add(clz);

        return supers;
    }

    private Set<IFieldReflectionEntry> buildFieldCacheForClass(final Class<?> clz)
    {
        final FieldAccess access = FieldAccess.get(clz);
        final String[] fieldNames = access.getFieldNames();
        final Class<?>[] fieldTypes = access.getFieldTypes();

        final Set<IFieldReflectionEntry> entries = Sets.newHashSet();

        for (int i = 0; i < access.getFieldCount(); i++)
        {
            entries.add(new FieldReflectionEntry(access, fieldNames[i], i, fieldTypes[i]));
        }

        return entries;
    }
}
