package com.ldtteam.blockout.style.definitions.deserializers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.ldtteam.blockout.style.definitions.StylesDefinition;
import com.ldtteam.blockout.util.json.IdentifierDeserializer;
import com.ldtteam.blockout.util.stream.StreamHelper;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

public class StylesDefinitionDeserializer implements JsonDeserializer<StylesDefinition>
{
    public static final  Type                         CONST_STYLES_DEFINITION_TYPE = new TypeToken<StylesDefinition>() {}.getType();
    private static final Gson                         CONST_GSON                   = new GsonBuilder()
                                                                                       .registerTypeAdapter(IdentifierDeserializer.CONST_IDENTIFIER_TYPE,
                                                                                         IdentifierDeserializer.getInstance())
                                                                                       .create();
    private static       StylesDefinitionDeserializer ourInstance                  = new StylesDefinitionDeserializer();

    private StylesDefinitionDeserializer()
    {
    }

    public static StylesDefinitionDeserializer getInstance()
    {
        return ourInstance;
    }

    @Override
    public StylesDefinition deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonArray())
        {
            throw new IllegalArgumentException("Styles definition needs to be array that points to styles.");
        }

        final JsonArray array = json.getAsJsonArray();
        final Collection<IIdentifier> styles =
          StreamHelper.getJsonArrayAsStream(array)
            .map(e -> (IIdentifier) CONST_GSON.fromJson(e, IdentifierDeserializer.CONST_IDENTIFIER_TYPE))
            .collect(Collectors.toList());

        return new StylesDefinition(styles);
    }
}
