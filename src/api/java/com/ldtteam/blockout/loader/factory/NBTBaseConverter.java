package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.NBTType;
import com.ldtteam.blockout.util.elementdata.IUIElementDataComponentStreamSupport;
import com.ldtteam.blockout.util.nbt.NBTStreamSupport;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NBTBaseConverter<T extends NBTBase> implements IUIElementDataComponentConverter<T>
{
    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN          = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN           = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN            = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN            = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN           = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN             = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

    private static final Map<Pattern, NBTType>                                                                                        TYPE_MATCHING_PATTERNS    =
      new LinkedHashMap<>();
    private static final Map<NBTType, Function<IUIElementDataComponent, NBTBase>>                                                     TYPE_CONVERSION_FUNCTIONS =
      new EnumMap<>(NBTType.class);
    private static final Map<NBTType, BiFunction<NBTBase, Function<ComponentType, IUIElementDataComponent>, IUIElementDataComponent>> NBT_CONVERSION_FUNCTIONS  =
      new EnumMap<>(NBTType.class);
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
          (component) -> convertToValue(component, (byteString) -> new NBTTagByte(Byte.parseByte(byteString.replace("b", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE_ARRAY, NBTBaseConverter::convertToByteArray);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_COMPOUND, NBTBaseConverter::convertToNBTTagCompound);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_DOUBLE,
          (component) -> convertToValue(component, (doubleString) -> new NBTTagDouble(Double.parseDouble(doubleString.replace("d", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_FLOAT,
          (component) -> convertToValue(component, (floatString) -> new NBTTagFloat(Float.parseFloat(floatString.replace("f", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_SHORT,
          (component) -> convertToValue(component, (shortString) -> new NBTTagShort(Short.parseShort(shortString.replace("s", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_LONG,
          (component) -> convertToValue(component, (longString) -> new NBTTagLong(Long.parseLong(longString.replace("l", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_INT, (component) -> convertToValue(component, (intString) -> new NBTTagInt(Integer.parseInt(intString))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_LIST, NBTBaseConverter::convertToList);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_STRING, (component) -> convertToValue(component, NBTTagString::new));
    }

    static
    {
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE_ARRAY, NBTBaseConverter::convertFromByteArray);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_COMPOUND, NBTBaseConverter::convertFromCompount);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_DOUBLE, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_FLOAT, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_INT, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_LONG, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_SHORT, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_LIST, NBTBaseConverter::convertFromList);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_STRING, NBTBaseConverter::convertFromValue);
    }


    @NotNull
    @Override
    public T readFromElement(
      @NotNull final IUIElementDataComponent component, @NotNull final IUIElementData sourceData, @NotNull final Object... params)
    {
        return (T) TYPE_CONVERSION_FUNCTIONS.get(getNBTType(component)).apply(component);
    }

    @Override
    public IUIElementDataComponent writeToElement(
      @NotNull final T value, @NotNull final Function<ComponentType, IUIElementDataComponent> newComponentInstanceProducer)
    {
        return NBT_CONVERSION_FUNCTIONS.get(NBTType.fromNBTBase(value)).apply(value, newComponentInstanceProducer);
    }

    private static NBTType getNBTType(@NotNull final IUIElementDataComponent component) throws IllegalArgumentException
    {
        if (component.isList())
        {
            final long childrenTypeCount = IUIElementDataComponentStreamSupport.streamList(component)
                                             .map(NBTBaseConverter::getNBTType)
                                             .distinct()
                                             .count();

            if (childrenTypeCount == 1)
            {
                final NBTType type = IUIElementDataComponentStreamSupport.streamList(component)
                                       .map(NBTBaseConverter::getNBTType)
                                       .distinct().findFirst()
                                       .get();
                if (type == NBTType.TAG_BYTE)
                {
                    return NBTType.TAG_BYTE_ARRAY;
                }

                return NBTType.TAG_LIST;
            }
        }

        if (component.isComplex())
        {
            return NBTType.TAG_COMPOUND;
        }

        return TYPE_MATCHING_PATTERNS
                 .entrySet()
                 .stream()
                 .filter(
                   tmpe -> tmpe
                             .getKey()
                             .matcher(component.getAsString())
                             .matches()
                 )
                 .map(Map.Entry::getValue)
                 .findFirst()
                 .orElse(NBTType.TAG_STRING);
    }

    private static <T extends NBTBase> NBTBase convertToValue(@NotNull final IUIElementDataComponent component, @NotNull final Function<String, T> converter)
    {
        return converter.apply(component.getAsString());
    }

    private static NBTBase convertToByteArray(@NotNull final IUIElementDataComponent component)
    {
        return new NBTTagByteArray(
          IUIElementDataComponentStreamSupport.streamList(component)
            .map(TYPE_CONVERSION_FUNCTIONS.get(NBTType.TAG_BYTE)::apply)
            .map(nbtBase -> (NBTTagByte) nbtBase)
            .map(NBTTagByte::getByte)
            .collect(Collectors.toList())
        );
    }

    private static NBTBase convertToList(@NotNull final IUIElementDataComponent component)
    {
        final NBTTagList list = new NBTTagList();

        IUIElementDataComponentStreamSupport.streamList(component)
          .map(child -> TYPE_CONVERSION_FUNCTIONS.get(getNBTType(child)).apply(child))
          .forEach(list::appendTag);

        return list;
    }

    private static final NBTBase convertToNBTTagCompound(@NotNull final IUIElementDataComponent component)
    {
        final NBTTagCompound compound = new NBTTagCompound();

        IUIElementDataComponentStreamSupport.streamMap(component)
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

    private static IUIElementDataComponent convertFromValue(@NotNull final NBTBase base, Function<ComponentType, IUIElementDataComponent> newInstanceCreator)
    {
        final IUIElementDataComponent newInstance = newInstanceCreator.apply(ComponentType.STRING);
        newInstance.setString(base.toString());

        return newInstance;
    }

    private static IUIElementDataComponent convertFromByteArray(NBTBase base, Function<ComponentType, IUIElementDataComponent> newInstanceCreator)
    {
        final NBTTagByteArray byteArray = (NBTTagByteArray) base;
        final IUIElementDataComponent newInstance = newInstanceCreator.apply(ComponentType.LIST);

        newInstance.setList(NBTStreamSupport.streamByteArray(byteArray)
                              .map(NBTTagByte::new)
                              .map(tag -> NBT_CONVERSION_FUNCTIONS.get(NBTType.TAG_BYTE).apply(tag, newInstanceCreator))
                              .collect(Collectors.toList()));

        return newInstance;
    }

    private static IUIElementDataComponent convertFromList(NBTBase base, Function<ComponentType, IUIElementDataComponent> newInstanceCreator)
    {
        final NBTTagList listTag = (NBTTagList) base;
        final IUIElementDataComponent newInstance = newInstanceCreator.apply(ComponentType.LIST);

        newInstance.setList(NBTStreamSupport.streamList(listTag)
                              .map(b -> NBT_CONVERSION_FUNCTIONS.get(NBTType.fromNBTBase(b)).apply(b, newInstanceCreator))
                              .collect(Collectors.toList()));

        return newInstance;
    }

    private static IUIElementDataComponent convertFromCompount(NBTBase base, Function<ComponentType, IUIElementDataComponent> newInstanceCreator)
    {
        final NBTTagCompound compoundTag = (NBTTagCompound) base;
        final IUIElementDataComponent newInstance = newInstanceCreator.apply(ComponentType.COMPLEX);

        newInstance.setMap(NBTStreamSupport.streamCompound(compoundTag)
                             .map(entry -> new HashMap.SimpleImmutableEntry<>(entry.getKey(),
                               NBT_CONVERSION_FUNCTIONS.get(NBTType.fromNBTBase(entry.getValue())).apply(entry.getValue(), newInstanceCreator)))
                             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return newInstance;
    }
}
