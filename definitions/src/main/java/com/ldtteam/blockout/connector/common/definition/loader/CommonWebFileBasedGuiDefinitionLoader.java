package com.ldtteam.blockout.connector.common.definition.loader;

import com.google.common.io.CharStreams;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.util.Log;
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

public class CommonWebFileBasedGuiDefinitionLoader implements IGuiDefinitionLoader<URL>
{

    public CommonWebFileBasedGuiDefinitionLoader() {
    }

    @NotNull
    @Override
    public String getGuiDefinition(final URL loadFrom)
    {
        try (final CloseableHttpClient closeableHttpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build())
        {
            final HttpGet getter = new HttpGet(loadFrom.toURI());
            return closeableHttpClient.execute(getter, new HttpResultToStringHandler());
        }
        catch (IOException e)
        {
            Log.getLogger().error("Failed to download the gui definition from: " + loadFrom.toString(), e);
            return "";
        }
        catch (URISyntaxException e)
        {
            Log.getLogger().error("Failed to parse the url for the gui definition from: " + loadFrom.toString(), e);
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
