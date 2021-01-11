package com.ldtteam.blockout.lang.xml.util;

import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;

public final class BlockOutXmlConstants {

    private BlockOutXmlConstants() {
        throw new IllegalStateException("Tried to initialize: BlockOutXmlConstants but this is a Utility class.");
    }

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "xml");
}
