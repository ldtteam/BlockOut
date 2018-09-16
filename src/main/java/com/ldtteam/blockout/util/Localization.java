package com.ldtteam.blockout.util;

import net.minecraft.client.resources.I18n;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for localization support.
 */
public class Localization
{

    private Localization() {}

    /**
     * Localizes a string.
     *
     * @param str The string to localize.
     * @return A localized string.
     */
    @Nullable
    public static String localize(@Nullable final String str)
    {
        if (str == null)
        {
            return null;
        }

        String s = str;
        int index = s.indexOf("$(");
        while (index != -1)
        {
            final int endIndex = s.indexOf(')', index);

            if (endIndex == -1)
            {
                break;
            }

            final String key = s.substring(index + 2, endIndex);
            String replacement = I18n.format(key);

            if (replacement.equals(key))
            {
                replacement = "MISSING:" + key;
            }

            s = s.substring(0, index) + replacement + s.substring(endIndex + 1);

            index = s.indexOf("$(", index + replacement.length());
        }

        return s;
    }
}
