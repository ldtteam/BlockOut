package com.ldtteam.blockout.util.template;

import com.ldtteam.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.ITemplateInstanceDataProvidingElement;
import com.ldtteam.blockout.event.injector.EventHandlerInjector;
import org.jetbrains.annotations.NotNull;

public final class TemplateUtils {

    private TemplateUtils() {
        throw new IllegalStateException("Tried to initialize: TemplateUtils but this is a Utility class.");
    }

    public static void insertTemplateConstructionDataInto(final ITemplateInstanceDataProvidingElement templateInstanceDataProvider, final IUIElement element) {
        if (templateInstanceDataProvider.getTemplateConstructionData() != null)
        {
            @NotNull final IBlockOutGuiConstructionData data = templateInstanceDataProvider.getTemplateConstructionData();
            injectConstructionData(element, data);
        }
    }

    public static void injectConstructionData(final IUIElement element, final IBlockOutGuiConstructionData data) {
        DependencyObjectInjector.inject(element, data);
        EventHandlerInjector.inject(element, data);

        if (element instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) element;
            iuiElementHost.getAllCombinedChildElements().values().forEach(c -> {
                DependencyObjectInjector.inject(c, data);
                EventHandlerInjector.inject(c, data);
            });
        }
    }
}
