package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.NBTType;
import com.ldtteam.blockout.util.elementdata.IUIElementDataComponentStreamSupport;
import com.ldtteam.blockout.util.nbt.NBTStreamSupport;
import net.minecraft.nbt.*;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class NBTBaseConverter<T extends INBT> implements IUIElementDataComponentConverter<T>
{
    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN          = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN           = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN            = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN            = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN           = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN             = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

    private static final Map<Pattern, NBTType>                                                                                                   TYPE_MATCHING_PATTERNS    =
      new LinkedHashMap<>();
    private static final Map<NBTType, Function<IUIElementDataComponent, INBT>>                                                               TYPE_CONVERSION_FUNCTIONS =
      new EnumMap<>(NBTType.class);
    private static final Map<NBTType, BiFunction<INBT, Function<ComponentType, ? extends IUIElementDataComponent>, IUIElementDataComponent>> NBT_CONVERSION_FUNCTIONS  =
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
          (component) -> convertToValue(component, (byteString) -> ByteNBT.valueOf(Byte.parseByte(byteString.replace("b", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE_ARRAY, NBTBaseConverter::convertToByteArray);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_COMPOUND, NBTBaseConverter::convertToNBTTagCompound);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_DOUBLE,
          (component) -> convertToValue(component, (doubleString) -> DoubleNBT.valueOf(Double.parseDouble(doubleString.replace("d", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_FLOAT,
          (component) -> convertToValue(component, (floatString) -> FloatNBT.valueOf(Float.parseFloat(floatString.replace("f", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_SHORT,
          (component) -> convertToValue(component, (shortString) -> ShortNBT.valueOf(Short.parseShort(shortString.replace("s", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_LONG,
          (component) -> convertToValue(component, (longString) -> LongNBT.valueOf(Long.parseLong(longString.replace("l", "")))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_INT,
          (component) -> convertToValue(component, (intString) -> IntNBT.valueOf(Integer.parseInt(intString))));
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_LIST, NBTBaseConverter::convertToList);
        TYPE_CONVERSION_FUNCTIONS.put(NBTType.TAG_STRING, (component) -> convertToValue(component, StringNBT::valueOf));
    }

    static
    {
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_BYTE_ARRAY, NBTBaseConverter::convertFromByteArray);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_COMPOUND, NBTBaseConverter::convertFromCompound);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_DOUBLE, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_FLOAT, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_INT, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_LONG, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_SHORT, NBTBaseConverter::convertFromValue);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_LIST, NBTBaseConverter::convertFromList);
        NBT_CONVERSION_FUNCTIONS.put(NBTType.TAG_STRING, NBTBaseConverter::convertFromValue);
    }

    public static final class ByteConverter extends NBTBaseConverter<ByteNBT>
    {
        public ByteConverter()
        {
            super(NBTType.TAG_BYTE);
        }
    }

    public static final class ShortConverter extends NBTBaseConverter<ShortNBT>
    {
        public ShortConverter()
        {
            super(NBTType.TAG_SHORT);
        }
    }

    public static final class IntConverter extends NBTBaseConverter<IntNBT>
    {
        public IntConverter()
        {
            super(NBTType.TAG_INT);
        }
    }

    public static final class LongConverter extends NBTBaseConverter<LongNBT>
    {
        public LongConverter()
        {
            super(NBTType.TAG_LONG);
        }
    }

    public static final class FloatConverter extends NBTBaseConverter<FloatNBT>
    {
        public FloatConverter()
        {
            super(NBTType.TAG_FLOAT);
        }
    }

    public static final class DoubleConverter extends NBTBaseConverter<DoubleNBT>
    {
        public DoubleConverter()
        {
            super(NBTType.TAG_DOUBLE);
        }
    }

    public static final class ByteArrayConverter extends NBTBaseConverter<ByteArrayNBT>
    {
        public ByteArrayConverter()
        {
            super(NBTType.TAG_BYTE_ARRAY);
        }
    }

    public static final class StringConverter extends NBTBaseConverter<StringNBT>
    {
        public StringConverter()
        {
            super(NBTType.TAG_STRING);
        }
    }

    public static final class ListConverter extends NBTBaseConverter<ListNBT>
    {
        public ListConverter()
        {
            super(NBTType.TAG_LIST);
        }
    }

    public static final class CompoundConverter extends NBTBaseConverter<CompoundNBT>
    {
        public CompoundConverter()
        {
            super(NBTType.TAG_COMPOUND);
        }
    }

    private final NBTType type;

    protected NBTBaseConverter(final NBTType type) {this.type = type;}

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

    private static <T extends INBT> INBT convertToValue(@NotNull final IUIElementDataComponent component, @NotNull final Function<String, T> converter)
    {
        return converter.apply(component.getAsString());
    }

    private static INBT convertToByteArray(@NotNull final IUIElementDataComponent component)
    {
        final List<Byte> bytes = IUIElementDataComponentStreamSupport.streamList(component)
                                   .map(TYPE_CONVERSION_FUNCTIONS.get(NBTType.TAG_BYTE)::apply)
                                   .map(nbtBase -> (ByteNBT) nbtBase)
                                   .map(ByteNBT::getByte)
                                   .collect(Collectors.toList());

        final byte[] byteArray = ArrayUtils.toPrimitive(bytes.toArray(new Byte[] {}));

        final ByteArrayNBT list = new ByteArrayNBT(byteArray);

        return list;
    }

    private static INBT convertToList(@NotNull final IUIElementDataComponent component)
    {
        final ListNBT list = new ListNBT();
        list.addAll(IUIElementDataComponentStreamSupport.streamList(component)
                      .map(child -> TYPE_CONVERSION_FUNCTIONS.get(getNBTType(child)).apply(child))
                      .collect(Collectors.toList()));

        return list;
    }

    private static final INBT convertToNBTTagCompound(@NotNull final IUIElementDataComponent component)
    {
        final CompoundNBT compound = new CompoundNBT();

        IUIElementDataComponentStreamSupport.streamMap(component)
          .forEach(child -> {
              final INBT nbtBase = TYPE_CONVERSION_FUNCTIONS.get(getNBTType(child.getValue())).apply(child.getValue());
              final String name = child.getKey();

              if (compound.contains(name))
              {
                  throw new IllegalArgumentException(String.format("Given jsonElement contains multiple entries with the same key: %s", name));
              }

              compound.put(name, nbtBase);
          });

        return compound;
    }

    private static <C extends IUIElementDataComponent> C convertFromValue(@NotNull final INBT base, Function<ComponentType, C> newInstanceCreator)
    {
        final C newInstance = newInstanceCreator.apply(ComponentType.STRING);
        newInstance.setString(base.toString());

        return newInstance;
    }

    private static <C extends IUIElementDataComponent> C convertFromByteArray(INBT base, Function<ComponentType, C> newInstanceCreator)
    {
        final ByteArrayNBT byteArray = (ByteArrayNBT) base;
        final C newInstance = newInstanceCreator.apply(ComponentType.LIST);

        newInstance.setList(NBTStreamSupport.streamByteArray(byteArray)
                              .map(ByteNBT::valueOf)
                              .map(tag -> NBT_CONVERSION_FUNCTIONS.get(NBTType.TAG_BYTE).apply(tag, newInstanceCreator))
                              .collect(Collectors.toList()));

        return newInstance;
    }

    private static <C extends IUIElementDataComponent> C convertFromList(INBT base, Function<ComponentType, C> newInstanceCreator)
    {
        final ListNBT listTag = (ListNBT) base;
        final C newInstance = newInstanceCreator.apply(ComponentType.LIST);

        newInstance.setList(NBTStreamSupport.streamList(listTag)
                              .map(b -> NBT_CONVERSION_FUNCTIONS.get(NBTType.fromNBTBase(b)).apply(b, newInstanceCreator))
                              .collect(Collectors.toList()));

        return newInstance;
    }

    private static <C extends IUIElementDataComponent> C convertFromCompound(INBT base, Function<ComponentType, C> newInstanceCreator)
    {
        final CompoundNBT compoundTag = (CompoundNBT) base;
        final C newInstance = newInstanceCreator.apply(ComponentType.COMPLEX);

        newInstance.setMap(NBTStreamSupport.streamCompound(compoundTag)
                             .map(entry -> new HashMap.SimpleImmutableEntry<>(entry.getKey(),
                               NBT_CONVERSION_FUNCTIONS.get(NBTType.fromNBTBase(entry.getValue())).apply(entry.getValue(), newInstanceCreator)))
                             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return newInstance;
    }

    @Override
    public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
    {
        return getNBTType(component) == type;
    }

    @NotNull
    @Override
    public T readFromElement(
      @NotNull final IUIElementDataComponent component, @Nullable final IUIElementData sourceData, @NotNull final Object... params)
    {
        return (T) TYPE_CONVERSION_FUNCTIONS.get(getNBTType(component)).apply(component);
    }

    @Override
    public <C extends IUIElementDataComponent> C writeToElement(
      @NotNull final T value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
    {
        return (C) NBT_CONVERSION_FUNCTIONS.get(NBTType.fromNBTBase(value)).apply(value, newComponentInstanceProducer);
    }
}
