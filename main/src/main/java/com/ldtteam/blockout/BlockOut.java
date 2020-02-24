package com.ldtteam.blockout;

import com.ldtteam.blockout.compat.UpdateHandler;
import com.ldtteam.blockout.element.advanced.TemplateInstance;
import com.ldtteam.blockout.element.advanced.list.factory.ListFactory;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.*;
import com.ldtteam.blockout.element.template.Template;
import com.ldtteam.blockout.loader.binding.DataContextBindingCommand;
import com.ldtteam.blockout.loader.binding.transformer.IntToStringTransformer;
import com.ldtteam.blockout.loader.object.loader.ObjectUIElementLoader;
import com.ldtteam.blockout.network.NetworkManager;
import com.ldtteam.blockout.proxy.ClientProxy;
import com.ldtteam.blockout.proxy.CommonProxy;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.reflection.ReflectionManager;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.style.resources.ItemStackResource;
import com.ldtteam.blockout.style.resources.TemplateResource;
import com.ldtteam.blockout.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.Set;

@Mod(Constants.MOD_ID)
public class BlockOut
{
    private static BlockOut INSTANCE;

    public BlockOut()
    {
        BlockOut.INSTANCE = this;

        Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onCommonSetup);
        Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onLoadCompleted);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().addListener(UpdateHandler::onPlayerLoggedOut);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().addListener(UpdateHandler::onTickClientTick);
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().addListener(UpdateHandler::onTickServerTick);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onClientSetup));
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onDedicatedServerSetup));

        ProxyHolder.getInstance().setProxy(
                DistExecutor.runForDist(
                        () -> ClientProxy::new,
                        () -> CommonProxy::new
                )
        );
    }

    public static BlockOut getInstance()
    {
        return INSTANCE;
    }

    public IProxy getProxy()
    {
        return IProxy.getInstance();
    }

    public void onCommonSetup(final FMLCommonSetupEvent event)
    {
        getProxy().onCommonSetup();

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
        getProxy().getBindingEngine().registerBindingTransformer(new IntToStringTransformer());

        getProxy().getPluginRegistry().performAutomaticDiscovery();
        getProxy().getPluginRegistry().getPlugins().values().forEach(p -> p.onCommonSetup(event));

        NetworkManager.init();
    }

    public void onClientSetup(final FMLClientSetupEvent event)
    {
        getProxy().onClientSetup();
        getProxy().getPluginRegistry().getPlugins().values().forEach(p -> p.onClientSetup(event));
    }

    public void onDedicatedServerSetup(final FMLDedicatedServerSetupEvent event)
    {
        getProxy().onDedicatedServerSetup();
        getProxy().getPluginRegistry().getPlugins().values().forEach(p -> p.onDedicatedServerSetup(event));
    }

    public void onLoadCompleted(final FMLLoadCompleteEvent event)
    {
        getProxy().getStyleManager().loadStyles();

        final Set<Class<?>> clzs = ProxyHolder.getInstance().getFactoryController().getAllKnownTypes();
        clzs.forEach(clz -> {
            ReflectionManager.getInstance().getFieldsForClass(clz);
        });
    }
}
