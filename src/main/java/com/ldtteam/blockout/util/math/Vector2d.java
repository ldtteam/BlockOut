package com.ldtteam.blockout.util.math;

import org.jetbrains.annotations.NotNull;

public final class Vector2d
{
    private final double x;
    private final double y;

    /**
     * Creates a new vector in the origin.
     */
    public Vector2d()
    {
        this(0d, 0d);
    }

    /**
     * Creates a new vector that points to d,d.
     * @param d The x and y value.
     */
    public Vector2d(final double d)
    {
        this(d, d);
    }

    /**
     * Creation constructor. Creates a new {@link Vector2d} from the given x, y, z.
     *
     * @param x The X-Coordinate.
     * @param y The Y-Coordinate.
     */
    public Vector2d(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor. Copies its values from the fiven {@link Vector2d}
     *
     * @param other The other {@link Vector2d} to copy from.
     */
    public Vector2d(@NotNull final Vector2d other)
    {
        this(other.getX(), other.getY());
    }

    /**
     * Getter for the X-Coordinate.
     *
     * @return The X-Coordinate.
     */
    public double getX()
    {
        return x;
    }

    /**
     * Getter for the Y-Coordinate
     *
     * @return The Y-Coordinate
     */
    public double getY()
    {
        return y;
    }

    /**
     * Creates a new moved vector where all coordinate elements are moved with the given delta
     *
     * @param delta The distance to move all coordinate elements.
     * @return A new {@link Vector2d} moved with the given delta.
     */
    @NotNull
    public Vector2d move(final double delta)
    {
        return move(delta, delta);
    }

    @NotNull
    public Vector2d move(final double deltaX, final double deltaY)
    {
        return new Vector2d(getX() + deltaX, getY() + deltaY);
    }

    @NotNull
    public Vector2d move(final Vector2d delta)
    {
        return move(delta.getX(), delta.getY());
    }

    @NotNull
    public Vector2d invert()
    {
        return mul(-1d);
    }

    @NotNull
    public Vector2d mul(final double val)
    {
        return new Vector2d(getX() * val, getY() * val);
    }

    @NotNull
    public double cross(Vector2d B)
    {
        return getX() * B.getY() - getY() * B.getX();
    }

    @NotNull
    public Vector2d normalize()
    {
        final double t = length();
        return new Vector2d(getX() / t, getY() / t);
    }

    public double length()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    @NotNull
    public Vector2d nullifyPositives()
    {
        return new Vector2d(getX() > 0 ? 0 : getX(), getY() > 0 ? 0 : getY());
    }

    @NotNull
    public Vector2d nullifyNegatives()
    {
        return invert().nullifyPositives().invert();
    }

    @NotNull
    public Vector2d clamp(@NotNull final Vector2d min, @NotNull final Vector2d max)
    {
        return new Vector2d(
          getAxisValueIfInBetween(Axis.X, this, min, max),
          getAxisValueIfInBetween(Axis.Y, this, min, max)
        );
    }

    private double getAxisValueIfInBetween(final Axis axis, @NotNull final Vector2d target, @NotNull final Vector2d min, @NotNull final Vector2d max)
    {
        return Math.min(max.get(axis), Math.max(min.get(axis), target.get(axis)));
    }

    public double get(final Axis a)
    {
        switch (a)
        {
            case X:
                return getX();
            case Y:
                return getY();
        }

        throw new IllegalArgumentException("Unknown Axis");
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getX());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Vector2d))
        {
            return false;
        }

        final Vector2d vector2d = (Vector2d) o;

        if (Double.compare(vector2d.getX(), getX()) != 0)
        {
            return false;
        }
        return Double.compare(vector2d.getY(), getY()) == 0;
    }

    @Override
    public String toString()
    {
        return String.format("%s,%s", x, y);
    }

    @NotNull
    public Vector2d maximize(@NotNull final Vector2d other)
    {
        return new Vector2d(Math.max(getX(), other.getX()), Math.max(getY(), other.getY()));
    }

    @NotNull
    public static Vector2d maximize(@NotNull final Vector2d first, @NotNull final Vector2d second)
    {
        return first.maximize(second);
    }
    
    @NotNull
    public Vector2d minimize(@NotNull final Vector2d other)
    {
        return new Vector2d(Math.min(getX(), other.getX()), Math.min(getY(), other.getY()));
    }

    @NotNull
    public static Vector2d minimize(@NotNull final Vector2d first, @NotNull final Vector2d second)
    {
        return first.minimize(second);
    }
}
