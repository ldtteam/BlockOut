package com.ldtteam.blockout.util.stream;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CollectorHelper
{

    private CollectorHelper()
    {
        throw new IllegalStateException("Tried to initialize: CollectorHelper but this is a Utility class.");
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> entryToMapCollector()
    {
        return Collectors.toMap(
          Map.Entry::getKey,
          Map.Entry::getValue
        );
    }
}
