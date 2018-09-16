package com.ldtteam.blockout.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ldtteam.test.AbstractBlockOutTest;
import net.minecraft.nbt.*;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONToNBTTest extends AbstractBlockOutTest
{
    @Test
    public void fromJSONDoubleNoSuffix()
    {
        final JsonElement doubleNoSuffix = getJsonElementForTest("/JSONtoNBT/double_no_suffix.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(doubleNoSuffix);

        assertTrue("Parsed NBT is not a Double", parsedNBT instanceof NBTTagDouble);

        NBTTagDouble parsedDouble = (NBTTagDouble) parsedNBT;
        assertEquals(0.0102503625d, parsedDouble.getDouble(), 0.00000001d);
    }

    private JsonElement getJsonElementForTest(@NotNull final String path)
    {
        final InputStream stream = getClass().getResourceAsStream(path);
        final JsonParser parser = new JsonParser();
        return parser.parse(new InputStreamReader(stream)).getAsJsonObject().get("test");
    }

    @Test
    public void fromJSONDoubleWithSuffix()
    {
        final JsonElement doubleNoSuffix = getJsonElementForTest("/JSONtoNBT/double_with_suffix.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(doubleNoSuffix);

        assertTrue("Parsed NBT is not a Double", parsedNBT instanceof NBTTagDouble);

        NBTTagDouble parsedDouble = (NBTTagDouble) parsedNBT;
        assertEquals(0.123456789d, parsedDouble.getDouble(), 0.00000001d);
    }

    @Test
    public void fromJSONFloatWithSuffix()
    {
        final JsonElement floatNoSuffix = getJsonElementForTest("/JSONtoNBT/float_with_suffix.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(floatNoSuffix);

        assertTrue("Parsed NBT is not a Float", parsedNBT instanceof NBTTagFloat);

        NBTTagFloat parsedFloat = (NBTTagFloat) parsedNBT;
        assertEquals(0.123456789f, parsedFloat.getFloat(), 0.00000001f);
    }

    @Test
    public void fromJSONByte()
    {
        final JsonElement byteJson = getJsonElementForTest("/JSONtoNBT/byte.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(byteJson);

        assertTrue("Parsed NBT is not a Byte", parsedNBT instanceof NBTTagByte);

        NBTTagByte parsedByte = (NBTTagByte) parsedNBT;
        assertEquals(122, parsedByte.getByte());
    }

    @Test
    public void fromJSONLong()
    {
        final JsonElement longJson = getJsonElementForTest("/JSONtoNBT/long.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(longJson);

        assertTrue("Parsed NBT is not a Long", parsedNBT instanceof NBTTagLong);

        NBTTagLong parsedLong = (NBTTagLong) parsedNBT;
        assertEquals(1256489563256899L, parsedLong.getLong());
    }

    @Test
    public void fromJSONShort()
    {
        final JsonElement shortJson = getJsonElementForTest("/JSONtoNBT/short.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(shortJson);

        assertTrue("Parsed NBT is not a Short", parsedNBT instanceof NBTTagShort);

        NBTTagShort parsedShort = (NBTTagShort) parsedNBT;
        assertEquals(1001, parsedShort.getShort());
    }

    @Test
    public void fromJSONInt()
    {
        final JsonElement intJson = getJsonElementForTest("/JSONtoNBT/int.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(intJson);

        assertTrue("Parsed NBT is not a Int", parsedNBT instanceof NBTTagInt);

        NBTTagInt parsedInt = (NBTTagInt) parsedNBT;
        assertEquals(120123658, parsedInt.getInt());
    }

    @Test
    public void fromJSONByteArray()
    {
        final JsonElement byteArrayJson = getJsonElementForTest("/JSONtoNBT/byteArray.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(byteArrayJson);

        assertTrue("Parsed NBT is not a ByteArray", parsedNBT instanceof NBTTagByteArray);

        NBTTagByteArray parsedByteArray = (NBTTagByteArray) parsedNBT;
        Assert.assertThat(new Byte[] {1, 2, 3, 4, 5}, IsEqual.equalTo(parsedByteArray.getByteArray()));
    }

    @Test
    public void fromJSONList()
    {
        final JsonElement listJson = getJsonElementForTest("/JSONtoNBT/list.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(listJson);

        assertTrue("Parsed NBT is not a List", parsedNBT instanceof NBTTagList);

        NBTTagList parsedList = (NBTTagList) parsedNBT;
        NBTTagList shouldBeList = new NBTTagList();
        shouldBeList.appendTag(new NBTTagString("bla"));
        shouldBeList.appendTag(new NBTTagString("blabla"));
        shouldBeList.appendTag(new NBTTagString("blablabla"));
        shouldBeList.appendTag(new NBTTagString("blablablabla"));
        shouldBeList.appendTag(new NBTTagString("blablablablabla"));

        assertEquals("Parsedlist does not contain correct amount.", shouldBeList.tagCount(), parsedList.tagCount());

        for (int i = 0; i < parsedList.tagCount(); i++)
        {
            Assert.assertThat(shouldBeList.get(i), IsEqual.equalTo(parsedList.get(i)));
        }
    }

    @Test
    public void fromJSONCompound()
    {
        final JsonElement listJson = getJsonElementForTest("/JSONtoNBT/compound_correct.json");
        final NBTBase parsedNBT = JSONToNBT.fromJSON(listJson);

        assertTrue("Parsed NBT is not a Compound", parsedNBT instanceof NBTTagCompound);

        NBTTagCompound parsedCompound = (NBTTagCompound) parsedNBT;
        NBTTagCompound shouldBeCompound = new NBTTagCompound();
        shouldBeCompound.setTag("keyOne", new NBTTagString("bla"));
        shouldBeCompound.setTag("keyTwo", new NBTTagString("blabla"));
        shouldBeCompound.setTag("keyThree", new NBTTagString("blablabla"));
        shouldBeCompound.setTag("keyFour", new NBTTagString("blablablabla"));
        shouldBeCompound.setTag("keyFive", new NBTTagString("blablablablabla"));

        Assert.assertThat(shouldBeCompound.getKeySet(), IsEqual.equalTo(parsedCompound.getKeySet()));

        parsedCompound.getKeySet().forEach(key -> {
            Assert.assertThat(parsedCompound.getTag(key), IsEqual.equalTo(shouldBeCompound.getTag(key)));
        });
    }
}
