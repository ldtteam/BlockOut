package com.ldtteam.blockout.util.color;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ColorUtils
{
    private static final Map<String, Integer> nameToColorMap = Maps.newHashMap();
    public static        int                  MARKER         = 0xE800;
    static
    {
        nameToColorMap.put("AliceBlue".toLowerCase(), 0xFFF0F8FF);
        nameToColorMap.put("AntiqueWhite".toLowerCase(), 0xFFFAEBD7);
        nameToColorMap.put("Aqua".toLowerCase(), 0xFF00FFFF);
        nameToColorMap.put("Aquamarine".toLowerCase(), 0xFF7FFFD4);
        nameToColorMap.put("Azure".toLowerCase(), 0xFFF0FFFF);
        nameToColorMap.put("Beige".toLowerCase(), 0xFFF5F5DC);
        nameToColorMap.put("Bisque".toLowerCase(), 0xFFFFE4C4);
        nameToColorMap.put("Black".toLowerCase(), 0xFF000000);
        nameToColorMap.put("BlanchedAlmond".toLowerCase(), 0xFFFFEBCD);
        nameToColorMap.put("Blue".toLowerCase(), 0xFF0000FF);
        nameToColorMap.put("BlueViolet".toLowerCase(), 0xFF8A2BE2);
        nameToColorMap.put("Brown".toLowerCase(), 0xFFA52A2A);
        nameToColorMap.put("BurlyWood".toLowerCase(), 0xFFDEB887);
        nameToColorMap.put("CadetBlue".toLowerCase(), 0xFF5F9EA0);
        nameToColorMap.put("Chartreuse".toLowerCase(), 0xFF7FFF00);
        nameToColorMap.put("Chocolate".toLowerCase(), 0xFFD2691E);
        nameToColorMap.put("Coral".toLowerCase(), 0xFFFF7F50);
        nameToColorMap.put("CornflowerBlue".toLowerCase(), 0xFF6495ED);
        nameToColorMap.put("Cornsilk".toLowerCase(), 0xFFFFF8DC);
        nameToColorMap.put("Crimson".toLowerCase(), 0xFFDC143C);
        nameToColorMap.put("Cyan".toLowerCase(), 0xFF00FFFF);
        nameToColorMap.put("DarkBlue".toLowerCase(), 0xFF00008B);
        nameToColorMap.put("DarkCyan".toLowerCase(), 0xFF008B8B);
        nameToColorMap.put("DarkGoldenRod".toLowerCase(), 0xFFB8860B);
        nameToColorMap.put("DarkGray".toLowerCase(), 0xFFA9A9A9);
        nameToColorMap.put("DarkGreen".toLowerCase(), 0xFF006400);
        nameToColorMap.put("DarkKhaki".toLowerCase(), 0xFFBDB76B);
        nameToColorMap.put("DarkMagenta".toLowerCase(), 0xFF8B008B);
        nameToColorMap.put("DarkOliveGreen".toLowerCase(), 0xFF556B2F);
        nameToColorMap.put("DarkOrange".toLowerCase(), 0xFFFF8C00);
        nameToColorMap.put("DarkOrchid".toLowerCase(), 0xFF9932CC);
        nameToColorMap.put("DarkRed".toLowerCase(), 0xFF8B0000);
        nameToColorMap.put("DarkSalmon".toLowerCase(), 0xFFE9967A);
        nameToColorMap.put("DarkSeaGreen".toLowerCase(), 0xFF8FBC8F);
        nameToColorMap.put("DarkSlateBlue".toLowerCase(), 0xFF483D8B);
        nameToColorMap.put("DarkSlateGray".toLowerCase(), 0xFF2F4F4F);
        nameToColorMap.put("DarkTurquoise".toLowerCase(), 0xFF00CED1);
        nameToColorMap.put("DarkViolet".toLowerCase(), 0xFF9400D3);
        nameToColorMap.put("DeepPink".toLowerCase(), 0xFFFF1493);
        nameToColorMap.put("DeepSkyBlue".toLowerCase(), 0xFF00BFFF);
        nameToColorMap.put("DimGray".toLowerCase(), 0xFF696969);
        nameToColorMap.put("DodgerBlue".toLowerCase(), 0xFF1E90FF);
        nameToColorMap.put("FireBrick".toLowerCase(), 0xFFB22222);
        nameToColorMap.put("FloralWhite".toLowerCase(), 0xFFFFFAF0);
        nameToColorMap.put("ForestGreen".toLowerCase(), 0xFF228B22);
        nameToColorMap.put("Fuchsia".toLowerCase(), 0xFFFF00FF);
        nameToColorMap.put("Gainsboro".toLowerCase(), 0xFFDCDCDC);
        nameToColorMap.put("GhostWhite".toLowerCase(), 0xFFF8F8FF);
        nameToColorMap.put("Gold".toLowerCase(), 0xFFFFD700);
        nameToColorMap.put("GoldenRod".toLowerCase(), 0xFFDAA520);
        nameToColorMap.put("Gray".toLowerCase(), 0xFF808080);
        nameToColorMap.put("Green".toLowerCase(), 0xFF008000);
        nameToColorMap.put("GreenYellow".toLowerCase(), 0xFFADFF2F);
        nameToColorMap.put("HoneyDew".toLowerCase(), 0xFFF0FFF0);
        nameToColorMap.put("HotPink".toLowerCase(), 0xFFFF69B4);
        nameToColorMap.put("IndianRed".toLowerCase(), 0xFFCD5C5C);
        nameToColorMap.put("Indigo".toLowerCase(), 0xFF4B0082);
        nameToColorMap.put("Ivory".toLowerCase(), 0xFFFFFFF0);
        nameToColorMap.put("Khaki".toLowerCase(), 0xFFF0E68C);
        nameToColorMap.put("Lavender".toLowerCase(), 0xFFE6E6FA);
        nameToColorMap.put("LavenderBlush".toLowerCase(), 0xFFFFF0F5);
        nameToColorMap.put("LawnGreen".toLowerCase(), 0xFF7CFC00);
        nameToColorMap.put("LemonChiffon".toLowerCase(), 0xFFFFFACD);
        nameToColorMap.put("LightBlue".toLowerCase(), 0xFFADD8E6);
        nameToColorMap.put("LightCoral".toLowerCase(), 0xFFF08080);
        nameToColorMap.put("LightCyan".toLowerCase(), 0xFFE0FFFF);
        nameToColorMap.put("LightGoldenRodYellow".toLowerCase(), 0xFFFAFAD2);
        nameToColorMap.put("LightGray".toLowerCase(), 0xFFD3D3D3);
        nameToColorMap.put("LightGreen".toLowerCase(), 0xFF90EE90);
        nameToColorMap.put("LightPink".toLowerCase(), 0xFFFFB6C1);
        nameToColorMap.put("LightSalmon".toLowerCase(), 0xFFFFA07A);
        nameToColorMap.put("LightSeaGreen".toLowerCase(), 0xFF20B2AA);
        nameToColorMap.put("LightSkyBlue".toLowerCase(), 0xFF87CEFA);
        nameToColorMap.put("LightSlateGray".toLowerCase(), 0xFF778899);
        nameToColorMap.put("LightSteelBlue".toLowerCase(), 0xFFB0C4DE);
        nameToColorMap.put("LightYellow".toLowerCase(), 0xFFFFFFE0);
        nameToColorMap.put("Lime".toLowerCase(), 0xFF00FF00);
        nameToColorMap.put("LimeGreen".toLowerCase(), 0xFF32CD32);
        nameToColorMap.put("Linen".toLowerCase(), 0xFFFAF0E6);
        nameToColorMap.put("Magenta".toLowerCase(), 0xFFFF00FF);
        nameToColorMap.put("Maroon".toLowerCase(), 0xFF800000);
        nameToColorMap.put("MediumAquaMarine".toLowerCase(), 0xFF66CDAA);
        nameToColorMap.put("MediumBlue".toLowerCase(), 0xFF0000CD);
        nameToColorMap.put("MediumOrchid".toLowerCase(), 0xFFBA55D3);
        nameToColorMap.put("MediumPurple".toLowerCase(), 0xFF9370DB);
        nameToColorMap.put("MediumSeaGreen".toLowerCase(), 0xFF3CB371);
        nameToColorMap.put("MediumSlateBlue".toLowerCase(), 0xFF7B68EE);
        nameToColorMap.put("MediumSpringGreen".toLowerCase(), 0xFF00FA9A);
        nameToColorMap.put("MediumTurquoise".toLowerCase(), 0xFF48D1CC);
        nameToColorMap.put("MediumVioletRed".toLowerCase(), 0xFFC71585);
        nameToColorMap.put("MidnightBlue".toLowerCase(), 0xFF191970);
        nameToColorMap.put("MintCream".toLowerCase(), 0xFFF5FFFA);
        nameToColorMap.put("MistyRose".toLowerCase(), 0xFFFFE4E1);
        nameToColorMap.put("Moccasin".toLowerCase(), 0xFFFFE4B5);
        nameToColorMap.put("NavajoWhite".toLowerCase(), 0xFFFFDEAD);
        nameToColorMap.put("Navy".toLowerCase(), 0xFF000080);
        nameToColorMap.put("OldLace".toLowerCase(), 0xFFFDF5E6);
        nameToColorMap.put("Olive".toLowerCase(), 0xFF808000);
        nameToColorMap.put("OliveDrab".toLowerCase(), 0xFF6B8E23);
        nameToColorMap.put("Orange".toLowerCase(), 0xFFFFA500);
        nameToColorMap.put("OrangeRed".toLowerCase(), 0xFFFF4500);
        nameToColorMap.put("Orchid".toLowerCase(), 0xFFDA70D6);
        nameToColorMap.put("PaleGoldenRod".toLowerCase(), 0xFFEEE8AA);
        nameToColorMap.put("PaleGreen".toLowerCase(), 0xFF98FB98);
        nameToColorMap.put("PaleTurquoise".toLowerCase(), 0xFFAFEEEE);
        nameToColorMap.put("PaleVioletRed".toLowerCase(), 0xFFDB7093);
        nameToColorMap.put("PapayaWhip".toLowerCase(), 0xFFFFEFD5);
        nameToColorMap.put("PeachPuff".toLowerCase(), 0xFFFFDAB9);
        nameToColorMap.put("Peru".toLowerCase(), 0xFFCD853F);
        nameToColorMap.put("Pink".toLowerCase(), 0xFFFFC0CB);
        nameToColorMap.put("Plum".toLowerCase(), 0xFFDDA0DD);
        nameToColorMap.put("PowderBlue".toLowerCase(), 0xFFB0E0E6);
        nameToColorMap.put("Purple".toLowerCase(), 0xFF800080);
        nameToColorMap.put("Red".toLowerCase(), 0xFFFF0000);
        nameToColorMap.put("RosyBrown".toLowerCase(), 0xFFBC8F8F);
        nameToColorMap.put("RoyalBlue".toLowerCase(), 0xFF4169E1);
        nameToColorMap.put("SaddleBrown".toLowerCase(), 0xFF8B4513);
        nameToColorMap.put("Salmon".toLowerCase(), 0xFFFA8072);
        nameToColorMap.put("SandyBrown".toLowerCase(), 0xFFF4A460);
        nameToColorMap.put("SeaGreen".toLowerCase(), 0xFF2E8B57);
        nameToColorMap.put("SeaShell".toLowerCase(), 0xFFFFF5EE);
        nameToColorMap.put("Sienna".toLowerCase(), 0xFFA0522D);
        nameToColorMap.put("Silver".toLowerCase(), 0xFFC0C0C0);
        nameToColorMap.put("SkyBlue".toLowerCase(), 0xFF87CEEB);
        nameToColorMap.put("SlateBlue".toLowerCase(), 0xFF6A5ACD);
        nameToColorMap.put("SlateGray".toLowerCase(), 0xFF708090);
        nameToColorMap.put("Snow".toLowerCase(), 0xFFFFFAFA);
        nameToColorMap.put("SpringGreen".toLowerCase(), 0xFF00FF7F);
        nameToColorMap.put("SteelBlue".toLowerCase(), 0xFF4682B4);
        nameToColorMap.put("Tan".toLowerCase(), 0xFFD2B48C);
        nameToColorMap.put("Teal".toLowerCase(), 0xFF008080);
        nameToColorMap.put("Thistle".toLowerCase(), 0xFFD8BFD8);
        nameToColorMap.put("Tomato".toLowerCase(), 0xFFFF6347);
        nameToColorMap.put("Turquoise".toLowerCase(), 0xFF40E0D0);
        nameToColorMap.put("Violet".toLowerCase(), 0xFFEE82EE);
        nameToColorMap.put("Wheat".toLowerCase(), 0xFFF5DEB3);
        nameToColorMap.put("White".toLowerCase(), 0xFFFFFFFF);
        nameToColorMap.put("WhiteSmoke".toLowerCase(), 0xFFF5F5F5);
        nameToColorMap.put("Yellow".toLowerCase(), 0xFFFFFF00);
        nameToColorMap.put("YellowGreen".toLowerCase(), 0xFF9ACD32);
    }
    private ColorUtils()
    {
        throw new IllegalStateException("Tried to initialize: ColorUtils but this is a Utility class.");
    }

    @NotNull
    public static String convertToFontRendererColor(@NotNull final String input)
    {
        if (input.startsWith(String.valueOf((char) MARKER)))
        {
            return input;
        }

        return convertToColor(input).encodeColor();
    }

    @NotNull
    public static IColor convertToColor(@NotNull final String input)
    {
        if (input.isEmpty())
        {
            return IColor.create(nameToColorMap.get("white"));
        }

        if (nameToColorMap.containsKey(input.toLowerCase()))
        {
            return IColor.create(nameToColorMap.get(input.toLowerCase()));
        }

        try
        {
            return IColor.create(Integer.parseInt(input));
        }
        catch (Exception e)
        {
            return IColor.create(nameToColorMap.get("white"));
        }
    }
}
