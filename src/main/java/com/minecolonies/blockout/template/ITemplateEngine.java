package com.minecolonies.blockout.template;

import com.minecolonies.blockout.core.element.IUIElement;

/**
 * An interface that describes how templates are generated.
 *
 * @param <T> The {@link IUIElement} that the template represents.
 * @param <D> The data context that this engine processes.
 */
public interface ITemplateEngine<T extends IUIElement, D>
{
    /**
     * Generates a new unique {@link IUIElement} that represents the template with a datacontext.
     *
     * @param dataContextForTemplate The datacontext.
     * @return The template.
     */
    T generateFromTemplate(D dataContextForTemplate);
}
