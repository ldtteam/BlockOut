package com.ldtteam.test;

import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.core.provider.holder.ProviderResolver;
import net.minecraft.util.ResourceLocation;
import com.ldtteam.jvoxelizer.util.identifier.ResourceLocationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Random;

import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Abstract test class to abstract away some common uses functionality in Tests
 */
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public abstract class AbstractBlockOutTest
{

    private Random random;

    @Before
    public void setupStaticMocks() throws Exception
    {
        mockStatic(Log.class);

        final Logger logger = LogManager.getLogger(getTestName());
        random = new Random(getTestName().hashCode());

        doReturn(logger).when(Log.class, "getLogger");
    }

    @Before
    public void setupJVoxelizerProviders()
    {
        ProviderResolver.getInstance().registerProvider(ResourceLocation.class.getName(), new ResourceLocationProvider()
        {
            @Override
            public ResourceLocation provide(final String s, final String s1)
            {
                return new ResourceLocation()
                {
                    @Override
                    public String getDomain()
                    {
                        return s;
                    }

                    @Override
                    public String getPath()
                    {
                        return s1;
                    }
                };
            }

            @Override
            public ResourceLocation provide(final String s)
            {
                final String[] sComp = s.split(":");
                final String domain = sComp.length == 1 ? "minecraft" : sComp[0];
                final String path = sComp.length == 1 ? sComp[0] : sComp[1];

                return new ResourceLocation()
                {
                    @Override
                    public String getDomain()
                    {
                        return domain;
                    }

                    @Override
                    public String getPath()
                    {
                        return path;
                    }
                };
            }
        });
    }

    public String getTestName()
    {
        return this.getClass().getName();
    }

    public Random getRandom()
    {
        return random;
    }
}