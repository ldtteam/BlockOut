package com.minecolonies.blockout.util;

import net.minecraft.util.ResourceLocation;

public class Constants
{
    public static final String MOD_ID   = "blockout";
    public static final String MOD_NAME = "BlockOut";

    public static final String VERSION       = "@VERSION@";
    public static final String MC_VERSION    = "@MCVERSION@";
    public static final String FORGE_VERSION = "required:forge@[@FORGEVERSION@,);";
    public static final String NETWORK_NAME  = MOD_ID + ":network";

    public static final String PROXY_CLIENT = "com.minecolonies.blockout.proxy.ClientProxy";
    public static final String PROXY_COMMON = "com.minecolonies.blockout.proxy.CommonProxy";

    public static final Long SERIAL_VAR_ID = Long.valueOf(MC_VERSION.hashCode());

    public static class Controls
    {

        public static class General
        {
            public static final String CONST_ID           = "id";
            public static final String CONST_ALIGNMENT    = "alignment";
            public static final String CONST_DOCK         = "dock";
            public static final String CONST_MARGIN       = "margin";
            public static final String CONST_ELEMENT_SIZE = "elementSize";
            public static final String CONST_VISIBLE      = "visible";
            public static final String CONST_ENABLED      = "enabled";
        }

        public static class Image
        {
            public static final ResourceLocation KEY_ICON = new ResourceLocation(MOD_ID, "image");

            public static final String CONST_ICON = "icon";
        }
    }

}
