package com.minecolonies.blockout.loader.xml;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;

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
    public boolean accepts(@NotNull final ResourceLocation location)
    {
        return location.getResourcePath().toLowerCase().endsWith(".xml");
    }

    @Override
    public IUIElementData createFromFile(@NotNull final ResourceLocation location)
    {
        final InputStream inputStream;
        try
        {
            inputStream = BlockOut.getBlockOut().getProxy().getResourceStream(location);
        }
        catch (Exception e)
        {
            Log.getLogger().error("Failed to load UI from json file: " + location + ". Could not find resource.", e);
            return null;
        }

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder;
        try
        {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            Log.getLogger().error("Failed to load UI from json file: " + location + ". Could not create document parser.", e);
            return null;
        }

        final IUIElementData result;
        try
        {
            result = new XMLUIElementData(documentBuilder.parse(inputStream), null);
        }
        catch (Exception e)
        {
            Log.getLogger().error("Failed to load UI from json file: " + location + ". Could not parse document.", e);
            return null;
        }

        return result;
    }
}
