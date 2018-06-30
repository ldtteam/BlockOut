package com.minecolonies.blockout.style;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.style.core.IStyle;
import com.minecolonies.blockout.style.core.IStyleManager;
import com.minecolonies.blockout.util.Log;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StyleFileBasedStyleManager implements IStyleManager, IResourceManagerReloadListener
{

    public static final String                             CONST_STYLE_FILE_PATH = "styles/styles.json";
    public static final TypeToken<HashMap<String, String>> CONST_MAP_TYPE        = new TypeToken<HashMap<String, String>>() {};

    private final Map<ResourceLocation, IStyle> styles = new HashMap();

    /**
     * All known {@link IStyle} that are registered to this {@link IStyleManager}.
     *
     * @return All known {@link IStyle}.
     */
    @NotNull
    @Override
    public ImmutableMap<ResourceLocation, IStyle> getStyles()
    {
        return ImmutableMap.copyOf(styles);
    }

    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager)
    {
        styles.clear();
        initialize(resourceManager);
    }

    public void initialize(final IResourceManager resourceManager)
    {
        final Set<String> resourceDomains = resourceManager.getResourceDomains();
        final ProgressManager.ProgressBar loadingBar = ProgressManager.push("Loading BlockOut Styles", resourceDomains.size());

        final Gson gson = new GsonBuilder().create();

        resourceDomains.forEach(domain -> {
            loadingBar.step("Loading styles from: " + domain);

            final ResourceLocation stylesLocation = new ResourceLocation(domain.toLowerCase(), CONST_STYLE_FILE_PATH);
            final Map<String, String> stylesInDomainMap;
            try
            {
                stylesInDomainMap = gson.fromJson(new InputStreamReader(BlockOut.getBlockOut().getProxy().getResourceStream(stylesLocation)), CONST_MAP_TYPE.getType());
            }
            catch (Exception e)
            {
                Log.getLogger().error(String.format("Failed to find styles map: %s", stylesLocation.toString()), e);
                return;
            }

            final ProgressManager.ProgressBar domainLoadingBar = ProgressManager.push("Loading styles from: " + domain);
        });

        ProgressManager.pop(loadingBar);
    }
}
