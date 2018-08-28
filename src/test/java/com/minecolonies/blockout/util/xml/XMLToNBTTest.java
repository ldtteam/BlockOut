package com.minecolonies.blockout.util.xml;

import com.minecolonies.test.AbstractBlockOutTest;
import net.minecraft.nbt.*;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XMLToNBTTest extends AbstractBlockOutTest
{

    private Node getXMLNodeForTest(@NotNull final String path) throws ParserConfigurationException, IOException, SAXException
    {
        final InputStream stream = getClass().getResourceAsStream(path);

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder;
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        return documentBuilder.parse(stream).getFirstChild();
    }

    @Test
    public void fromXMLDoubleNoSuffix() throws IOException, SAXException, ParserConfigurationException
    {
        final Node doubleNoSuffix = getXMLNodeForTest("/XMLtoNBT/double_no_suffix.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(doubleNoSuffix);

        assertTrue("Parsed NBT is not a Double", parsedNBT instanceof NBTTagDouble);

        NBTTagDouble parsedDouble = (NBTTagDouble) parsedNBT;
        assertEquals(0.0102503625d, parsedDouble.getDouble(), 0.00000001d);
    }

    @Test
    public void fromXMLDoubleWithSuffix() throws IOException, SAXException, ParserConfigurationException
    {
        final Node doubleNoSuffix = getXMLNodeForTest("/XMLtoNBT/double_with_suffix.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(doubleNoSuffix);

        assertTrue("Parsed NBT is not a Double", parsedNBT instanceof NBTTagDouble);

        NBTTagDouble parsedDouble = (NBTTagDouble) parsedNBT;
        assertEquals(0.123456789d, parsedDouble.getDouble(), 0.00000001d);
    }

    @Test
    public void fromXMLFloatWithSuffix() throws IOException, SAXException, ParserConfigurationException
    {
        final Node floatNoSuffix = getXMLNodeForTest("/XMLtoNBT/float_with_suffix.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(floatNoSuffix);

        assertTrue("Parsed NBT is not a Float", parsedNBT instanceof NBTTagFloat);

        NBTTagFloat parsedFloat = (NBTTagFloat) parsedNBT;
        assertEquals(0.123456789f, parsedFloat.getFloat(), 0.00000001f);
    }

    @Test
    public void fromXMLByte() throws IOException, SAXException, ParserConfigurationException
    {
        final Node byteXml = getXMLNodeForTest("/XMLtoNBT/byte.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(byteXml);

        assertTrue("Parsed NBT is not a Byte", parsedNBT instanceof NBTTagByte);

        NBTTagByte parsedByte = (NBTTagByte) parsedNBT;
        assertEquals(122, parsedByte.getByte());
    }

    @Test
    public void fromXMLLong() throws IOException, SAXException, ParserConfigurationException
    {
        final Node longXml = getXMLNodeForTest("/XMLtoNBT/long.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(longXml);

        assertTrue("Parsed NBT is not a Long", parsedNBT instanceof NBTTagLong);

        NBTTagLong parsedLong = (NBTTagLong) parsedNBT;
        assertEquals(1256489563256899L, parsedLong.getLong());
    }

    @Test
    public void fromXMLShort() throws IOException, SAXException, ParserConfigurationException
    {
        final Node shortXml = getXMLNodeForTest("/XMLtoNBT/short.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(shortXml);

        assertTrue("Parsed NBT is not a Short", parsedNBT instanceof NBTTagShort);

        NBTTagShort parsedShort = (NBTTagShort) parsedNBT;
        assertEquals(1001, parsedShort.getShort());
    }

    @Test
    public void fromXMLInt() throws IOException, SAXException, ParserConfigurationException
    {
        final Node intXml = getXMLNodeForTest("/XMLtoNBT/int.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(intXml);

        assertTrue("Parsed NBT is not a Int", parsedNBT instanceof NBTTagInt);

        NBTTagInt parsedInt = (NBTTagInt) parsedNBT;
        assertEquals(120123658, parsedInt.getInt());
    }

    @Test
    public void fromXMLByteArray() throws IOException, SAXException, ParserConfigurationException
    {
        final Node byteArrayXml = getXMLNodeForTest("/XMLtoNBT/byteArray.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(byteArrayXml);

        assertTrue("Parsed NBT is not a ByteArray", parsedNBT instanceof NBTTagByteArray);

        NBTTagByteArray parsedByteArray = (NBTTagByteArray) parsedNBT;
        Assert.assertThat(new Byte[] {1, 2, 3, 4, 5}, IsEqual.equalTo(parsedByteArray.getByteArray()));
    }

    @Test
    public void fromXMLList() throws IOException, SAXException, ParserConfigurationException
    {
        final Node listXml = getXMLNodeForTest("/XMLtoNBT/list.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(listXml);

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
    public void fromXMLCompound() throws IOException, SAXException, ParserConfigurationException
    {
        final Node listXml = getXMLNodeForTest("/XMLtoNBT/compound_correct.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(listXml);

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

    @Test(expected = IllegalArgumentException.class)
    public void fromXMLCompoundWithDuplicateKey() throws IOException, SAXException, ParserConfigurationException
    {
        final Node listXml = getXMLNodeForTest("/XMLtoNBT/compound_duplicate_key.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(listXml);
    }
}