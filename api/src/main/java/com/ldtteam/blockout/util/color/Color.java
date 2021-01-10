package com.ldtteam.blockout.util.color;

import com.ldtteam.blockout.util.math.Vector2d;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import static com.ldtteam.blockout.util.color.ColorUtils.MARKER;

public class Color
{

    @OnlyIn(Dist.CLIENT)
    public static void resetOpenGLColoring()
    {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }

    private final int value;

    public Color(final int value) {this.value = value;}

    public Color(final int value, final int alpha)
    {
        this.value = (alpha & 255) << 24 | value;
    }

    public Color(final int red, final int green, final int blue, final int alpha)
    {
        this.value = (alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | (blue & 255);
    }

    /**
     * Returns the red component in the range 0-255 in the default sRGB
     * space.
     * @return the red component.
     * @see #getRGB
     */
    public int getRed() {
        return (getRGB() >> 16) & 0xFF;
    }

    /**
     * Returns the green component in the range 0-255 in the default sRGB
     * space.
     * @return the green component.
     * @see #getRGB
     */
    public int getGreen() {
        return (getRGB() >> 8) & 0xFF;
    }

    /**
     * Returns the blue component in the range 0-255 in the default sRGB
     * space.
     * @return the blue component.
     * @see #getRGB
     */
    public int getBlue() {
        return (getRGB() >> 0) & 0xFF;
    }

    /**
     * Returns the alpha component in the range 0-255.
     * @return the alpha component.
     * @see #getRGB
     */
    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    /**
     * Returns the RGB value representing the color in the default sRGB Color model.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     * @return the RGB value of the color in the default sRGB
     *         <code>ColorModel</code>.
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @since JDK1.0
     */
    public int getRGB() {
        return value;
    }

    /**
     * Encodes the color to be used by BlockOuts custom font renderer.
     * @return The encoded color.
     */
    public String encodeColor()
    {
        return String.format("%c%c%c%c",
          ((char) (MARKER + (getRed() & 0xFF))),
          ((char) (MARKER + (getGreen() & 0xFF))),
          ((char) (MARKER + (getBlue() & 0xFF))),
          ((char) (MARKER + (getAlpha() & 0xFF))));
    }

    /**
     * Function to get the Red value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Red Channel in this color
     */
    public float getRedFloat()
    {
        return getRed() / 255F;
    }

    /**
     * Function to get the Green value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Green Channel in this color
     */
    public float getGreenFloat()
    {
        return getGreen() / 255F;
    }

    /**
     * Function to get the Blue value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Blue Channel in this color
     */
    public float getBlueFloat()
    {
        return getBlue() / 255F;
    }

    /**
     * Function to get the Alpha value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Alpha Channel in this color
     */
    public float getAlphaFloat()
    {
        return getAlpha() / 255F;
    }

    /**
     * Convenient Function to perform Coloring of the GL buffer in this color.
     */
    @OnlyIn(Dist.CLIENT)
    public void performOpenGLColoring()
    {
        RenderSystem.color4f(getRed() / 255F, getGreen() / 255F, getBlue() / 255F, getAlpha() / 255F);
    }

    /**
     * Calculates the Angle of two Colors in Degrees, can be used to determine which color is closer to a third color.
     *
     * @return A Angle in 360 Degrees describing the color on the Adobe color wheel.
     */
    public double getAngleInDegrees()
    {
        Vector2d tRedVec = new Vector2d(getRed() * Math.cos(Math.toRadians(0)), getRed() * Math.sin(Math.toRadians(0)));
        Vector2d tGreenVec = new Vector2d(getGreen() * Math.cos(Math.toRadians(120)), getGreen() * Math.sin(Math.toRadians(120)));
        Vector2d tBlueVec = new Vector2d(getBlue() * Math.cos(Math.toRadians(240)), getBlue() * Math.sin(Math.toRadians(240)));

        Vector2d tColorVec = new Vector2d(tRedVec.getX() + tBlueVec.getX() + tGreenVec.getX(), tRedVec.getY() + tBlueVec.getY() + tGreenVec.getY());

        if (tColorVec.getY() == 0)
        {
            if (tColorVec.getX() < -10)
            {
                return 90;
            }
            else if (tColorVec.getX() > 10)
            {
                return 270;
            }
            else
            {
                return 0;
            }
        }

        if (tColorVec.getX() == 0)
        {
            if (tColorVec.getY() < -10)
            {
                return 180;
            }
            else if (tColorVec.getY() > 10)
            {
                return 0;
            }
            else
            {
                return 0;
            }
        }

        return 360 - (Math.atan((((float) tColorVec.getX()) / ((float) tColorVec.getY()))) * (180 / Math.PI));
    }
}
