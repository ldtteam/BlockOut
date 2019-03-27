package com.ldtteam.blockout.connector.common.definition.loader;

import com.google.common.io.CharStreams;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class CommonIIdentifierBasedGuiDefinitionLoader implements IGuiDefinitionLoader
{

    @NotNull
    private final String domain;
    @NotNull
    private final String path;

    private CommonIIdentifierBasedGuiDefinitionLoader()
    {
        this.domain = "";
        this.path = "";
    }

    public CommonIIdentifierBasedGuiDefinitionLoader(final IIdentifier location)
    {
        this.domain = location.getDomain();
        this.path = location.getPath();
    }

    @NotNull
    @Override
    public String getGuiDefinition()
    {
        try
        {
            final InputStream stream = BlockOut.getBlockOut().getProxy().getResourceStream(getLocation());
            String data;
            try (final Reader reader = new InputStreamReader(stream))
            {
                data = CharStreams.toString(reader);
            }

            return data;
        }
        catch (Exception e)
        {
            Log.getLogger().warn("Failed to read data from location: " + getLocation(), e);
            return "";
        }
    }

    @NotNull
    public IIdentifier getLocation()
    {
        return IIdentifier.create(domain, path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getLocation());
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        final CommonIIdentifierBasedGuiDefinitionLoader that = (CommonIIdentifierBasedGuiDefinitionLoader) o;
        return Objects.equals(getLocation(), that.getLocation());
    }
}
