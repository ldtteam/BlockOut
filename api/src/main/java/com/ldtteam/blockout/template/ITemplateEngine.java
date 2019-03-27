package com.ldtteam.blockout.template;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

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
    default IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<Object> dataContextProperty,
      @NotNull final IIdentifier resourceId,
      @NotNull final String controlId)
    {
        return this.generateFromTemplate(
          parent, dataContextProperty, resourceId, controlId, Function.identity());
    }

    IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<Object> dataContextProperty,
      @NotNull final IIdentifier resourceId,
      @NotNull final String controlId,
      @NotNull final Function<IUIElementData, IUIElementData> dataOverrideCallback);

    /**
     * Generates a new unique {@link IUIElement} that represents the template with a datacontext.
     *
     * @param dataContextProperty The datacontext property.
     * @return The template.
     */
    default IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<Object> dataContextProperty,
      @NotNull final IUIElementData templateData,
      @NotNull final String controlId)
    {
        return this.generateFromTemplate(parent, dataContextProperty, templateData, controlId, Function.identity());
    }

    IUIElement generateFromTemplate(
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<Object> dataContextProperty,
      @NotNull final IUIElementData templateData,
      @NotNull final String controlId,
      @NotNull final Function<IUIElementData, IUIElementData> dataOverrideCallback);
}
