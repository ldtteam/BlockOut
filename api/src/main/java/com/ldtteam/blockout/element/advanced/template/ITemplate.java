package com.ldtteam.blockout.element.advanced.template;

import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ITemplate extends IUIElementHost {
    /**
     * Generates a new instance of the template.
     *
     * @param instanceParent The instances parent.
     * @param boundContext   The property for the context retrieval.
     * @param controlId      The id of the new instance.
     * @return The instance from the parent.
     */
    IUIElement generateInstance(
            @NotNull IUIElementHost instanceParent,
            @NotNull IDependencyObject<Object> boundContext,
            @NotNull String controlId,
            @NotNull Function<IUIElementData<?>, IUIElementData<?>> dataOverrideCallback);
}
