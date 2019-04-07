package com.ldtteam.blockout.style.simple;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.style.core.IStyle;
import com.ldtteam.blockout.style.core.IStyleManager;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.style.definitions.ResourceTypeDefinition;
import com.ldtteam.blockout.style.definitions.StyleDefinition;
import com.ldtteam.blockout.style.definitions.StylesDefinition;
import com.ldtteam.blockout.style.definitions.deserializers.ResourceTypeDefinitionDeserializer;
import com.ldtteam.blockout.style.definitions.deserializers.StyleDefinitionDeserializer;
import com.ldtteam.blockout.style.definitions.deserializers.StylesDefinitionDeserializer;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.modloader.IModLoader;
import com.ldtteam.jvoxelizer.progressmanager.IProgressBar;
import com.ldtteam.jvoxelizer.progressmanager.IProgressManager;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
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
    public static final String                      CONST_STYLE_FILE_PATH = "styles/styles.json";
    private static      SimpleFileBasedStyleManager ourInstance           = new SimpleFileBasedStyleManager();
    private final       Map<IIdentifier, IStyle>    styles                = new HashMap();

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
    public ImmutableMap<IIdentifier, IStyle> getStyles()
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
        IModLoader.instance().getActiveModList().stream().map(modContainer -> modContainer.getModId()).collect(Collectors.toList());

        final Set<String> resourceDomains = IModLoader.instance().getActiveModList().stream().map(modContainer -> modContainer.getModId()).collect(Collectors.toSet());
        final IProgressBar loadingBar = IProgressManager.push("Loading BlockOut Styles", resourceDomains.size());

        final Gson gson = new GsonBuilder()
                            .registerTypeAdapter(StylesDefinitionDeserializer.CONST_STYLES_DEFINITION_TYPE, StylesDefinitionDeserializer.getInstance())
                            .registerTypeAdapter(StyleDefinitionDeserializer.CONST_STYLE_DEFINTION_TYPE, StyleDefinitionDeserializer.getInstance())
                            .registerTypeAdapter(ResourceTypeDefinitionDeserializer.CONST_RESOURCE_TYPE_DEFINITION_TYPE, ResourceTypeDefinitionDeserializer.getInstance())
                            .create();

        resourceDomains.forEach(domain -> {
            loadingBar.step("Loading styles from: " + domain);

            final IIdentifier stylesLocation = IIdentifier.create(domain.toLowerCase(), CONST_STYLE_FILE_PATH);
            final StylesDefinition stylesDefinition;
            try
            {
                stylesDefinition = gson.fromJson(new InputStreamReader(ProxyHolder.getInstance().getResourceStream(stylesLocation)),
                  StylesDefinitionDeserializer.CONST_STYLES_DEFINITION_TYPE);
            }
            catch (Exception e)
            {
                //Mod does not support BlockOut or has no style. So no biggy.
                return;
            }

            final IProgressBar domainLoadingBar =
              IProgressManager.push(String.format("Loading styles from: %s", domain), stylesDefinition.getStyleLocations().size());
            stylesDefinition.getStyleLocations().forEach(styleLocation ->
            {
                domainLoadingBar.step(String.format("Loading style from: %s", styleLocation));
                final StyleDefinition styleDefinition;
                try
                {
                    styleDefinition = gson.fromJson(new InputStreamReader(ProxyHolder.getInstance().getResourceStream(styleLocation)),
                      StyleDefinitionDeserializer.CONST_STYLE_DEFINTION_TYPE);
                }
                catch (Exception e)
                {
                    Log.getLogger().error(String.format("Failed to find style: %s", styleLocation.toString()), e);
                    return;
                }

                final IProgressBar styleLoadingBar = IProgressManager.push(String.format("Loading style: %s in domain: %s", styleDefinition.getStyleId(), domain),
                  styleDefinition.getResourceTypeDefinitionLocations().size());
                final Map<IIdentifier, IResource> resourcesForStyle =
                  styleDefinition.getResourceTypeDefinitionLocations()
                    .stream()
                    .flatMap(resourceTypeLocation -> {
                        styleLoadingBar.step("Loading resources from: " + resourceTypeLocation);

                        final ResourceTypeDefinition resourceTypeDefinition;
                        try
                        {
                            resourceTypeDefinition = gson.fromJson(new InputStreamReader(ProxyHolder.getInstance().getResourceStream(resourceTypeLocation)),
                              ResourceTypeDefinitionDeserializer.CONST_RESOURCE_TYPE_DEFINITION_TYPE);
                        }
                        catch (Exception e)
                        {
                            Log.getLogger().error(String.format("Failed to find resource type definition: %s", resourceTypeLocation), e);
                            return (new ArrayList<IResource>().stream());
                        }

                        return ProxyHolder.getInstance().getResourceLoaderManager().loadResources(resourceTypeDefinition).stream();
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

                    final Map<IIdentifier, IResource> merged = new HashMap<>(alreadyCreatedStyle.getResources());
                    resourcesForStyle.forEach((key, value) -> merged.merge(key, value, (one, two) -> two));

                    style = new SimpleStyle(styleDefinition.getStyleId(), merged);
                }
                else
                {
                    style = new SimpleStyle(styleDefinition.getStyleId(), resourcesForStyle);
                }

                styles.put(styleDefinition.getStyleId(), style);

                IProgressManager.pop(styleLoadingBar);
            });

            IProgressManager.pop(domainLoadingBar);
        });

        IProgressManager.pop(loadingBar);
    }
}
