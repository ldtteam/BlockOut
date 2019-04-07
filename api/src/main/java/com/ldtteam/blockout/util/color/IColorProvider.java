package com.ldtteam.blockout.util.color;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface IColorProvider
{
    /**
     * Convenient function to reset the color in the GL Buffer to the Default.
     */
    void resetOpenGLColoring();

    IColor create(int color);
}
