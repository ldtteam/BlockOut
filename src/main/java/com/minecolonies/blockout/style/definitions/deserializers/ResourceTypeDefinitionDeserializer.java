package com.minecolonies.blockout.style.definitions.deserializers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.minecolonies.blockout.style.definitions.ResourceDefinition;
import com.minecolonies.blockout.style.definitions.ResourceTypeDefinition;
import com.minecolonies.blockout.util.stream.StreamHelper;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

public class ResourceTypeDefinitionDeserializer implements JsonDeserializer<ResourceTypeDefinition>
{
    public static final  Type                               CONST_RESOURCE_TYPE_DEFINITION_TYPE = new TypeToken<ResourceTypeDefinition>() {}.getType();
    private static final Gson                               CONST_GSON                          = new GsonBuilder()
                                                                                                    .registerTypeAdapter(ResourceDefinitionDeserializer.CONST_RESOURCE_DEFINITION_TYPE,
                                                                                                      ResourceDefinitionDeserializer.getInstance())
                                                                                                    .create();
    private static       ResourceTypeDefinitionDeserializer ourInstance                         = new ResourceTypeDefinitionDeserializer();

    private ResourceTypeDefinitionDeserializer()
    {
    }

    public static ResourceTypeDefinitionDeserializer getInstance()
    {
        return ourInstance;
    }

    @Override
    public ResourceTypeDefinition deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            throw new JsonParseException("ResourceType Definition needs to be object with type id and array with resource data.");
        }

        final JsonObject object = json.getAsJsonObject();
        if (!object.has("id") || !object.get("id").isJsonPrimitive())
        {
            throw new JsonParseException("ResourceType Definition does not have an id string");
        }

        final String typeId = object.get("id").getAsString();

        if (!object.has("resources") || !object.get("resource").isJsonArray())
        {
            throw new JsonParseException("ResourceType Definition does not have a resource array.");
        }

        final JsonArray resources = object.get("resources").getAsJsonArray();
        final Collection<ResourceDefinition> resourceDefinitions =
          StreamHelper.getJsonArrayAsStream(resources)
            .map(e -> (ResourceDefinition) CONST_GSON.fromJson(e, ResourceDefinitionDeserializer.CONST_RESOURCE_DEFINITION_TYPE))
            .collect(Collectors.toList());

        return new ResourceTypeDefinition(typeId, resourceDefinitions);
    }
}
