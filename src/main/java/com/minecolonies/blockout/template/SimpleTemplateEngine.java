package com.minecolonies.blockout.template;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.element.template.Template;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.style.resources.TemplateResource;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
      @NotNull final IUIElementHost parent, @Nullable final Object dataContextForTemplate, @NotNull final ResourceLocation resourceId, @NotNull final String controlId)
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

        return generateFromTemplate(parent, dataContextForTemplate, templateData.getData(), controlId);
    }

    @Override
    public IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent, @Nullable final Object dataContextForTemplate, @NotNull final IUIElementData templateData, @NotNull final String controlId)
    {
        final IUIElement templateCandidate = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(templateData);
        if (templateCandidate instanceof Template)
        {
            Template template = (Template) templateCandidate;
            return template.generateInstance(parent, dataContextForTemplate, controlId);
        }

        throw new IllegalArgumentException(String.format("The given IUIElementData does not contain a Template as root control."));
    }
}
