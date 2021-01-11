package com.ldtteam.blockout.util;

import net.minecraft.util.ResourceLocation;

public final class BlockOutControlConceptsConstants {

    private BlockOutControlConceptsConstants() {
        throw new IllegalStateException("Tried to initialize: BlockOutControlConceptsConstants but this is a Utility class.");
    }

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "control-concepts");
}
