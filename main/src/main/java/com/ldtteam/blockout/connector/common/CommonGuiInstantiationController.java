package com.ldtteam.blockout.connector.common;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.event.injector.EventHandlerInjector;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.UIManager;
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
