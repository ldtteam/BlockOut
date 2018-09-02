package com.minecolonies.blockout.util.kryo;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;

public final class BlockOutInstantiationStrategy implements InstantiatorStrategy
{
    private final LinkedHashSet<InstantiatorStrategy> strategies;

    public BlockOutInstantiationStrategy(final LinkedHashSet<InstantiatorStrategy> strategies) {this.strategies = strategies;}

    public BlockOutInstantiationStrategy(final InstantiatorStrategy... strategies)
    {
        this.strategies = new LinkedHashSet<>(Arrays.asList(strategies));
    }

    /**
     * Create a dedicated instantiator for the given class
     *
     * @param type Class that will be instantiated
     * @return Dedicated instantiator
     */
    @Override
    public <T> ObjectInstantiator<T> newInstantiatorOf(final Class<T> type)
    {
        return strategies.stream()
                 .map(s -> {
                     try
                     {
                         return s.newInstantiatorOf(type);
                     }
                     catch (Exception ignored)
                     {
                         return null;
                     }
                 })
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElseThrow(() -> new IllegalStateException("No instantiator strategy can create an instance of the type: " + type.toString()));
    }
}
