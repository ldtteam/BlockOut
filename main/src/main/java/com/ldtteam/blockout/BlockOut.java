package com.ldtteam.blockout;

import com.ldtteam.blockout.compat.UpdateHandler;
import com.ldtteam.blockout.element.advanced.TemplateInstance;
import com.ldtteam.blockout.element.advanced.list.factory.ListFactory;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.*;
import com.ldtteam.blockout.element.template.Template;
import com.ldtteam.blockout.loader.binding.DataContextBindingCommand;
import com.ldtteam.blockout.loader.object.loader.ObjectUIElementLoader;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.reflection.ReflectionManager;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.style.resources.ItemStackResource;
import com.ldtteam.blockout.style.resources.TemplateResource;
import com.ldtteam.jvoxelizer.common.gameevent.event.ITickEvent;
import com.ldtteam.jvoxelizer.common.gameevent.event.player.IPlayerGameEvent;
import com.ldtteam.jvoxelizer.event.handler.IEventHandlerManager;
import com.ldtteam.jvoxelizer.progressmanager.IProgressBar;
import com.ldtteam.jvoxelizer.progressmanager.IProgressManager;

import java.util.Set;

public class BlockOut
{
    private static BlockOut ourInstance = new BlockOut();

    private BlockOut()
    {
    }

    public static BlockOut getInstance()
    {
        return ourInstance;
    }

    public void preInit()
    {
        getProxy().getLoaderManager().registerLoader(new ObjectUIElementLoader());

        getProxy().getFactoryController().registerFactory(new RootGuiElement.Factory());
        getProxy().getFactoryController().registerFactory(new Image.Factory());
        getProxy().getFactoryController().registerFactory(new Slot.Factory());
        getProxy().getFactoryController().registerFactory(new Button.Factory());
        getProxy().getFactoryController().registerFactory(new CheckBox.Factory());
        getProxy().getFactoryController().registerFactory(new Label.Factory());
        getProxy().getFactoryController().registerFactory(new TextField.Factory());
        getProxy().getFactoryController().registerFactory(new BlockStateIcon.Factory());
        getProxy().getFactoryController().registerFactory(new TemplateInstance.Factory());
        getProxy().getFactoryController().registerFactory(new RangeSelector.Factory());

        getProxy().getFactoryController().registerFactory(new Region.Factory());
        getProxy().getFactoryController().registerFactory(new Template.Factory());
        getProxy().getFactoryController().registerFactory(new ItemIcon.Factory());

        getProxy().getFactoryController().registerFactory(new ListFactory());

        getProxy().getResourceLoaderManager().registerTypeLoader(new ImageResource.Loader());
        getProxy().getResourceLoaderManager().registerTypeLoader(new ItemStackResource.Loader());
        getProxy().getResourceLoaderManager().registerTypeLoader(new TemplateResource.Loader());

        getProxy().getBindingEngine().registerBindingCommand(new DataContextBindingCommand());

        IEventHandlerManager.registerHandler(
          IPlayerGameEvent.ILoggedOutEvent.class,
          UpdateHandler::onPlayerLoggedOut
        );

        IEventHandlerManager.registerHandler(
          ITickEvent.IClientTickEvent.class,
          UpdateHandler::onTickClientTick
        );

        IEventHandlerManager.registerHandler(
          ITickEvent.IServerTickEvent.class,
          UpdateHandler::onTickServerTick
        );
    }

    public IProxy getProxy()
    {
        return ProxyHolder.getInstance();
    }

    public void postInit()
    {
        getProxy().getStyleManager().loadStyles();

        final Set<Class<?>> clzs = ProxyHolder.getInstance().getFactoryController().getAllKnownTypes();
        final IProgressBar dependencyEventResolverBar = IProgressManager.push("Analyzing known elements. Determining dependency objects and events.", clzs.size());
        clzs.forEach(clz -> {
            dependencyEventResolverBar.step(clz.getName());
            ReflectionManager.getInstance().getFieldsForClass(clz);
        });
        IProgressManager.pop(dependencyEventResolverBar);
    }
}
