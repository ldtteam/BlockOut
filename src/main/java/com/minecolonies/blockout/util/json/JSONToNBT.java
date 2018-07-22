package com.minecolonies.blockout.util.json;

import com.google.gson.JsonElement;
import com.minecolonies.blockout.util.NBTType;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JSONToNBT
{
    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN          = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN           = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN            = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN            = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN           = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN             = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

    private static final Map<Pattern, NBTType>                        TYPE_MATCHING_PATTERNS    = new LinkedHashMap<>();
    private static final Map<NBTType, Function<JsonElement, NBTBase>> TYPE_CONVERSION_FUNCTIONS = new EnumMap<>(NBTType.class);
    static
    {
        TYPE_MATCHING_PATTERNS.put(DOUBLE_PATTERN_NOSUFFIX, NBTType.TAG_DOUBLE);
        TYPE_MATCHING_PATTERNS.put(DOUBLE_PATTERN, NBTType.TAG_DOUBLE);
        TYPE_MATCHING_PATTERNS.put(FLOAT_PATTERN, NBTType.TAG_FLOAT);
        TYPE_MATCHING_PATTERNS.put(BYTE_PATTERN, NBTType.TAG_BYTE);
        TYPE_MATCHING_PATTERNS.put(LONG_PATTERN, NBTType.TAG_LONG);
        TYPE_MATCHING_PATTERNS.put(SHORT_PATTERN, NBTType.TAG_SHORT);
        TYPE_MATCHING_PATTERNS.put(INT_PATTERN, NBTType.TAG_INT);
    }

    static
    {
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE,
          (jsonElement) -> convertFromValue(jsonElement, (byteString) -> new NBTTagByte(Byte.parseByte(byteString.replace("b", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE_ARRAY, JSONToNBT::convertToByteArray);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_COMPOUND, JSONToNBT::convertToNBTTagCompound);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_DOUBLE,
          (jsonElement) -> convertFromValue(jsonElement, (doubleString) -> new NBTTagDouble(Double.parseDouble(doubleString.replace("d", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_FLOAT,
          (jsonElement) -> convertFromValue(jsonElement, (floatString) -> new NBTTagFloat(Float.parseFloat(floatString.replace("f", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_SHORT,
          (jsonElement) -> convertFromValue(jsonElement, (shortString) -> new NBTTagShort(Short.parseShort(shortString.replace("s", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_LONG,
          (jsonElement) -> convertFromValue(jsonElement, (longString) -> new NBTTagLong(Long.parseLong(longString.replace("l", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_INT, (jsonElement) -> convertFromValue(jsonElement, (intString) -> new NBTTagInt(Integer.parseInt(intString))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_LIST, JSONToNBT::convertToList);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_STRING, (jsonElement) -> convertFromValue(jsonElement, NBTTagString::new));
    }
    private JSONToNBT()
    {
        throw new IllegalArgumentException("Utility class: JSONToNBT");
    }

    /**
     * Maps a given XML JsonElement onto a NBTBase.
     *
     * @param jsonElement The jsonElement to map.
     * @return The mapped NBTBase.
     *
     * @throws IllegalArgumentException when the XML syntax does not match.
     */
    public static final NBTBase fromJSON(@NotNull final JsonElement jsonElement) throws IllegalArgumentException
    {
        return TYPE_CONVERSION_FUNCTIONS.get(getNBTType(jsonElement)).apply(jsonElement);
    }

    private static NBTType getNBTType(@NotNull final JsonElement jsonElement) throws IllegalArgumentException
    {
        if (jsonElement.isJsonArray())
        {
            final long childrenTypeCount = JSONStreamSupport.streamChildData(jsonElement)
                                             .map(JSONToNBT::getNBTType)
                                             .distinct()
                                             .count();

            if (childrenTypeCount == 1)
            {
                final NBTType type = JSONStreamSupport.streamChildData(jsonElement)
                                       .map(JSONToNBT::getNBTType)
                                       .distinct().findFirst()
                                       .get();
                if (type == NBTType.TAG_BYTE)
                {
                    return NBTType.TAG_BYTE_ARRAY;
                }

                return NBTType.TAG_LIST;
            }
        }

        if (jsonElement.isJsonObject())
        {
            return NBTType.TAG_COMPOUND;
        }

        return TYPE_MATCHING_PATTERNS
                 .entrySet()
                 .stream()
                 .filter(
                   tmpe -> tmpe
                             .getKey()
                             .matcher(jsonElement.getAsString())
                             .matches()
                 )
                 .map(Map.Entry::getValue)
                 .findFirst()
                 .orElse(NBTType.TAG_STRING);
    }

    private static NBTBase convertToByteArray(@NotNull final JsonElement jsonElement)
    {
        return new NBTTagByteArray(
          JSONStreamSupport.streamChildData(jsonElement)
            .map(TYPE_CONVERSION_FUNCTIONS.get(NBTType.TAG_BYTE)::apply)
            .map(nbtBase -> (NBTTagByte) nbtBase)
            .map(NBTTagByte::getByte)
            .collect(Collectors.toList())
        );
    }

    private static <T extends NBTBase> NBTBase convertFromValue(@NotNull final JsonElement jsonElement, @NotNull final Function<String, T> converter)
    {
        if (!jsonElement.isJsonPrimitive())
        {
            throw new IllegalArgumentException("No primitive given for primitive value parsing");
        }

        return converter.apply(jsonElement.getAsString());
    }

    private static NBTBase convertToList(@NotNull final JsonElement jsonElement)
    {
        final NBTTagList list = new NBTTagList();

        JSONStreamSupport.streamChildData(jsonElement)
          .map(child -> TYPE_CONVERSION_FUNCTIONS.get(getNBTType(child)).apply(child))
          .forEach(list::appendTag);

        return list;
    }

    private static final NBTBase convertToNBTTagCompound(@NotNull final JsonElement jsonElement)
    {
        final NBTTagCompound compound = new NBTTagCompound();

        JSONStreamSupport.streamObject(jsonElement)
          .forEach(child -> {
              final NBTBase nbtBase = TYPE_CONVERSION_FUNCTIONS.get(getNBTType(child.getValue())).apply(child.getValue());
              final String name = child.getKey();

              if (compound.hasKey(name))
              {
                  throw new IllegalArgumentException(String.format("Given jsonElement contains multiple entries with the same key: %s", name));
              }

              compound.setTag(name, nbtBase);
          });

        return compound;
    }
}
