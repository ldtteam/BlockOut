package com.minecolonies.blockout.connector.common;

import com.google.common.io.CharStreams;
import com.minecolonies.blockout.connector.core.IGuiDefinitionLoader;
import com.minecolonies.blockout.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;

public class CommonWebFileBasedGuiDefinitionLoader implements IGuiDefinitionLoader
{
    @NotNull
    private final URL url;

    private CommonWebFileBasedGuiDefinitionLoader()
    {
        this.url = null;
    }

    public CommonWebFileBasedGuiDefinitionLoader(@NotNull final URL url) {this.url = url;}

    @NotNull
    @Override
    public String getGuiDefinition()
    {
        try (final CloseableHttpClient closeableHttpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build())
        {
            final HttpGet getter = new HttpGet(url.toURI());
            return closeableHttpClient.execute(getter, new HttpResultToStringHandler());
        }
        catch (IOException e)
        {
            Log.getLogger().error("Failed to download the gui definition from: " + url.toString(), e);
            return "";
        }
        catch (URISyntaxException e)
        {
            Log.getLogger().error("Failed to parse the url for the gui definition from: " + url.toString(), e);
            return "";
        }
    }

    private static final class HttpResultToStringHandler implements ResponseHandler<String>
    {

        @Override
        public String handleResponse(final HttpResponse response) throws IOException
        {
            final InputStream stream = response.getEntity().getContent();
            String data;
            try (final Reader reader = new InputStreamReader(stream))
            {
                data = CharStreams.toString(reader);
            }

            return data;
        }
    }
}
