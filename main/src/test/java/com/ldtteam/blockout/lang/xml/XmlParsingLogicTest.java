package com.ldtteam.blockout.lang.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlParsingLogicTest
{

    @Test
    public void test() throws ParserConfigurationException, IOException, SAXException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();

        final Document document = builder.parse(new File("src/test/resources/gui_template.xml"));
        assert document != null;
    }
}
