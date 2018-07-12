package com.minecolonies.blockout.util.xml;

import com.minecolonies.test.AbstractBlockOutTest;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import org.jetbrains.annotations.NotNull;
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

    @Test
    public void fromXMLDoubleNoSuffix() throws IOException, SAXException, ParserConfigurationException
    {
        final Node doubleNoSuffix = getXMLNodeForTest("/XMLtoNBT/double_no_suffix.xml");
        final NBTBase parsedNBT = XMLToNBT.fromXML(doubleNoSuffix);

        assertTrue("Parsed NBT is not a Double", parsedNBT instanceof NBTTagDouble);

        NBTTagDouble parsedDouble = (NBTTagDouble) parsedNBT;
        assertEquals(0.0102503625d, parsedDouble.getDouble(), 0.00000001d);
    }

    private Node getXMLNodeForTest(@NotNull final String path) throws ParserConfigurationException, IOException, SAXException
    {
        final InputStream stream = getClass().getResourceAsStream(path);

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder;
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        return documentBuilder.parse(stream).getFirstChild();
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
}