package com.minecolonies.test;

import com.minecolonies.blockout.util.Log;
import net.minecraft.item.ItemStack;
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
@PrepareForTest({Log.class, ItemStack.class})
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

    public String getTestName()
    {
        return this.getClass().getName();
    }

    public Random getRandom()
    {
        return random;
    }
}