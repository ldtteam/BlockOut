package com.ldtteam.blockout.util.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.ldtteam.blockout.BlockOut;
import org.objenesis.strategy.SerializingInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

public final class KryoUtil
{

    private KryoUtil()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Creates a new kryo instance to use.
     */
    public static Kryo createNewKryo()
    {
        final Kryo instance = new Kryo();
        instance.setInstantiatorStrategy(
          new BlockOutInstantiationStrategy(
            new Kryo.DefaultInstantiatorStrategy(),
            new SerializingInstantiatorStrategy(),
            new StdInstantiatorStrategy()
          )
        );

        instance.setRegistrationRequired(false);
        if (BlockOut.getInstance() != null)
            instance.setClassLoader(BlockOut.getInstance().getClass().getClassLoader());

        return instance;
    }
}