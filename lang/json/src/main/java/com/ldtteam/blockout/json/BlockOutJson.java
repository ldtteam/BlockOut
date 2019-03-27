package com.ldtteam.blockout.json;

@Mod(modid = com.ldtteam.blockout.json.util.Constants.MOD_ID, name = com.ldtteam.blockout.json.util.Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION + "required-after:blockout;", acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOutJson
{

    @Mod.EventHandler
    public void onFMLPreInitialization(final FMLPreInitializationEvent event)
    {
        ProxyHolder.getInstance().getLoaderManager().registerLoader(new JsonLoader());
    }
}
