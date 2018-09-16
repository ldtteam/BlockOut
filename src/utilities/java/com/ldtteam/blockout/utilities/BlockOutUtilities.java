package com.ldtteam.blockout.utilities;

import com.ldtteam.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = com.ldtteam.blockout.utilities.util.Constants.MOD_ID, name = com.ldtteam.blockout.utilities.util.Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION + "required:blockout;", acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOutUtilities
{

    public static BlockOutUtilities getBlockOutUtilities()
    {
        return blockOutUtilities;
    }

    @Mod.Instance
    private static BlockOutUtilities blockOutUtilities;
}
