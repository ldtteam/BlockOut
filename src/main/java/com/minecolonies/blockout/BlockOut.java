package com.minecolonies.blockout;

import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.proxy.IProxy;
import com.minecolonies.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION, acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOut
{

    public static BlockOut getBlockOut()
    {
        return blockOut;
    }

    @SidedProxy(clientSide = Constants.PROXY_CLIENT, serverSide = Constants.PROXY_COMMON)
    private static IProxy proxy;

    @Mod.Instance
    private static BlockOut blockOut;

    public IProxy getProxy()
    {
        return proxy;
    }

    /**
     * Event handler for forge pre init event.
     *
     * @param event the forge pre init event.
     */
    @Mod.EventHandler
    public void preInit(@NotNull final FMLPreInitializationEvent event)
    {
        NetworkManager.init();

        if (event.getSide() == Side.CLIENT)
        {
            //ILoaderManager.registerLoader(new XMLLoader());
            //ILoaderManager.registerLoader(new JsonLoader());
        }
    }
}
