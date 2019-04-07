package com.ldtteam.blockout.json;

import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "blockout_lang_json", name = "BlockOut - Json Loader", version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION + "required-after:blockout;", acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOutJson
{

    @Mod.EventHandler
    public void onFMLPreInitialization(final FMLPreInitializationEvent event)
    {
        ProxyHolder.getInstance().getLoaderManager().registerLoader(new JsonLoader());
    }
}
