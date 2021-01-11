package com.ldtteam.blockout.util;

import net.minecraft.util.ResourceLocation;

public final class BlockOutDefinitionsConstants {

    private BlockOutDefinitionsConstants() {
        throw new IllegalStateException("Tried to initialize: BlockOutDefinitionsConstants but this is a Utility class.");
    }

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "definitions");
}
