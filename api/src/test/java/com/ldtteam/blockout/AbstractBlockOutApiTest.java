package com.ldtteam.blockout;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.google.common.collect.Sets;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.reflection.IFieldReflectionEntry;
import com.ldtteam.blockout.reflection.IReflectionManager;
import com.ldtteam.jvoxelizer.core.provider.holder.ProviderResolver;
import com.ldtteam.jvoxelizer.util.tuple.ITuple;
import com.ldtteam.jvoxelizer.util.tuple.ITupleProvider;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * This base test class sets up BlockOuts api with the required classes in a mock fashion.
 * Generally certain implementations exist in sort of a duplicate fashion, however generally without performance improvements like caching.
 */
public class AbstractBlockOutApiTest
{

    /**
     * Sets up the {@link ProxyHolder} by creating a mocked {@link IProxy} and mocking its methods.
     * This serves as a replacement for the setup done by BlockOut during pre-init.
     */
    @Before
    public final void MockProxyHolder()
    {
        IProxy proxy = Mockito.mock(IProxy.class);
        IReflectionManager reflectionManager = Mockito.mock(IReflectionManager.class);

        ///Mock the reflection manager result simply without caching it.
        when(reflectionManager.getFieldsForClass(any())).then((Answer<Set<IFieldReflectionEntry>>) invocation -> {
            final Class<?> clz = (Class<?>) invocation.getArguments()[0];

            final FieldAccess access = FieldAccess.get(clz);
            final String[] fieldNames = access.getFieldNames();
            final Class[] fieldTypes = access.getFieldTypes();

            final Set<IFieldReflectionEntry> entries = Sets.newHashSet();

            for (int j = 0; j < access.getFieldCount(); j++)
            {
                final int i = j;

                entries.add(new IFieldReflectionEntry()
                {
                    @Override
                    public Object get(final Object from)
                    {
                        return access.get(from, i);
                    }

                    @Override
                    public void set(final Object to, final Object value)
                    {
                        access.set(to, i, value);
                    }

                    @Override
                    public String getName()
                    {
                        return fieldNames[i];
                    }

                    @Override
                    public Type getType()
                    {
                        return fieldTypes[i];
                    }
                });
            }

            return entries;
        });

        ///Have the proxy return the mocked reflection manager
        when(proxy.getReflectionManager()).thenReturn(reflectionManager);

        ///Set the proxy-holders proxy.
        ProxyHolder.getInstance().setProxy(proxy);
    }

    /**
     * This sets up all required tests for vanilla minecraft.
     */
    @Before
    public void setupVanillaMinecraft()
    {
    }

    @Before
    public void setupJVoxelizer()
    {
        //TODO: Create proper test harness
        ProviderResolver.getInstance().registerProvider(ITuple.class.getName(), new ITupleProvider()
        {
            @Override
            public <A, B> ITuple<A, B> provide(final A a, final B b)
            {
                return new ITuple<A, B>()
                {
                    @Override
                    public A getFirst()
                    {
                        return a;
                    }

                    @Override
                    public B getSecond()
                    {
                        return b;
                    }
                };
            }
        });
    }
}
