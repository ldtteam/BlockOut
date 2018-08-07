package com.minecolonies.blockout.util;

import net.minecraft.util.ResourceLocation;

public class Constants
{
    public static final String MOD_ID   = "blockout";
    public static final String MOD_NAME = "BlockOut";

    public static final String VERSION       = "@VERSION@";
    public static final String MC_VERSION    = "@MCVERSION@";
    public static final String FORGE_VERSION = "required:forge;";
    public static final String NETWORK_NAME  = MOD_ID + ":network";

    public static final String PROXY_CLIENT = "com.minecolonies.blockout.proxy.ClientProxy";
    public static final String PROXY_COMMON = "com.minecolonies.blockout.proxy.CommonProxy";

    public static final String DEBUG = "@DEBUG@";

    public static final Long SERIAL_VAR_ID = Long.valueOf(MC_VERSION.hashCode());

    public static class Styles
    {
        public static final ResourceLocation CONST_DEFAULT = new ResourceLocation(MOD_ID, "default");
    }

    public static class Resources
    {
        public static final String CONST_IMAGE_RESOURCE_TYPE     = "image";
        public static final String CONST_ITEMSTACK_RESOURCE_TYPE = "itemstack";
        public static final String CONST_TEMPLATE_RESOURCE_TYPE  = "template";
    }

    public static class Controls
    {

        public static class General
        {
            public static final String CONST_ID           = "id";
            public static final String CONST_STYLE_ID     = "style";
            public static final String CONST_ALIGNMENT    = "alignment";
            public static final String CONST_DOCK         = "dock";
            public static final String CONST_MARGIN       = "margin";
            public static final String CONST_PADDING      = "padding";
            public static final String CONST_ELEMENT_SIZE = "size";
            public static final String CONST_VISIBLE      = "visible";
            public static final String CONST_ENABLED      = "enabled";
        }

        public static class Image
        {
            public static final ResourceLocation KEY_IMAGE = new ResourceLocation(MOD_ID, "image");

            public static final String CONST_ICON       = "icon";
        }

        public static class ItemIcon
        {
            public static final ResourceLocation KEY_ITEM = new ResourceLocation(MOD_ID, "itemIcon");

            public static final String CONST_ICON       = "icon";
        }

        public static class Root
        {
            public static final ResourceLocation KEY_ROOT = new ResourceLocation(MOD_ID, "root");
        }

        public static class Slot
        {
            public static final ResourceLocation KEY_SLOT = new ResourceLocation(MOD_ID, "slot");

            public static final String CONST_BACKGROUND_IMAGE = "background";
            public static final String CONST_INVENTORY_ID     = "inventory";
            public static final String CONST_INVENTORY_INDEX  = "index";
        }

        public static class Button
        {
            public static final ResourceLocation KEY_BUTTON = new ResourceLocation(MOD_ID, "button");

            public static final String CONST_DEFAULT_BACKGROUND_IMAGE       = "defaultBackgroundImage";
            public static final String CONST_DISABLED_BACKGROUND_IMAGE      = "disabledBackgroundImage";
            public static final String CONST_CLICKED_BACKGROUND_IMAGE       = "clickedBackgroundImage";
            public static final String CONST_INITIALLY_CLICKED              = "clicked";
        }

        public static class Template
        {
            public static final ResourceLocation KEY_TEMPLATE = new ResourceLocation(MOD_ID, "template");
        }

        public static class Label
        {
            public static final ResourceLocation KEY_LABEL = new ResourceLocation(MOD_ID, "label");

            public static final String CONST_CONTENT = "content";
        }

        public static class TextField
        {
            public static final ResourceLocation KEY_TEXT_FIELD = new ResourceLocation(MOD_ID, "textfield");

            public static final String CONST_CONTENT = "content";
        }

        public static class ProgressBar
        {
            public static final ResourceLocation KEY_PROGRESS_BAR = new ResourceLocation(MOD_ID, "progressbar");

            public static final String CONST_BACKGROUND_IMAGE      = "backgroundImage";
            public static final String CONST_FOREGROUND_IMAGE      = "foregroundImage";
            public static final String CONST_ORIENTATION           = "orientation";
            public static final String CONST_MIN                   = "min";
            public static final String CONST_MAX                   = "max";
            public static final String CONST_VALUE                 = "value";
        }

        public static class Region
        {
            public static final ResourceLocation KEY_REGION = new ResourceLocation(MOD_ID, "region");
        }

        public static class List
        {
            public static final ResourceLocation KEY_LIST = new ResourceLocation(MOD_ID, "list");

            public static final String CONST_SCROLL_BACKGROUND = "scrollBackground";
            public static final String CONST_SCROLL_FOREGROUND = "scrollForeground";
            public static final String CONST_TEMPLATE          = "template";
            public static final String CONST_SCROLLOFFSET      = "initialOffset";
        }
    }

}
