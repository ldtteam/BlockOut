package com.ldtteam.blockout.proxy;

/**
 * The proxy for the font renderer.
 */
public interface IFontRendererProxy
{
    /**
     * Draws a string splitted into multiple lines at the given position with the given max width and color.
     *
     * @param translatedContents The string to draw.
     * @param x The x position of the top left corner of the text.
     * @param y The y position of the top left corner of the text.
     * @param maxWidth The maximal width of each line to draw.
     * @param color The color to draw in.
     */
    void drawSplitString(String translatedContents, int x, int y, int maxWidth, int color);

    /**
     * Trims the given string at the max width depending on the font.
     * @param stringToTrim The string to trim.
     * @param maxWidth The maximal width of the string, after which to cut of the remained.
     * @return The trimmed string.
     */
    default String trimStringToWidth(String stringToTrim, int maxWidth)
    {
        return trimStringToWidth(stringToTrim, maxWidth, false);
    }

    /**
     * Trims the given string at the max width depending on the font.
     * @param stringToTrim The string to trim.
     * @param maxWidth The maximal width of the string, after which to cut of the remained.
     * @param reverse Trim from the end.
     * @return The trimmed string.
     */
    String trimStringToWidth(String stringToTrim, int maxWidth, boolean reverse);

    /**
     * Draws a given string with a shadow in a given position and in a given color.
     *
     * @param stringToDraw The string to draw.
     * @param x The x position of the top left corner of the text.
     * @param y The y position of the top left corner of the text.
     * @param colorRGB The color to render in.
     * @return The new X position.
     */
    int drawStringWithShadow(String stringToDraw, float x, float y, int colorRGB);

    /**
     * Draws a given string without a shadow in a given position and in a given color.
     *
     * @param stringToDraw The string to draw.
     * @param x The x position of the top left corner of the text.
     * @param y The y position of the top left corner of the text.
     * @param colorRGB The color to render in.
     * @return The new X position.
     */
    int drawString(String stringToDraw, float x, float y, int colorRGB);

    /**
     * Returns the height of the current font.
     * @return The height of the font.
     */
    int getFontHeight();

    /**
     * Calculates the width of the given string depending on the size of the font.
     * @param string The string to calculate the width from.
     * @return The width of the string when rendered with the current font.
     */
    int getStringWidth(String string);
}
