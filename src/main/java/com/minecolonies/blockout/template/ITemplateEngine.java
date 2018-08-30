package com.minecolonies.blockout.template;

import com.minecolonies.blockout.binding.property.Property;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * An interface that describes how templates are generated.
 */
public interface ITemplateEngine
{
    /**
     * Generates a new unique {@link IUIElement} that represents the template with a datacontext.
     *
     * @param dataContextProperty The datacontext property.
     * @return The template.
     */
    IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final Property<Object> dataContextProperty,
      @NotNull final ResourceLocation resourceId,
      @NotNull final String controlId);

    /**
     * Generates a new unique {@link IUIElement} that represents the template with a datacontext.
     *
     * @param dataContextProperty The datacontext property.
     * @return The template.
     */
    IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final Property<Object> dataContextProperty,
      @NotNull final IUIElementData templateData,
      @NotNull final String controlId);
}
