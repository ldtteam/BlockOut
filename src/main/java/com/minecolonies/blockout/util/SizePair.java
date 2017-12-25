package com.minecolonies.blockout.util;

/**
 * Size pair of width and height.
 */
public class SizePair
{
    private final int x;
    private final int y;

    /**
     * Instantiates a SizePair object.
     *
     * @param w width.
     * @param h height.
     */
    public SizePair(final int w, final int h)
    {
        x = w;
        y = h;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
