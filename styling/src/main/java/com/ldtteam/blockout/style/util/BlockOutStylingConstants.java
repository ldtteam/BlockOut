package com.ldtteam.blockout.style.util;

import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;

public final class BlockOutStylingConstants {

    private BlockOutStylingConstants() {
        throw new IllegalStateException("Tried to initialize: BlockOutStylingConstants but this is a Utility class.");
    }

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "styling");
}
