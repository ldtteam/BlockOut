package com.ldtteam.blockout.util.color;

import com.google.common.collect.Maps;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ColorUtils
{
    public static int MARKER = 0xE800;
    private static final Map<String, Integer> nameToColorMap = Maps.newHashMap();
    
    static {
        nameToColorMap.put("AliceBlue".toLowerCase(),  0xF0F8FF);
        nameToColorMap.put("AntiqueWhite".toLowerCase(),  0xFAEBD7);
        nameToColorMap.put("Aqua".toLowerCase(),  0x00FFFF);
        nameToColorMap.put("Aquamarine".toLowerCase(),  0x7FFFD4);
        nameToColorMap.put("Azure".toLowerCase(),  0xF0FFFF);
        nameToColorMap.put("Beige".toLowerCase(),  0xF5F5DC);
        nameToColorMap.put("Bisque".toLowerCase(),  0xFFE4C4);
        nameToColorMap.put("Black".toLowerCase(),  0x000000);
        nameToColorMap.put("BlanchedAlmond".toLowerCase(),  0xFFEBCD);
        nameToColorMap.put("Blue".toLowerCase(),  0x0000FF);
        nameToColorMap.put("BlueViolet".toLowerCase(),  0x8A2BE2);
        nameToColorMap.put("Brown".toLowerCase(),  0xA52A2A);
        nameToColorMap.put("BurlyWood".toLowerCase(),  0xDEB887);
        nameToColorMap.put("CadetBlue".toLowerCase(),  0x5F9EA0);
        nameToColorMap.put("Chartreuse".toLowerCase(),  0x7FFF00);
        nameToColorMap.put("Chocolate".toLowerCase(),  0xD2691E);
        nameToColorMap.put("Coral".toLowerCase(),  0xFF7F50);
        nameToColorMap.put("CornflowerBlue".toLowerCase(),  0x6495ED);
        nameToColorMap.put("Cornsilk".toLowerCase(),  0xFFF8DC);
        nameToColorMap.put("Crimson".toLowerCase(),  0xDC143C);
        nameToColorMap.put("Cyan".toLowerCase(),  0x00FFFF);
        nameToColorMap.put("DarkBlue".toLowerCase(),  0x00008B);
        nameToColorMap.put("DarkCyan".toLowerCase(),  0x008B8B);
        nameToColorMap.put("DarkGoldenRod".toLowerCase(),  0xB8860B);
        nameToColorMap.put("DarkGray".toLowerCase(),  0xA9A9A9);
        nameToColorMap.put("DarkGreen".toLowerCase(),  0x006400);
        nameToColorMap.put("DarkKhaki".toLowerCase(),  0xBDB76B);
        nameToColorMap.put("DarkMagenta".toLowerCase(),  0x8B008B);
        nameToColorMap.put("DarkOliveGreen".toLowerCase(),  0x556B2F);
        nameToColorMap.put("DarkOrange".toLowerCase(),  0xFF8C00);
        nameToColorMap.put("DarkOrchid".toLowerCase(),  0x9932CC);
        nameToColorMap.put("DarkRed".toLowerCase(),  0x8B0000);
        nameToColorMap.put("DarkSalmon".toLowerCase(),  0xE9967A);
        nameToColorMap.put("DarkSeaGreen".toLowerCase(),  0x8FBC8F);
        nameToColorMap.put("DarkSlateBlue".toLowerCase(),  0x483D8B);
        nameToColorMap.put("DarkSlateGray".toLowerCase(),  0x2F4F4F);
        nameToColorMap.put("DarkTurquoise".toLowerCase(),  0x00CED1);
        nameToColorMap.put("DarkViolet".toLowerCase(),  0x9400D3);
        nameToColorMap.put("DeepPink".toLowerCase(),  0xFF1493);
        nameToColorMap.put("DeepSkyBlue".toLowerCase(),  0x00BFFF);
        nameToColorMap.put("DimGray".toLowerCase(),  0x696969);
        nameToColorMap.put("DodgerBlue".toLowerCase(),  0x1E90FF);
        nameToColorMap.put("FireBrick".toLowerCase(),  0xB22222);
        nameToColorMap.put("FloralWhite".toLowerCase(),  0xFFFAF0);
        nameToColorMap.put("ForestGreen".toLowerCase(),  0x228B22);
        nameToColorMap.put("Fuchsia".toLowerCase(),  0xFF00FF);
        nameToColorMap.put("Gainsboro".toLowerCase(),  0xDCDCDC);
        nameToColorMap.put("GhostWhite".toLowerCase(),  0xF8F8FF);
        nameToColorMap.put("Gold".toLowerCase(),  0xFFD700);
        nameToColorMap.put("GoldenRod".toLowerCase(),  0xDAA520);
        nameToColorMap.put("Gray".toLowerCase(),  0x808080);
        nameToColorMap.put("Green".toLowerCase(),  0x008000);
        nameToColorMap.put("GreenYellow".toLowerCase(),  0xADFF2F);
        nameToColorMap.put("HoneyDew".toLowerCase(),  0xF0FFF0);
        nameToColorMap.put("HotPink".toLowerCase(),  0xFF69B4);
        nameToColorMap.put("IndianRed".toLowerCase(),  0xCD5C5C);
        nameToColorMap.put("Indigo".toLowerCase(),  0x4B0082);
        nameToColorMap.put("Ivory".toLowerCase(),  0xFFFFF0);
        nameToColorMap.put("Khaki".toLowerCase(),  0xF0E68C);
        nameToColorMap.put("Lavender".toLowerCase(),  0xE6E6FA);
        nameToColorMap.put("LavenderBlush".toLowerCase(),  0xFFF0F5);
        nameToColorMap.put("LawnGreen".toLowerCase(),  0x7CFC00);
        nameToColorMap.put("LemonChiffon".toLowerCase(),  0xFFFACD);
        nameToColorMap.put("LightBlue".toLowerCase(),  0xADD8E6);
        nameToColorMap.put("LightCoral".toLowerCase(),  0xF08080);
        nameToColorMap.put("LightCyan".toLowerCase(),  0xE0FFFF);
        nameToColorMap.put("LightGoldenRodYellow".toLowerCase(),  0xFAFAD2);
        nameToColorMap.put("LightGray".toLowerCase(),  0xD3D3D3);
        nameToColorMap.put("LightGreen".toLowerCase(),  0x90EE90);
        nameToColorMap.put("LightPink".toLowerCase(),  0xFFB6C1);
        nameToColorMap.put("LightSalmon".toLowerCase(),  0xFFA07A);
        nameToColorMap.put("LightSeaGreen".toLowerCase(),  0x20B2AA);
        nameToColorMap.put("LightSkyBlue".toLowerCase(),  0x87CEFA);
        nameToColorMap.put("LightSlateGray".toLowerCase(),  0x778899);
        nameToColorMap.put("LightSteelBlue".toLowerCase(),  0xB0C4DE);
        nameToColorMap.put("LightYellow".toLowerCase(),  0xFFFFE0);
        nameToColorMap.put("Lime".toLowerCase(),  0x00FF00);
        nameToColorMap.put("LimeGreen".toLowerCase(),  0x32CD32);
        nameToColorMap.put("Linen".toLowerCase(),  0xFAF0E6);
        nameToColorMap.put("Magenta".toLowerCase(),  0xFF00FF);
        nameToColorMap.put("Maroon".toLowerCase(),  0x800000);
        nameToColorMap.put("MediumAquaMarine".toLowerCase(),  0x66CDAA);
        nameToColorMap.put("MediumBlue".toLowerCase(),  0x0000CD);
        nameToColorMap.put("MediumOrchid".toLowerCase(),  0xBA55D3);
        nameToColorMap.put("MediumPurple".toLowerCase(),  0x9370DB);
        nameToColorMap.put("MediumSeaGreen".toLowerCase(),  0x3CB371);
        nameToColorMap.put("MediumSlateBlue".toLowerCase(),  0x7B68EE);
        nameToColorMap.put("MediumSpringGreen".toLowerCase(),  0x00FA9A);
        nameToColorMap.put("MediumTurquoise".toLowerCase(),  0x48D1CC);
        nameToColorMap.put("MediumVioletRed".toLowerCase(),  0xC71585);
        nameToColorMap.put("MidnightBlue".toLowerCase(),  0x191970);
        nameToColorMap.put("MintCream".toLowerCase(),  0xF5FFFA);
        nameToColorMap.put("MistyRose".toLowerCase(),  0xFFE4E1);
        nameToColorMap.put("Moccasin".toLowerCase(),  0xFFE4B5);
        nameToColorMap.put("NavajoWhite".toLowerCase(),  0xFFDEAD);
        nameToColorMap.put("Navy".toLowerCase(),  0x000080);
        nameToColorMap.put("OldLace".toLowerCase(),  0xFDF5E6);
        nameToColorMap.put("Olive".toLowerCase(),  0x808000);
        nameToColorMap.put("OliveDrab".toLowerCase(),  0x6B8E23);
        nameToColorMap.put("Orange".toLowerCase(),  0xFFA500);
        nameToColorMap.put("OrangeRed".toLowerCase(),  0xFF4500);
        nameToColorMap.put("Orchid".toLowerCase(),  0xDA70D6);
        nameToColorMap.put("PaleGoldenRod".toLowerCase(),  0xEEE8AA);
        nameToColorMap.put("PaleGreen".toLowerCase(),  0x98FB98);
        nameToColorMap.put("PaleTurquoise".toLowerCase(),  0xAFEEEE);
        nameToColorMap.put("PaleVioletRed".toLowerCase(),  0xDB7093);
        nameToColorMap.put("PapayaWhip".toLowerCase(),  0xFFEFD5);
        nameToColorMap.put("PeachPuff".toLowerCase(),  0xFFDAB9);
        nameToColorMap.put("Peru".toLowerCase(),  0xCD853F);
        nameToColorMap.put("Pink".toLowerCase(),  0xFFC0CB);
        nameToColorMap.put("Plum".toLowerCase(),  0xDDA0DD);
        nameToColorMap.put("PowderBlue".toLowerCase(),  0xB0E0E6);
        nameToColorMap.put("Purple".toLowerCase(),  0x800080);
        nameToColorMap.put("Red".toLowerCase(),  0xFF0000);
        nameToColorMap.put("RosyBrown".toLowerCase(),  0xBC8F8F);
        nameToColorMap.put("RoyalBlue".toLowerCase(),  0x4169E1);
        nameToColorMap.put("SaddleBrown".toLowerCase(),  0x8B4513);
        nameToColorMap.put("Salmon".toLowerCase(),  0xFA8072);
        nameToColorMap.put("SandyBrown".toLowerCase(),  0xF4A460);
        nameToColorMap.put("SeaGreen".toLowerCase(),  0x2E8B57);
        nameToColorMap.put("SeaShell".toLowerCase(),  0xFFF5EE);
        nameToColorMap.put("Sienna".toLowerCase(),  0xA0522D);
        nameToColorMap.put("Silver".toLowerCase(),  0xC0C0C0);
        nameToColorMap.put("SkyBlue".toLowerCase(),  0x87CEEB);
        nameToColorMap.put("SlateBlue".toLowerCase(),  0x6A5ACD);
        nameToColorMap.put("SlateGray".toLowerCase(),  0x708090);
        nameToColorMap.put("Snow".toLowerCase(),  0xFFFAFA);
        nameToColorMap.put("SpringGreen".toLowerCase(),  0x00FF7F);
        nameToColorMap.put("SteelBlue".toLowerCase(),  0x4682B4);
        nameToColorMap.put("Tan".toLowerCase(),  0xD2B48C);
        nameToColorMap.put("Teal".toLowerCase(),  0x008080);
        nameToColorMap.put("Thistle".toLowerCase(),  0xD8BFD8);
        nameToColorMap.put("Tomato".toLowerCase(),  0xFF6347);
        nameToColorMap.put("Turquoise".toLowerCase(),  0x40E0D0);
        nameToColorMap.put("Violet".toLowerCase(),  0xEE82EE);
        nameToColorMap.put("Wheat".toLowerCase(),  0xF5DEB3);
        nameToColorMap.put("White".toLowerCase(),  0xFFFFFF);
        nameToColorMap.put("WhiteSmoke".toLowerCase(),  0xF5F5F5);
        nameToColorMap.put("Yellow".toLowerCase(),  0xFFFF00);
        nameToColorMap.put("YellowGreen".toLowerCase(),  0x9ACD32);
    }

    private ColorUtils()
    {
        throw new IllegalStateException("Tried to initialize: ColorUtils but this is a Utility class.");
    }

    @NotNull
    @SideOnly(Side.CLIENT)
    public static String convertToFontRendererColor(@NotNull final String input)
    {
        if (input.startsWith(String.valueOf((char) MARKER)))
        {
            return input;
        }

        if (input.isEmpty())
            return TextFormatting.RESET.toString();

        if (nameToColorMap.containsKey(input.toLowerCase()))
            return new Color(nameToColorMap.get(input.toLowerCase())).encodeColor();

        try {
            return new Color(Integer.decode(input), true).encodeColor();
        }
        catch (Exception e)
        {
            return input;
        }
    }
}
