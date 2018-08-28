package com.minecolonies.blockout.util.math;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class BoundingBox
{

    @NotNull
    private final Vector2d localOrigin;
    @NotNull
    private final Vector2d size;

    public BoundingBox()
    {
        this(new Vector2d(), new Vector2d());
    }

    public BoundingBox(final Vector2d localOrigin, final Vector2d size)
    {
        this.localOrigin = localOrigin;
        this.size = size;
    }

    public BoundingBox(@NotNull final BoundingBox other)
    {
        this(new Vector2d(other.getLocalOrigin()), new Vector2d(other.getSize()));
    }

    @NotNull
    public Vector2d getLocalOrigin()
    {
        return localOrigin;
    }

    @NotNull
    public Vector2d getSize()
    {
        return size;
    }

    @NotNull
    public Vector2d getLowerRightCoordinate()
    {
        return getLocalOrigin().move(getSize().getX(), 0);
    }

    @NotNull
    public Vector2d getUpperLeftCoordinate()
    {
        return getLocalOrigin().move(0, getSize().getY());
    }

    @NotNull
    public Vector2d getLowerLeftCoordinate()
    {
        return getLocalOrigin();
    }

    @NotNull
    public Vector2d getUpperRightCoordinate()
    {
        return getLocalOrigin().move(getSize().getX(), getSize().getY());
    }

    public boolean includes(@NotNull final Vector2d point)
    {
        return (getLowerLeftCoordinate().getX() <= point.getX() &&
                  getLowerLeftCoordinate().getY() <= point.getY() &&
                  getUpperRightCoordinate().getX() >= point.getX() &&
                  getUpperRightCoordinate().getY() >= point.getY());
    }

    public boolean includes(@NotNull final BoundingBox box)
    {
        return this.equals(include(box));
    }

    @NotNull
    public BoundingBox include(@NotNull final BoundingBox box)
    {
        return box.include(getLowerLeftCoordinate())
                 .include(getLowerRightCoordinate())
                 .include(getUpperRightCoordinate())
                 .include(getUpperLeftCoordinate());
    }

    @NotNull
    public BoundingBox include(@NotNull final Vector2d point)
    {
        if (includes(point))
        {
            return new BoundingBox(this);
        }

        final Vector2d deltaLocalOrigin = getLocalOrigin().move(point.invert()).invert().nullifyPositives();
        final Vector2d newLocalOrigin = getLocalOrigin().move(deltaLocalOrigin);

        final Vector2d deltaUpperRight = getUpperRightCoordinate().move(point.invert()).nullifyPositives().invert();
        final Vector2d newUpperRight = getUpperRightCoordinate().move(deltaUpperRight);

        final Vector2d newSize = newUpperRight.move(newLocalOrigin.invert());

        return new BoundingBox(newLocalOrigin, newSize);
    }

    @NotNull
    public BoundingBox intersect(@NotNull final BoundingBox box)
    {
        return fromExtremities(box.getLowerLeftCoordinate().clamp(getLowerLeftCoordinate(), getUpperRightCoordinate()),
          box.getUpperRightCoordinate().clamp(getLowerLeftCoordinate(), getUpperRightCoordinate()));
    }

    @NotNull
    public static BoundingBox fromExtremities(@NotNull final Vector2d lowerLeft, @NotNull final Vector2d upperRight)
    {
        return new BoundingBox(lowerLeft, upperRight.move(lowerLeft.invert()));
    }

    @Override
    public int hashCode()
    {
        int result = getLocalOrigin().hashCode();
        result = 31 * result + getSize().hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof BoundingBox))
        {
            return false;
        }

        final BoundingBox that = (BoundingBox) o;

        if (!getLocalOrigin().equals(that.getLocalOrigin()))
        {
            return false;
        }
        return getSize().equals(that.getSize());
    }

    public static BoundingBox fromString(@NotNull final String string)
    {
        final String[] parts = string.split(",");
        final Double[] components = Arrays.stream(parts).map(s -> Double.parseDouble(s.trim())).collect(Collectors.toList()).toArray(new Double[4]);

        if (components.length != 4)
        {
            throw new IllegalArgumentException("String does not contain 4 parts");
        }

        return new BoundingBox(new Vector2d(components[0], components[1]), new Vector2d(components[2], components[3]));
    }

    @Override
    public String toString()
    {
        return getLocalOrigin().getX() + "," + getLocalOrigin().getY() + "," + getSize().getX() + "," + getSize().getY() + ",";
    }
}

