package com.ldtteam.blockout.connector.common;

import com.ldtteam.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.event.injector.EventHandlerInjector;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.management.UIManager;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
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

    public IRootGuiElement instantiateNewGui(@NotNull final IGuiKey key)
    {
        IRootGuiElement host;
        final IUIElementData<?> elementData = loadDataFrom(key.getDataSource());
        if (elementData == null)
        {
            throw new IllegalArgumentException("The given guikey contains no valid IUIElementData");
        }

        final IUIElement element = ProxyHolder.getInstance().getFactoryController().getElementFromData(elementData);
        if (!(element instanceof IRootGuiElement))
        {
            throw new IllegalArgumentException("The given guikey has no Root as root element.");
        }

        host = (IRootGuiElement) element;

        DependencyObjectInjector.inject(host, key.getConstructionData());
        host.getAllCombinedChildElements().values().forEach(c -> DependencyObjectInjector.inject(c, key.getConstructionData()));

        EventHandlerInjector.inject(host, key.getConstructionData());
        host.getAllCombinedChildElements().values().forEach(c -> EventHandlerInjector.inject(c, key.getConstructionData()));

        host.setUiManager(new UIManager(host, key));
        host.getUiManager().getUpdateManager().updateElement(host);

        return host;
    }

    private <T> IUIElementData<?> loadDataFrom(T data)
    {
        final IGuiDefinitionLoader<T> loader = IProxy.getInstance().getDefinitionLoaderManager().getLoaderFor(data);
        return ProxyHolder.getInstance().getLoaderManager().loadData(loader.getGuiDefinition(data));
    }
}
