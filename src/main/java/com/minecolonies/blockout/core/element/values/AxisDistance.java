package com.minecolonies.blockout.core.element.values;

import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class AxisDistance
{
    @NotNull
    private final Optional<Double> left;
    @NotNull
    private final Optional<Double> top;
    @NotNull
    private final Optional<Double> right;
    @NotNull
    private final Optional<Double> bottom;

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
}
