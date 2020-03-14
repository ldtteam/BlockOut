package com.ldtteam.blockout.util;

import net.minecraft.util.ResourceLocation;

public final class BlockOutControlImplementationsConstants {

    private BlockOutControlImplementationsConstants() {
        throw new IllegalStateException("Tried to initialize: BlockOutControlImplementationsConstants but this is a Utility class.");
    }

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "control-implementations");
}
