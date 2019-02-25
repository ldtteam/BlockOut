package com.ldtteam.blockout.util.color;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface IColor
{
    /**
     * Convenient function to reset the color in the GL Buffer to the Default.
     */
    static void resetOpenGLColoring()
    {
        //TODO: Link open gl code.
    }

    static IColor create(int color)
    {
        throw new NotImplementedException();
    }

    String encodeColor();

    /**
     * Function to get the Red value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Red Channel in this color
     */
    float getRedFloat();

    /**
     * Function to get the Green value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Green Channel in this color
     */
    float getGreenFloat();

    /**
     * Function to get the Blue value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Blue Channel in this color
     */
    float getBlueFloat();

    /**
     * Function to get the Alpha value in Float.
     *
     * @return A Float between 0 and 1.0 indicating the state of the Alpha Channel in this color
     */
    float getAlphaFloat();

    /**
     * Convenient Function to perform Coloring of the GL buffer in this color.
     */
    void performOpenGLColoring();

    /**
     * Calculates the Angle of two Colors in Degrees, can be used to determine which color is closer to a third color.
     *
     * @return A Angle in 360 Degrees describing the color on the Adobe color wheel.
     */
    double getAngleInDegrees();

    /**
     * The int encoded RGB value of this color.
     *
     * @return The int encoded RGB channels
     */
    int getRGB();
}
