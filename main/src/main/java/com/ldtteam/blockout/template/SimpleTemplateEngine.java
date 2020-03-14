package com.ldtteam.blockout.template;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.advanced.template.ITemplate;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.style.resources.TemplateResource;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SimpleTemplateEngine implements ITemplateEngine
{
    private static SimpleTemplateEngine ourInstance = new SimpleTemplateEngine();

    private final Cache<ResourceLocation, TemplateResource> CACHE_TEMPLATE_DATA = CacheBuilder.newBuilder()
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
      @NotNull final ResourceLocation resourceId,
      @NotNull final String controlId,
      @NotNull final
      Function<IUIElementData<?>, IUIElementData<?>> dataOverrideCallback)
    {
        final TemplateResource templateData;
        try
        {
            templateData = CACHE_TEMPLATE_DATA.get(resourceId,
              () ->
                ProxyHolder.getInstance().getStyleManager().getResource(parent.getStyleId(), resourceId));
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
      @NotNull final IUIElementData<?> templateData,
      @NotNull final String controlId,
      @NotNull final
      Function<IUIElementData<?>, IUIElementData<?>> dataOverrideCallback)
    {
        final IUIElement templateCandidate = ProxyHolder.getInstance().getFactoryController().getElementFromData(templateData);
        if (templateCandidate instanceof ITemplate)
        {
            ITemplate template = (ITemplate) templateCandidate;
            return template.generateInstance(parent, dataContextProperty, controlId, dataOverrideCallback);
        }

        throw new IllegalArgumentException("The given IUIElementData<?> does not contain a Template as root control.");
    }
}
