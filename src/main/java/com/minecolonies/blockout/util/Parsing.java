package com.minecolonies.blockout.util;

import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.minecolonies.blockout.util.Log.getLogger;

/**
 * A utility class with method that handle parsing tasks.
 */
public class Parsing
{
    private static final Pattern PERCENTAGE_PATTERN = Pattern.compile("([-+]?\\d+)(%|px)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern RGBA_PATTERN       =
      Pattern.compile("rgba?\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*(?:,\\s*([01]\\.\\d+)\\s*)?\\)", Pattern.CASE_INSENSITIVE);

    private Parsing() {}

    /**
     * Parse the scalable integer attribute from name and definition.
     *
     * @param attr  the attr.
     * @param def   the definition.
     * @param scale the scale.
     * @return the integer.
     */
    public static int parseScalableInteger(final String attr, final int def, final int scale)
    {
        if (attr != null)
        {
            final Matcher m = PERCENTAGE_PATTERN.matcher(attr);
            if (m.find())
            {
                return parseScalableIntegerUsingMatcher(m, def, scale);
            }
        }

        return def;
    }

    /**
     * Parses a scaled integer using the output of the {@link Matcher}.
     *
     * @param m     The matcher whos output is used.
     * @param def   The default value.
     * @param scale The maximal value.
     * @return The scaled value.
     */
    public static int parseScalableIntegerUsingMatcher(final Matcher m, final int def, final int scale)
    {
        try
        {
            int value = Integer.parseInt(m.group(1));

            if ("%".equals(m.group(2)))
            {
                value = scale * MathHelper.clamp(value, 0, 100) / 100;
            }
            //  DO NOT attempt to do a "value < 0" treated as (100% of parent) - abs(size)
            //  without differentiating between 'size' and 'position' value types
            //  even then, it's probably not actually necessary...

            return value;
        }
        catch (NumberFormatException | IndexOutOfBoundsException | IllegalStateException ex)
        {
            getLogger().warn(ex);
        }

        return def;
    }

    /**
     * Parse the size pair attribute.
     *
     * @param attr  the name.
     * @param def   the definition.
     * @param scale the scale.
     * @return the SizePair.
     */
    @Nullable
    public static SizePair parseSizePairAttribute(final String attr, final SizePair def, final SizePair scale)
    {
        if (attr != null)
        {
            int w = def != null ? def.getX() : 0;
            int h = def != null ? def.getY() : 0;

            final Matcher m = PERCENTAGE_PATTERN.matcher(attr);
            if (m.find())
            {
                w = Parsing.parseScalableIntegerUsingMatcher(m, w, scale != null ? scale.getX() : 0);

                if (m.find() || m.find(0))
                {
                    //  If no second value is passed, use the first value
                    h = Parsing.parseScalableIntegerUsingMatcher(m, h, scale != null ? scale.getY() : 0);
                }
            }

            return new SizePair(w, h);
        }

        return def;
    }

    /**
     * Parse the color attribute from name and definition.
     *
     * @param attr the attr.
     * @param def  the definition
     * @return int color value.
     */
    public static int parseColor(final String attr, final int def)
    {
        if (attr == null)
        {
            return def;
        }

        final Matcher m = RGBA_PATTERN.matcher(attr);

        if (attr.startsWith("#"))
        {
            //  CSS Hex format: #00112233
            return Integer.parseInt(attr.substring(1), 16);
        }
        //  CSS RGB format: rgb(255,0,0) and rgba(255,0,0,0.3)
        else if ((attr.startsWith("rgb(") || attr.startsWith("rgba(")) && m.find())
        {
            return getRGBA(attr, m);
        }
        else
        {
            return getColorByNumberOrName(def, attr);
        }
    }

    private static int getRGBA(final String attr, final Matcher m)
    {
        final int r = MathHelper.clamp(Integer.parseInt(m.group(1)), 0, 255);
        final int g = MathHelper.clamp(Integer.parseInt(m.group(2)), 0, 255);
        final int b = MathHelper.clamp(Integer.parseInt(m.group(3)), 0, 255);

        int color = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);

        if (attr.startsWith("rgba"))
        {
            final int alpha = (int) (Double.parseDouble(m.group(4)) * 255.0F);
            color |= MathHelper.clamp(alpha, 0, 255) << 24;
        }

        return color;
    }

    private static int getColorByNumberOrName(final int def, final String attr)
    {
        try
        {
            return Integer.parseInt(attr);
        }
        catch (final NumberFormatException ex)
        {
            return Color.getByName(attr, def);
        }
    }
}
