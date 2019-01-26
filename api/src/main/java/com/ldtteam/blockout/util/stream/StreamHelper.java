package com.ldtteam.blockout.util.stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StreamHelper
{

    private StreamHelper()
    {
        throw new IllegalArgumentException("Utility class: StreamHelper");
    }

    public static Stream<JsonElement> getJsonArrayAsStream(final JsonArray array)
    {
        return StreamSupport.stream(array.spliterator(), false);
    }
}