package com.minecolonies.blockout.connector.common;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.event.injector.EventHandlerInjector;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.management.UIManager;
import org.jetbrains.annotations.NotNull;

public class CommonGuiInstantiationController
{
    private static CommonGuiInstantiationController ourInstance = new CommonGuiInstantiationController();

    private CommonGuiInstantiationController()
    {
    }

    public static CommonGuiInstantiationController getInstance()
    {
        return ourInstance;
    }

    public RootGuiElement instantiateNewGui(@NotNull final IGuiKey key)
    {
        RootGuiElement host;
        final IUIElementData elementData = BlockOut.getBlockOut().getProxy().getLoaderManager().loadData(key.getGuiDefinitionLoader());

        if (elementData == null)
        {
            throw new IllegalArgumentException("The given guikey contains no valid IUIElementData");
        }

        final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(elementData);
        if (!(element instanceof RootGuiElement))
        {
            throw new IllegalArgumentException("The given guikey has no Root as root element.");
        }

        host = (RootGuiElement) element;

        DependencyObjectInjector.inject(host, key.getConstructionData());
        host.getAllCombinedChildElements().values().forEach(c -> DependencyObjectInjector.inject(c, key.getConstructionData()));

        EventHandlerInjector.inject(host, key.getConstructionData());
        host.getAllCombinedChildElements().values().forEach(c -> EventHandlerInjector.inject(c, key.getConstructionData()));

        host.setUiManager(new UIManager(host, key));
        host.getUiManager().getUpdateManager().updateElement(host);

        return host;
    }
}
