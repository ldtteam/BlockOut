package com.ldtteam.blockout.core.element.values;

import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class AxisDistance
{
    @NotNull
    private Optional<Double> left;
    @NotNull
    private Optional<Double> top;
    @NotNull
    private Optional<Double> right;
    @NotNull
    private Optional<Double> bottom;

    public AxisDistance()
    {
        this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public AxisDistance(@NotNull final Optional<Double> left, @NotNull final Optional<Double> top, @NotNull final Optional<Double> right, @NotNull final Optional<Double> bottom)
    {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public AxisDistance(@NotNull final Number left, @NotNull final Number top, @NotNull final Number right, @NotNull final Number bottom)
    {
        this(Optional.of(left.doubleValue()), Optional.of(top.doubleValue()), Optional.of(right.doubleValue()), Optional.of(bottom.doubleValue()));
    }

    @NotNull
    public Optional<Double> getLeft()
    {
        return left;
    }

    @NotNull
    public Optional<Double> getTop()
    {
        return top;
    }

    @NotNull
    public Optional<Double> getRight()
    {
        return right;
    }

    @NotNull
    public Optional<Double> getBottom()
    {
        return bottom;
    }

    public AxisDistance move(@NotNull final Vector2d delta)
    {
        final Optional<Double> left = this.left.map(aDouble -> aDouble + delta.getX());
        final Optional<Double> top = this.top.map(aDouble -> aDouble + delta.getY());
        final Optional<Double> right = this.right.map(aDouble -> aDouble - delta.getX());
        final Optional<Double> bottom = this.bottom.map(aDouble -> aDouble - delta.getY());

        return new AxisDistanceBuilder().setLeft(left).setTop(top).setRight(right).setBottom(bottom).create();
    }

    @Override
    public int hashCode()
    {
        int result = getLeft().hashCode();
        result = 31 * result + getTop().hashCode();
        result = 31 * result + getRight().hashCode();
        result = 31 * result + getBottom().hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final AxisDistance that = (AxisDistance) o;

        if (!getLeft().equals(that.getLeft()))
        {
            return false;
        }
        if (!getTop().equals(that.getTop()))
        {
            return false;
        }
        if (!getRight().equals(that.getRight()))
        {
            return false;
        }
        return getBottom().equals(that.getBottom());
    }

    @Override
    public String toString()
    {
        return String.format("%s,%s,%s,%s",
          getLeft().map(Object::toString).orElse(""),
          getTop().map(Object::toString).orElse(""),
          getRight().map(Object::toString).orElse(""),
          getBottom().map(Object::toString).orElse(""));
    }
}
