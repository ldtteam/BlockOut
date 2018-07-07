package com.minecolonies.blockout_test;

import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout_test.command.CommandOpenTestGui;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Constants.MOD_ID + "_test", name = Constants.MOD_NAME + "_Test", version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION + "required:blockout;", acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOut_Test
{

    @Mod.Instance
    private static BlockOut_Test blockOutTest;

    public static BlockOut_Test getBlockOutTest()
    {
        return blockOutTest;
    }

    @Mod.EventHandler
    public void onFMLServerStarting(final FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandOpenTestGui());
    }
}
