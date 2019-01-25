package com.ldtteam.blockout.util;

import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.reflection.ReflectionUtil;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;

public class Constants
{
    public static final String MOD_ID   = "blockout";
    public static final String MOD_NAME = "BlockOut";

    public static final String VERSION       = "@VERSION@";
    public static final String MC_VERSION    = "@MCVERSION@";
    public static final String FORGE_VERSION = "required:forge;";
    public static final String NETWORK_NAME  = MOD_ID + ":network";

    public static final String PROXY_CLIENT = "com.ldtteam.blockout.proxy.ClientProxy";
    public static final String PROXY_COMMON = "com.ldtteam.blockout.proxy.CommonProxy";

    public static final String DEBUG = "@DEBUG@";

    public static final Long SERIAL_VAR_ID = Long.valueOf(MC_VERSION.hashCode());

    public static class Styles
    {
        public static final ResourceLocation CONST_DEFAULT = new ResourceLocation(MOD_ID, "default");
    }

    public static class Resources
    {
        public static final ResourceLocation MISSING = new ResourceLocation("minecraft:missingno");
    }

    public static class ResourceTypes
    {
        public static final String CONST_IMAGE_RESOURCE_TYPE     = "image";
        public static final String CONST_ITEMSTACK_RESOURCE_TYPE = "itemstack";
        public static final String CONST_TEMPLATE_RESOURCE_TYPE  = "template";
    }

    public static class ConverterTypes
    {
        public static final Type CHILDREN_LIST_TYPE = ReflectionUtil.createParameterizedType(null, List.class, IUIElementData.class);
        public static final Type CHILDREN_LIST_FACTORY_TYPE = ReflectionUtil.createParameterizedType(null, IUIElementDataComponentConverter.class, CHILDREN_LIST_TYPE);
        
        public static final Type ALIGNMENT_ENUMSET_TYPE = ReflectionUtil.createParameterizedType(null, EnumSet.class, Alignment.class);
        public static final Type ALIGNMENT_ENUMSET_FACTORY_TYPE = ReflectionUtil.createParameterizedType(null, IUIElementDataComponentConverter.class, ALIGNMENT_ENUMSET_TYPE);

        public static final Type DOCK_ENUMSET_TYPE = ReflectionUtil.createParameterizedType(null, EnumSet.class, Dock.class);
        public static final Type DOCK_ENUMSET_FACTORY_TYPE = ReflectionUtil.createParameterizedType(null, IUIElementDataComponentConverter.class, DOCK_ENUMSET_TYPE);
    }

    public static class Controls
    {

        public static class General
        {
            public static final String CONST_TYPE = "type";
            public static final String CONST_ID           = "id";
            public static final String CONST_DATACONTEXT           = "context";
            public static final String CONST_STYLE_ID     = "style";
            public static final String CONST_ALIGNMENT    = "alignment";
            public static final String CONST_DOCK         = "dock";
            public static final String CONST_MARGIN       = "margin";
            public static final String CONST_PADDING      = "padding";
            public static final String CONST_ELEMENT_SIZE = "size";
            public static final String CONST_VISIBLE      = "visible";
            public static final String CONST_ENABLED      = "enabled";
            public static final String CONST_CHILDREN = "children";
            public static final String CONST_CONTEXT = "context";
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

        public static class CheckBox
        {
            public static final ResourceLocation KEY_CHECKBOX = new ResourceLocation(MOD_ID, "checkbox");

            public static final String CONST_DEFAULT_BACKGROUND_IMAGE  = "defaultBackgroundImage";
            public static final String CONST_DISABLED_BACKGROUND_IMAGE = "disabledBackgroundImage";
            public static final String CONST_CHECKED_BACKGROUND_IMAGE  = "checkedBackgroundImage";
            public static final String CONST_INITIALLY_CHECKED         = "checked";
        }

        public static class Template
        {
            public static final ResourceLocation KEY_TEMPLATE = new ResourceLocation(MOD_ID, "template");
        }

        public static class Label
        {
            public static final ResourceLocation KEY_LABEL = new ResourceLocation(MOD_ID, "label");

            public static final String CONST_CONTENT = "content";
            public static final String CONST_FONT_COLOR = "font-color";
        }

        public static class TextField
        {
            public static final ResourceLocation KEY_TEXT_FIELD = new ResourceLocation(MOD_ID, "textfield");

            public static final String CONST_CONTENT           = "content";
            public static final String CONST_CURSOR_POS        = "cursorCounter";
            public static final String CONST_CURSOR_SCROLL_OFF = "cursorScroll";
            public static final String CONST_CURSOR_SEL_END    = "cursor_SEL";
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
            public static final String CONST_ORIENTATION = "orientation";
            public static final String CONST_SHOW_BAR = "showScrollBar";
            public static final String CONST_SOURCE = "source";
        }

        public static class TemplateInstance
        {
            public static final ResourceLocation KEY_TEMPLATE_INSTANCE = new ResourceLocation(MOD_ID, "instance");

            public static final String CONST_TEMPLATE = "template";
        }
    }

}
