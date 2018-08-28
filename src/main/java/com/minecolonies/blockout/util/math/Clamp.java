package com.minecolonies.blockout.util.math;

public final class Clamp
{

    private Clamp()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    public static final double Clamp(double min, double value, double max)
    {
        return Math.min(max, Math.max(min, value));
    }

    public static final int Clamp(int min, int value, int max)
    {
        return Math.min(max, Math.max(min, value));
    }

    public static final float Clamp(float min, float value, float max)
    {
        return Math.min(max, Math.max(min, value));
    }

    public static final long Clamp(long min, long value, long max)
    {
        return Math.min(max, Math.max(min, value));
    }
}