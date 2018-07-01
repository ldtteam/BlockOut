package com.minecolonies.blockout.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class ResourceLocationDeserializer implements JsonDeserializer<ResourceLocation>
{
    public static final Type                         CONST_RESOURCE_LOCATION_TYPE = new TypeToken<ResourceLocation>() {}.getType();
    private static      ResourceLocationDeserializer ourInstance                  = new ResourceLocationDeserializer();

    private ResourceLocationDeserializer()
    {
    }

    public static ResourceLocationDeserializer getInstance()
    {
        return ourInstance;
    }

    @Override
    public ResourceLocation deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonPrimitive())
        {
            throw new JsonParseException("ResourceLocation needs to be string.");
        }

        return new ResourceLocation(json.getAsString());
    }
}
