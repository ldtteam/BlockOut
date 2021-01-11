package com.ldtteam.blockout.lang.json.util;

import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;

public final class BlockOutJsonConstants {

    private BlockOutJsonConstants() {
        throw new IllegalStateException("Tried to initialize: BlockOutJsonConstants but this is a Utility class.");
    }

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "json");
}
