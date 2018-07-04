package com.minecolonies.blockout.template;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface that describes how templates are generated.
 */
public interface ITemplateEngine
{
    /**
     * Generates a new unique {@link IUIElement} that represents the template with a datacontext.
     *
     * @param dataContextForTemplate The datacontext.
     * @return The template.
     */
    IUIElement generateFromTemplate(@NotNull final IUIElementHost parent, @Nullable final Object dataContextForTemplate, @NotNull final ResourceLocation resourceId);

    /**
     * Generates a new unique {@link IUIElement} that represents the template with a datacontext.
     *
     * @param dataContextForTemplate The datacontext.
     * @return The template.
     */
    IUIElement generateFromTemplate(@NotNull final IUIElementHost parent, @Nullable final Object dataContextForTemplate, @NotNull final IUIElementData templateData);
}
