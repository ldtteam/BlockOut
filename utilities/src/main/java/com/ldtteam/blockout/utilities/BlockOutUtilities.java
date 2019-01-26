package com.ldtteam.blockout.utilities;

import com.ldtteam.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = com.ldtteam.blockout.utilities.util.Constants.MOD_ID, name = com.ldtteam.blockout.utilities.util.Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION + "required:blockout;required:" + com.ldtteam.blockout.json.util.Constants.MOD_ID, acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOutUtilities
{

    @Mod.Instance
    private static BlockOutUtilities blockOutUtilities;

    public static BlockOutUtilities getBlockOutUtilities()
    {
        return blockOutUtilities;
    }
}
