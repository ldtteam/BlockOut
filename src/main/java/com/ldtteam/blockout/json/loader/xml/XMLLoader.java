package com.ldtteam.blockout.json.loader.xml;

import com.ldtteam.blockout.json.loader.ILoader;
import org.jetbrains.annotations.NotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Utilities to load xml files.
 */
public final class XMLLoader implements ILoader
{

    public XMLLoader()
    {
        // Hides default constructor.
    }

    @Override
    @NotNull
    public IUIElementData createFromDataAndBindingEngine(@NotNull final String data)
    {
        try
        {
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            return new XMLUIElementData(documentBuilder.parse(data), null);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Data is not parsable in XML", e);
        }
    }
}
