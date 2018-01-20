package com.minecolonies.blockout;

import com.minecolonies.blockout.loader.LoaderManager;
import com.minecolonies.blockout.loader.json.JsonLoader;
import com.minecolonies.blockout.loader.xml.XMLLoader;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION, acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOut
{

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
            LoaderManager.registerLoader(new XMLLoader());
            LoaderManager.registerLoader(new JsonLoader());
        }
    }
}
