package com.minecolonies.blockout.loader.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonLoader implements ILoader
{
    @Override
    public boolean accepts(@NotNull final ResourceLocation location)
    {
        return location.getResourcePath().toLowerCase().endsWith(".xml");
    }

    @Override
    public IUIElementData createFromFile(@NotNull final ResourceLocation location)
    {
        final InputStream inputStream;
        try
        {
            inputStream = BlockOut.getBlockOut().getProxy().getResourceStream(location);
        }
        catch (Exception e)
        {
            Log.getLogger().error("Failed to load UI from json file: " + location + ". Could not find resource.", e);
            return null;
        }

        final JsonParser parser = new JsonParser();

        final JsonElement element = parser.parse(new InputStreamReader(inputStream));
        if (!element.isJsonObject())
        {
            Log.getLogger().error("Failed to load UI from json file: " + location + ". Stored JSON element is no Object");
            return null;
        }

        return new JsonUIElementData(element.getAsJsonObject(), null);
    }
}
