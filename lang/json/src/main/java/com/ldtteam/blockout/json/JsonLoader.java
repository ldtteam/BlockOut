package com.ldtteam.blockout.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ldtteam.blockout.loader.ILoader;
import com.ldtteam.blockout.loader.core.IUIElementData;
import org.jetbrains.annotations.NotNull;

public class JsonLoader implements ILoader
{

    @NotNull
    @Override
    public IUIElementData<?> loadDataFromDefinition(@NotNull final String data) throws Exception
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
