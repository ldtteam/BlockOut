package com.minecolonies.blockout.style.simple;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.style.core.IStyle;
import com.minecolonies.blockout.style.core.IStyleManager;
import com.minecolonies.blockout.style.core.resources.core.IResource;
import com.minecolonies.blockout.style.definitions.ResourceTypeDefinition;
import com.minecolonies.blockout.style.definitions.StyleDefinition;
import com.minecolonies.blockout.style.definitions.StylesDefinition;
import com.minecolonies.blockout.style.definitions.deserializers.ResourceTypeDefinitionDeserializer;
import com.minecolonies.blockout.style.definitions.deserializers.StyleDefinitionDeserializer;
import com.minecolonies.blockout.style.definitions.deserializers.StylesDefinitionDeserializer;
import com.minecolonies.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ProgressManager;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleFileBasedStyleManager implements IStyleManager
{
    public static final String                        CONST_STYLE_FILE_PATH = "styles/styles.json";
    private static      SimpleFileBasedStyleManager   ourInstance           = new SimpleFileBasedStyleManager();
    private final       Map<ResourceLocation, IStyle> styles                = new HashMap();

    private SimpleFileBasedStyleManager()
    {
    }

    public static SimpleFileBasedStyleManager getInstance()
    {
        return ourInstance;
    }

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

    /**
     * Loads the styles during post init.
     */
    @Override
    public void loadStyles()
    {
        styles.clear();
        initialize();
    }

    public void initialize()
    {
        Loader.instance().getActiveModList().stream().map(modContainer -> modContainer.getModId()).collect(Collectors.toList());

        final Set<String> resourceDomains = Loader.instance().getActiveModList().stream().map(modContainer -> modContainer.getModId()).collect(Collectors.toSet());
        final ProgressManager.ProgressBar loadingBar = ProgressManager.push("Loading BlockOut Styles", resourceDomains.size());

        final Gson gson = new GsonBuilder()
                            .registerTypeAdapter(StylesDefinitionDeserializer.CONST_STYLES_DEFINITION_TYPE, StylesDefinitionDeserializer.getInstance())
                            .registerTypeAdapter(StyleDefinitionDeserializer.CONST_STYLE_DEFINTION_TYPE, StyleDefinitionDeserializer.getInstance())
                            .registerTypeAdapter(ResourceTypeDefinitionDeserializer.CONST_RESOURCE_TYPE_DEFINITION_TYPE, ResourceTypeDefinitionDeserializer.getInstance())
                            .create();

        resourceDomains.forEach(domain -> {
            loadingBar.step("Loading styles from: " + domain);

            final ResourceLocation stylesLocation = new ResourceLocation(domain.toLowerCase(), CONST_STYLE_FILE_PATH);
            final StylesDefinition stylesDefinition;
            try
            {
                stylesDefinition = gson.fromJson(new InputStreamReader(BlockOut.getBlockOut().getProxy().getResourceStream(stylesLocation)),
                  StylesDefinitionDeserializer.CONST_STYLES_DEFINITION_TYPE);
            }
            catch (Exception e)
            {
                //Mod does not support BlockOut or has no style. So no biggy.
                return;
            }

            final ProgressManager.ProgressBar domainLoadingBar =
              ProgressManager.push(String.format("Loading styles from: %s", domain), stylesDefinition.getStyleLocations().size());
            stylesDefinition.getStyleLocations().forEach(styleLocation ->
            {
                domainLoadingBar.step(String.format("Loading style from: %s", styleLocation));
                final StyleDefinition styleDefinition;
                try
                {
                    styleDefinition = gson.fromJson(new InputStreamReader(BlockOut.getBlockOut().getProxy().getResourceStream(styleLocation)),
                      StyleDefinitionDeserializer.CONST_STYLE_DEFINTION_TYPE);
                }
                catch (Exception e)
                {
                    Log.getLogger().error(String.format("Failed to find style: %s", styleLocation.toString()), e);
                    return;
                }

                final ProgressManager.ProgressBar styleLoadingBar = ProgressManager.push(String.format("Loading style: %s in domain: %s", styleDefinition.getStyleId(), domain),
                  styleDefinition.getResourceTypeDefinitionLocations().size());
                final Map<ResourceLocation, IResource> resourcesForStyle =
                  styleDefinition.getResourceTypeDefinitionLocations()
                    .stream()
                    .flatMap(resourceTypeLocation -> {
                        styleLoadingBar.step("Loading resources from: " + resourceTypeLocation);

                        final ResourceTypeDefinition resourceTypeDefinition;
                        try
                        {
                            resourceTypeDefinition = gson.fromJson(new InputStreamReader(BlockOut.getBlockOut().getProxy().getResourceStream(resourceTypeLocation)),
                              ResourceTypeDefinitionDeserializer.CONST_RESOURCE_TYPE_DEFINITION_TYPE);
                        }
                        catch (Exception e)
                        {
                            Log.getLogger().error(String.format("Failed to find resource type definition: %s", resourceTypeLocation), e);
                            return (new ArrayList<IResource>().stream());
                        }

                        return BlockOut.getBlockOut().getProxy().getResourceLoaderManager().loadResources(resourceTypeDefinition).stream();
                    })
                    .collect(
                      Collectors.toMap(
                        IResource::getId,
                        Function.identity(),
                        (one, two) -> {
                            throw new IllegalStateException(String.format("The style definition: %s contains two resources with the same key: %s", styleLocation, one.getId()));
                        }
                      )
                    );

                final IStyle style;
                if (styles.containsKey(styleDefinition.getStyleId()))
                {
                    Log.getLogger().warn("Already existing style detected. Merging and overriding...");
                    final IStyle alreadyCreatedStyle = styles.remove(styleDefinition.getStyleId());

                    final Map<ResourceLocation, IResource> merged = new HashMap<>(alreadyCreatedStyle.getResources());
                    resourcesForStyle.forEach((key, value) -> merged.merge(key, value, (one, two) -> two));

                    style = new SimpleStyle(styleDefinition.getStyleId(), merged);
                }
                else
                {
                    style = new SimpleStyle(styleDefinition.getStyleId(), resourcesForStyle);
                }

                styles.put(styleDefinition.getStyleId(), style);

                ProgressManager.pop(styleLoadingBar);
            });

            ProgressManager.pop(domainLoadingBar);
        });

        ProgressManager.pop(loadingBar);
    }
}
