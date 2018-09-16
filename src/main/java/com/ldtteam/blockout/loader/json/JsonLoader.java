package com.ldtteam.blockout.loader.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ldtteam.blockout.loader.ILoader;
import com.ldtteam.blockout.loader.IUIElementData;
import org.jetbrains.annotations.NotNull;

public class JsonLoader implements ILoader
{
    @Override
    public IUIElementData createFromFile(@NotNull final String data)
    {
        try
        {
            final JsonParser parser = new JsonParser();
            final JsonElement element = parser.parse(data);
            return new JsonUIElementData(element.getAsJsonObject(), null);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Cannot parse JSON data.", ex);
        }

    }
}
