package com.ldtteam.blockout.util.color;

import com.ldtteam.jvoxelizer.core.provider.holder.AbstractHolder;

final class IColorProviderHolder extends AbstractHolder<IColorProvider> implements IColorProvider
{
    private static IColorProviderHolder ourInstance = new IColorProviderHolder();

    private IColorProviderHolder()
    {
        super(IColor.class.getName());
    }

    public static IColorProviderHolder getInstance()
    {
        return ourInstance;
    }

    /**
     * Convenient function to reset the color in the GL Buffer to the Default.
     */
    @Override
    public void resetOpenGLColoring()
    {
        getProvider().resetOpenGLColoring();
    }

    @Override
    public IColor create(final int color)
    {
        return getProvider().create(color);
    }
}