package com.ldtteam.blockout.template;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.template.Template;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.style.resources.TemplateResource;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SimpleTemplateEngine implements ITemplateEngine
{
    private static SimpleTemplateEngine ourInstance = new SimpleTemplateEngine();

    private final Cache<IIdentifier, TemplateResource> CACHE_TEMPLATE_DATA = CacheBuilder.newBuilder()
                                                                                    .expireAfterAccess(30, TimeUnit.MINUTES)
                                                                                    .maximumSize(30)
                                                                                    .build();

    private SimpleTemplateEngine()
    {
    }

    public static SimpleTemplateEngine getInstance()
    {
        return ourInstance;
    }

    @Override
    public IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<Object> dataContextProperty,
      @NotNull final IIdentifier resourceId,
      @NotNull final String controlId,
      @NotNull final
      Function<IUIElementData, IUIElementData> dataOverrideCallback)
    {
        final TemplateResource templateData;
        try
        {
            templateData = CACHE_TEMPLATE_DATA.get(resourceId,
              () ->
                BlockOut.getBlockOut().getProxy().getStyleManager().getResource(parent.getStyleId(), resourceId));
        }
        catch (ExecutionException e)
        {
            throw new IllegalArgumentException(String.format("Failed to load template resource: %s.", resourceId), e);
        }

        return generateFromTemplate(parent, dataContextProperty, templateData.getData(), controlId, dataOverrideCallback);
    }

    @Override
    public IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<Object> dataContextProperty,
      @NotNull final IUIElementData templateData,
      @NotNull final String controlId,
      @NotNull final
      Function<IUIElementData, IUIElementData> dataOverrideCallback)
    {
        final IUIElement templateCandidate = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(templateData);
        if (templateCandidate instanceof Template)
        {
            Template template = (Template) templateCandidate;
            return template.generateInstance(parent, dataContextProperty, controlId, dataOverrideCallback);
        }

        throw new IllegalArgumentException(String.format("The given IUIElementData does not contain a Template as root control."));
    }
}
