package com.ldtteam.blockout;

import com.ldtteam.blockout.network.NetworkManager;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.AbstractForgeMod;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.core.IForgeJVoxelizerSetupProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.jetbrains.annotations.NotNull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION, acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOutForge extends AbstractForgeMod
{

    @SidedProxy(clientSide = Constants.PROXY_CLIENT, serverSide = Constants.PROXY_COMMON)
    private static IProxy        proxy;
    @Mod.Instance
    private static BlockOutForge blockOut;

    public static BlockOutForge getBlockOut()
    {
        return blockOut;
    }

    public static boolean isDebugging() {return Constants.DEBUG.equals("@DEBUG@");}

    /**
     * Event handler for forge pre init event.
     *
     * @param event the forge pre init event.
     */
    @Mod.EventHandler
    public void preInit(@NotNull final FMLPreInitializationEvent event)
    {
        super.preInit(event);

        ProxyHolder.getInstance().setProxy(proxy);
        ProxyHolder.getInstance().onPreInit();
        NetworkManager.init();

        BlockOut.getInstance().preInit();

        //We are plugin capable. L
        preInitializePlugins();
    }

    @Override
    protected IForgeJVoxelizerSetupProxy getModSetupProxy()
    {
        return () -> {
            //Noop
        };
    }

    @Mod.EventHandler
    public void onInit(final FMLInitializationEvent event)
    {
        //TODO: Uncomment when custom fontrenderer is loaded.
        //getProxy().initializeFontRenderer();
    }

    @Mod.EventHandler
    public void onFMLPostInitialization(final FMLPostInitializationEvent event)
    {
        BlockOut.getInstance().postInit();
    }
}
