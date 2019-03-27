package com.ldtteam.blockout.json;

import org.jetbrains.annotations.NotNull;

public class JsonLoader implements ILoader
{

    @NotNull
    @Override
    public IUIElementData loadDataFromDefinition(@NotNull final String data) throws Exception
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
