package com.minecolonies.blockout.loader.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minecolonies.blockout.core.Pane;
import com.minecolonies.blockout.loader.ComponentConstructionController;
import com.minecolonies.blockout.loader.ILoader;
import com.minecolonies.blockout.loader.IPaneParams;
import com.minecolonies.blockout.loader.xml.XMLLoader;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.views.View;
import com.minecolonies.blockout.views.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JsonLoader implements ILoader
{
    @Override
    public boolean accepts(@NotNull final ResourceLocation location)
    {
        return location.getResourcePath().toLowerCase().endsWith(".xml");
    }

    @Override
    public boolean accepts(@NotNull final IPaneParams paneParams)
    {
        return paneParams instanceof JsonPaneParams;
    }

    @Override
    public Pane createFromPaneParams(
      @NotNull final IPaneParams params, @NotNull final View parent)
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

    @Override
    public void createFromFile(final ResourceLocation resource, final View parent)
    {
        createFromJson(createInputStream(resource), parent);
    }

    private Pane createFromPaneParams(final IPaneParams params)
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
                  String.format("Exception when parsing Json for pane type %s", paneType),
                  exc);
            }
        }

        return null;
    }

    /**
     * Parse XML from an InputSource into contents for a View.
     *
     * @param input  xml file.
     * @param parent parent view.
     */
    private void createFromJson(final InputStream input, final View parent)
    {
        final JsonParser parser = new JsonParser();

        createFromJson(parser.parse(new InputStreamReader(input)).getAsJsonObject(), parent);
    }

    /**
     * Parse an JSON Document into contents for a View.
     *
     * @param object json document.
     * @param parent parent view.
     */
    private void createFromJson(final JsonObject object, final View parent)
    {
        final JsonPaneParams root = new JsonPaneParams(object);
        if (parent instanceof Window)
        {
            ((Window) parent).loadParams(root);
        }

        root.getChildren().stream()
          .filter(o -> o instanceof JsonPaneParams)
          .map(JsonPaneParams.class::cast)
          .forEach(p -> createFromPaneParams(p, parent));
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
