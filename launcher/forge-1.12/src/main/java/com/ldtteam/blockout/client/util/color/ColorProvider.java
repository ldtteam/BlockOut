package com.ldtteam.blockout.client.util.color;

import com.ldtteam.blockout.util.color.IColor;
import com.ldtteam.blockout.util.color.IColorProvider;
import net.minecraft.client.renderer.GlStateManager;

public class ColorProvider implements IColorProvider
{
    private static ColorProvider ourInstance = new ColorProvider();

    private ColorProvider()
    {
    }

    public static ColorProvider getInstance()
    {
        return ourInstance;
    }

    /**
     * Convenient function to reset the color in the GL Buffer to the Default.
     */
    @Override
    public void resetOpenGLColoring()
    {
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    @Override
    public IColor create(final int color)
    {
        return new Color(color, true);
    }
}
