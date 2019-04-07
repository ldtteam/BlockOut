package com.ldtteam.blockout.utilities;

import com.ldtteam.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "blockout_utilities", name = "BlockOut - Utilities", version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION + "required-after:blockout;required-after:blockout_lang_json", acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOutUtilities
{

    @Mod.Instance
    private static BlockOutUtilities blockOutUtilities;

    public static BlockOutUtilities getBlockOutUtilities()
    {
        return blockOutUtilities;
    }
}
