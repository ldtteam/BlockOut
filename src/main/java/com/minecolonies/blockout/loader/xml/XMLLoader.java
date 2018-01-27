package com.minecolonies.blockout.loader.xml;

import com.minecolonies.blockout.core.Pane;
import com.minecolonies.blockout.loader.ComponentConstructionController;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.views.View;
import com.minecolonies.blockout.views.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utilities to load xml files.
 */
public final class XMLLoader implements ILoader
{

    public XMLLoader()
    {
        // Hides default constructor.
    }

    private Pane createFromPaneParams(final IUIElementData params)
    {
        //  Parse Attributes first, to full construct
        final String paneType = params.getType();
        final String style = params.getStringAttribute("style", null);

        Constructor<? extends Pane> constructor = ComponentConstructionController.getConstructorForTypeAndStyle(paneType, style);
        if (constructor == null && style != null)
        {
            constructor = ComponentConstructionController.getConstructorForTypeAndStyle(paneType, null);
        }

        if (constructor != null)
        {
            try
            {
                return constructor.newInstance(params);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException exc)
            {
                Log.getLogger().error(
                  String.format("Exception when parsing XML for pane type %s", paneType),
                  exc);
            }
        }

        return null;
    }

    @Override
    public boolean accepts(@NotNull final ResourceLocation location)
    {
        return location.getResourcePath().toLowerCase().endsWith(".xml");
    }

    @Override
    public boolean accepts(@NotNull final IUIElementData paneParams)
    {
        return paneParams instanceof XMLPaneParams;
    }

    /**
     * Create a pane from its xml parameters.
     *
     * @param params xml parameters.
     * @param parent parent view.
     * @return the new pane.
     */
    @Override
    public Pane createFromPaneParams(final IUIElementData params, final View parent)
    {
        if ("layout".equalsIgnoreCase(params.getType()))
        {
            final String resource = params.getStringAttribute("source", null);
            if (resource != null)
            {
                createFromFile(new ResourceLocation(resource), parent);
            }

            return null;
        }

        params.setParentView(parent);
        final Pane pane = createFromPaneParams(params);

        if (pane != null)
        {
            pane.putInside(parent);
            pane.parseChildren(params);
        }

        return pane;
    }

    /**
     * Parse XML contains in a ResourceLocation into contents for a Window.
     *
     * @param resource xml as a {@link ResourceLocation}.
     * @param parent   parent view.
     */
    @Override
    public void createFromFile(final ResourceLocation resource, final View parent)
    {
        createFromXML(new InputSource(createInputStream(resource)), parent);
    }

    /**
     * Parse an XML Document into contents for a View.
     *
     * @param doc    xml document.
     * @param parent parent view.
     */
    private void createFromXML(final Document doc, final View parent)
    {
        doc.getDocumentElement().normalize();

        final XMLPaneParams root = new XMLPaneParams(doc.getDocumentElement());
        if (parent instanceof Window)
        {
            ((Window) parent).loadParams(root);
        }

        root.getChildren().stream()
          .filter(o -> o instanceof XMLPaneParams)
          .map(XMLPaneParams.class::cast)
          .forEach(p -> createFromPaneParams(p, parent));
    }

    /**
     * Parse XML from an InputSource into contents for a View.
     *
     * @param input  xml file.
     * @param parent parent view.
     */
    private void createFromXML(final InputSource input, final View parent)
    {
        try
        {
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            final Document doc = dBuilder.parse(input);

            createFromXML(doc, parent);
        }
        catch (ParserConfigurationException | SAXException | IOException exc)
        {
            Log.getLogger().error("Exception when parsing XML.", exc);
        }
    }

    /**
     * Create an InputStream from a ResourceLocation.
     *
     * @param res ResourceLocation to get an InputStream from.
     * @return the InputStream created from the ResourceLocation.
     */
    private InputStream createInputStream(final ResourceLocation res)
    {
        try
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                return Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
            }
            else
            {
                return XMLLoader.class.getResourceAsStream(String.format("/assets/%s/%s", res.getResourceDomain(), res.getResourcePath()));
            }
        }
        catch (final IOException e)
        {
            Log.getLogger().error("IOException XMLLoader.java", e);
        }
        return null;
    }
}
