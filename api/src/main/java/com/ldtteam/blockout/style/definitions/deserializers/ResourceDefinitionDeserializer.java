package com.ldtteam.blockout.style.definitions.deserializers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.ldtteam.blockout.style.definitions.ResourceDefinition;
import com.ldtteam.blockout.util.json.IdentifierDeserializer;
import com.ldtteam.minelaunch.util.IIdentifier;

import java.lang.reflect.Type;

public class ResourceDefinitionDeserializer implements JsonDeserializer<ResourceDefinition>
{
    public static final  Type                           CONST_RESOURCE_DEFINITION_TYPE = new TypeToken<ResourceDefinition>() {}.getType();
    private static final Gson                           CONST_GSON                     =
      new GsonBuilder().registerTypeAdapter(IdentifierDeserializer.CONST_IDENTIFIER_TYPE, IdentifierDeserializer.getInstance()).create();
    private static       ResourceDefinitionDeserializer ourInstance                    = new ResourceDefinitionDeserializer();

    private ResourceDefinitionDeserializer()
    {
    }

    public static ResourceDefinitionDeserializer getInstance()
    {
        return ourInstance;
    }

    @Override
    public ResourceDefinition deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            throw new JsonParseException("Resource definition data needs to be object with id and data.");
        }

        final JsonObject object = json.getAsJsonObject();

        if (!object.has("id"))
        {
            throw new JsonParseException("Resource definition data has no id.");
        }

        final IIdentifier id = CONST_GSON.fromJson(object.get("id"), IdentifierDeserializer.CONST_IDENTIFIER_TYPE);
        final JsonElement data = object.has("data") ? object.get("data") : new JsonObject();

        return new ResourceDefinition(id, data);
    }
}
