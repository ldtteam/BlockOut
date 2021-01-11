package com.ldtteam.blockout.connector.common.definition.loader;

import com.google.common.io.CharStreams;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class CommonResourceLocationBasedGuiDefinitionLoader implements IGuiDefinitionLoader<ResourceLocation>
{

    private static final long serialVersionUID = 7644030184707070730L;

    public CommonResourceLocationBasedGuiDefinitionLoader()
    {
    }

    @NotNull
    @Override
    public String getGuiDefinition(final ResourceLocation loadFrom)
    {
        try
        {
            final InputStream stream = ProxyHolder.getInstance().getResourceStream(loadFrom);
            String data;
            try (final Reader reader = new InputStreamReader(stream))
            {
                data = CharStreams.toString(reader);
            }

            return data;
        }
        catch (Exception e)
        {
            Log.getLogger().warn("Failed to read data from location: " + loadFrom, e);
            return "";
        }
    }
}
