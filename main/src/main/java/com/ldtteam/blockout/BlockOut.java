package com.ldtteam.blockout;

import com.ldtteam.blockout.compat.UpdateHandler;
import com.ldtteam.blockout.loader.binding.DataContextBindingCommand;
import com.ldtteam.blockout.loader.binding.transformer.IntToStringTransformer;
import com.ldtteam.blockout.loader.object.loader.ObjectUIElementLoader;
import com.ldtteam.blockout.network.NetworkingManager;
import com.ldtteam.blockout.proxy.ClientProxy;
import com.ldtteam.blockout.proxy.CommonProxy;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.reflection.ReflectionManager;
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

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onClientSetup));
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(this::onDedicatedServerSetup));

        ProxyHolder.getInstance().setProxy(DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new));
    }

    public static BlockOut getInstance()
    {
        return INSTANCE;
    }

    public void onCommonSetup(final FMLCommonSetupEvent event)
    {
        IProxy.getInstance().onCommonSetup();

        IProxy.getInstance().getLoaderManager().registerLoader(new ObjectUIElementLoader());

        IProxy.getInstance().getBindingEngine().registerBindingCommand(new DataContextBindingCommand());
        IProxy.getInstance().getBindingEngine().registerBindingTransformer(new IntToStringTransformer());

        IProxy.getInstance().getPluginRegistry().performAutomaticDiscovery();
        IProxy.getInstance().getPluginRegistry().getPlugins().values().forEach(p -> p.onCommonSetup(event));

        NetworkingManager.getInstance().init();
    }

    public void onClientSetup(final FMLClientSetupEvent event)
    {
        IProxy.getInstance().onClientSetup();
        IProxy.getInstance().getPluginRegistry().getPlugins().values().forEach(p -> p.onClientSetup(event));
    }

    public void onDedicatedServerSetup(final FMLDedicatedServerSetupEvent event)
    {
        IProxy.getInstance().onDedicatedServerSetup();
        IProxy.getInstance().getPluginRegistry().getPlugins().values().forEach(p -> p.onDedicatedServerSetup(event));
    }

    public void onLoadCompleted(final FMLLoadCompleteEvent event)
    {
        IProxy.getInstance().getStyleManager().loadStyles();

        final Set<Class<?>> clzs = ProxyHolder.getInstance().getFactoryController().getAllKnownTypes();
        clzs.forEach(clz -> {
            ReflectionManager.getInstance().getFieldsForClass(clz);
        });
    }
}
