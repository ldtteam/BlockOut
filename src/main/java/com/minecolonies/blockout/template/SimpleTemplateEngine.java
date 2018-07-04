package com.minecolonies.blockout.template;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.element.template.Template;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.style.resources.TemplateResource;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleTemplateEngine implements ITemplateEngine
{
    private static SimpleTemplateEngine ourInstance = new SimpleTemplateEngine();

    private SimpleTemplateEngine()
    {
    }

    public static SimpleTemplateEngine getInstance()
    {
        return ourInstance;
    }

    @Override
    public IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent, @Nullable final Object dataContextForTemplate, @NotNull final ResourceLocation resourceId)
    {
        final TemplateResource templateData =
          BlockOut.getBlockOut().getProxy().getStyleManager().getResource(parent.getStyleId(), resourceId);

        return generateFromTemplate(parent, dataContextForTemplate, templateData.getData());
    }

    @Override
    public IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent, @Nullable final Object dataContextForTemplate, @NotNull final IUIElementData templateData)
    {
        final IUIElement templateCandidate = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(templateData);
        if (templateCandidate instanceof Template)
        {
            Template template = (Template) templateCandidate;
            return template.generateInstance(parent, dataContextForTemplate);
        }

        throw new IllegalArgumentException(String.format("The given IUIElementData does not contain a Template as root control."));
    }
}
