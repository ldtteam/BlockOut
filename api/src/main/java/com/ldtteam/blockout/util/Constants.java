package com.ldtteam.blockout.util;

import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.reflection.ReflectionUtil;

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
        public static final String CONST_DEFAULT = String.format("%s:default", MOD_ID);
    }

    public static class Resources
    {
        public static final String MISSING = "minecraft:missingno";
    }

    public static class ResourceTypes
    {
        public static final String CONST_IMAGE_RESOURCE_TYPE     = "image";
        public static final String CONST_ITEMSTACK_RESOURCE_TYPE = "itemstack";
        public static final String CONST_TEMPLATE_RESOURCE_TYPE  = "template";
    }

    public static class ConverterTypes
    {
        public static final Type CHILDREN_LIST_TYPE         = ReflectionUtil.createParameterizedType(null, List.class, IUIElementData.class);
        public static final Type CHILDREN_LIST_FACTORY_TYPE = ReflectionUtil.createParameterizedType(null, IUIElementDataComponentConverter.class, CHILDREN_LIST_TYPE);

        public static final Type ALIGNMENT_ENUMSET_TYPE         = ReflectionUtil.createParameterizedType(null, EnumSet.class, Alignment.class);
        public static final Type ALIGNMENT_ENUMSET_FACTORY_TYPE = ReflectionUtil.createParameterizedType(null, IUIElementDataComponentConverter.class, ALIGNMENT_ENUMSET_TYPE);

        public static final Type DOCK_ENUMSET_TYPE         = ReflectionUtil.createParameterizedType(null, EnumSet.class, Dock.class);
        public static final Type DOCK_ENUMSET_FACTORY_TYPE = ReflectionUtil.createParameterizedType(null, IUIElementDataComponentConverter.class, DOCK_ENUMSET_TYPE);
    }

    public static class Controls
    {

        public static class General
        {
            public static final String CONST_TYPE         = "type";
            public static final String CONST_ID           = "id";
            public static final String CONST_DATACONTEXT  = "context";
            public static final String CONST_STYLE_ID     = "style";
            public static final String CONST_ALIGNMENT    = "alignment";
            public static final String CONST_DOCK         = "dock";
            public static final String CONST_MARGIN       = "margin";
            public static final String CONST_PADDING      = "padding";
            public static final String CONST_ELEMENT_SIZE = "size";
            public static final String CONST_VISIBLE      = "visible";
            public static final String CONST_ENABLED      = "enabled";
            public static final String CONST_CHILDREN     = "children";
            public static final String CONST_CONTEXT      = "context";
        }

        public static class Image
        {
            public static final String KEY_IMAGE = String.format("%s:image", MOD_ID);

            public static final String CONST_ICON = "icon";
        }

        public static class ItemIcon
        {
            public static final String KEY_ITEM = String.format("%s:itemIcon", MOD_ID);

            public static final String CONST_ICON = "icon";
        }

        public static class BlockStateIcon
        {
            public static final String KEY_BLOCKSTATE = String.format("%s:blockStateIcon", MOD_ID);

            public static final String CONST_BLOCK_STATE = "blockState";
        }

        public static class Root
        {
            public static final String KEY_ROOT = String.format("%s:root", MOD_ID);
        }

        public static class Slot
        {
            public static final String KEY_SLOT = String.format("%s:slot", MOD_ID);

            public static final String CONST_BACKGROUND_IMAGE = "background";
            public static final String CONST_INVENTORY_ID     = "inventory";
            public static final String CONST_INVENTORY_INDEX  = "index";
        }

        public static class Button
        {
            public static final String KEY_BUTTON = String.format("%s:button", MOD_ID);

            public static final String CONST_DEFAULT_BACKGROUND_IMAGE  = "defaultBackgroundImage";
            public static final String CONST_DISABLED_BACKGROUND_IMAGE = "disabledBackgroundImage";
            public static final String CONST_CLICKED_BACKGROUND_IMAGE  = "clickedBackgroundImage";
            public static final String CONST_INITIALLY_CLICKED         = "clicked";
        }

        public static class CheckBox
        {
            public static final String KEY_CHECKBOX = String.format("%s:checkbox", MOD_ID);

            public static final String CONST_DEFAULT_BACKGROUND_IMAGE  = "defaultBackgroundImage";
            public static final String CONST_DISABLED_BACKGROUND_IMAGE = "disabledBackgroundImage";
            public static final String CONST_CHECKED_BACKGROUND_IMAGE  = "checkedBackgroundImage";
            public static final String CONST_INITIALLY_CHECKED         = "checked";
        }

        public static class Template
        {
            public static final String KEY_TEMPLATE = String.format("%s:template", MOD_ID);
        }

        public static class Label
        {
            public static final String KEY_LABEL = String.format("%s:label", MOD_ID);

            public static final String CONST_CONTENT    = "content";
            public static final String CONST_FONT_COLOR = "font-color";
        }

        public static class TextField
        {
            public static final String KEY_TEXT_FIELD = String.format("%s:textfield", MOD_ID);

            public static final String CONST_CONTENT                = "content";
            public static final String CONST_CURSOR_POS             = "cursorCounter";
            public static final String CONST_CURSOR_SCROLL_OFF      = "cursorScroll";
            public static final String CONST_CURSOR_SEL_END         = "cursor_SEL";
            public static final String CONST_DO_BACK_DRAW           = "doBackgroundDraw";
            public static final String CONST_MAX_LENGTH             = "maxLength";
            public static final String CONST_OUTER_BACKGROUND_COLOR = "outerColor";
            public static final String CONST_INNER_BACKGROUND_COLOR = "innerColor";
            public static final String CONST_ENABLED_FONT_COLOR     = "enabledFontColor";
            public static final String CONST_DISABLED_FONT_COLOR    = "disabledFontColor";
            public static final String CONST_CURSOR_COLOR           = "cursorColor";
            public static final String CONST_SELECTION_COLOR        = "selectionColor";
        }

        public static class ProgressBar
        {
            public static final String KEY_PROGRESS_BAR = String.format("%s:progressbar", MOD_ID);

            public static final String CONST_BACKGROUND_IMAGE = "backgroundImage";
            public static final String CONST_FOREGROUND_IMAGE = "foregroundImage";
            public static final String CONST_ORIENTATION      = "orientation";
            public static final String CONST_MIN              = "min";
            public static final String CONST_MAX              = "max";
            public static final String CONST_VALUE            = "value";
        }

        public static class Region
        {
            public static final String KEY_REGION = String.format("%s:region", MOD_ID);
        }

        public static class List
        {
            public static final String KEY_LIST = String.format("%s:list", MOD_ID);

            public static final String CONST_SCROLL_BACKGROUND = "scrollBackground";
            public static final String CONST_SCROLL_FOREGROUND = "scrollForeground";
            public static final String CONST_TEMPLATE          = "template";
            public static final String CONST_SCROLLOFFSET      = "initialOffset";
            public static final String CONST_ORIENTATION       = "orientation";
            public static final String CONST_SHOW_BAR          = "showScrollBar";
            public static final String CONST_SOURCE            = "source";
        }

        public static class TemplateInstance
        {
            public static final String KEY_TEMPLATE_INSTANCE = String.format("%s:instance", MOD_ID);

            public static final String CONST_TEMPLATE = "template";
        }

        public static class RangeSelector
        {
            public static final String KEY_RANGE_SELECTOR = String.format("%s:rangeSelector", MOD_ID);

            public static final String CONST_LEFT_VALUE                = "leftValue";
            public static final String CONST_RIGHT_VALUE               = "rightValue";
            public static final String CONST_LEFT_BACKGROUND           = "leftBackground";
            public static final String CONST_RIGHT_BACKGROUND          = "rightBackground";
            public static final String CONST_SELECTED_BACKGROUND       = "selectedBackground";
            public static final String CONST_LEFT_SELECTOR_BACKGROUND  = "leftSelectorBackground";
            public static final String CONST_RIGHT_SELECTOR_BACKGROUND = "rightSelectorBackground";
        }
    }
}
