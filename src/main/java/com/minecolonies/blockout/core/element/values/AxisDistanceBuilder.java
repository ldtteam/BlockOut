package com.minecolonies.blockout.core.element.values;

import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class AxisDistanceBuilder
{
    private Optional<Double> left   = Optional.empty();
    private Optional<Double> top    = Optional.empty();
    private Optional<Double> right  = Optional.empty();
    private Optional<Double> bottom = Optional.empty();

    public void readFromString(@NotNull final Vector2d parentSize, @NotNull final String string)
    {
        if (string.trim().isEmpty())
        {
            setLeft(Optional.empty());
            setTop(Optional.empty());
            setRight(Optional.empty());
            setBottom(Optional.empty());
        }
        else
        {
            //Check for special behaviour.
            //S means that the string only can contain one or two components.
            //And that easy input behaviour is requested.
            if (string.startsWith("S"))
            {
                final String specialInput = string.trim().replace("S", "");
                final String[] components = specialInput.split(",");
                if (components.length == 1)
                {
                    setWithParse(this::setLeft, parentSize.getX(), components[0]);
                    setWithParse(this::setTop, parentSize.getY(), components[0]);
                    setWithParse(this::setRight, parentSize.getX(), components[0]);
                    setWithParse(this::setBottom, parentSize.getY(), components[0]);
                }
                else if (components.length == 2)
                {
                    setWithParse(this::setLeft, parentSize.getX(), components[0]);
                    setWithParse(this::setTop, parentSize.getY(), components[1]);
                    setWithParse(this::setRight, parentSize.getX(), components[0]);
                    setWithParse(this::setBottom, parentSize.getY(), components[1]);
                }
                else
                {
                    Log.getLogger().error("Cannot interpret special axis distance input: " + string);
                    setLeft(Optional.empty());
                    setTop(Optional.empty());
                    setRight(Optional.empty());
                    setBottom(Optional.empty());
                }
            }
            else
            {
                final String[] components = string.trim().split(",");
                if (components.length == 0 || components.length > 4)
                {
                    setLeft(Optional.empty());
                    setTop(Optional.empty());
                    setRight(Optional.empty());
                    setBottom(Optional.empty());
                }
                else
                {
                    setWithParse(this::setLeft, parentSize.getX(), components[0]);

                    if (components.length > 1)
                    {
                        setWithParse(this::setTop, parentSize.getY(), components[1]);
                    }

                    if (components.length > 2)
                    {
                        setWithParse(this::setTop, parentSize.getX(), components[2]);
                    }

                    if (components.length > 3)
                    {
                        setWithParse(this::setTop, parentSize.getY(), components[3]);
                    }
                }
            }
        }
    }

    public AxisDistanceBuilder setLeft(final Optional<Double> left)
    {
        this.left = left;
        return this;
    }

    public AxisDistanceBuilder setTop(final Optional<Double> top)
    {
        this.top = top;
        return this;
    }

    public AxisDistanceBuilder setRight(final Optional<Double> right)
    {
        this.right = right;
        return this;
    }

    public AxisDistanceBuilder setBottom(final Optional<Double> bottom)
    {
        this.bottom = bottom;
        return this;
    }

    private void setWithParse(@NotNull final Consumer<Optional<Double>> setter, @NotNull final double parentSize, @NotNull final String component)
    {
        if (component.trim().isEmpty())
        {
            setter.accept(Optional.empty());
        }
        else
        {
            if (!component.endsWith("%"))
            {
                try
                {
                    setter.accept(Optional.of(Double.parseDouble(component)));
                }
                catch (Exception ex)
                {
                    Log.getLogger().error("Failed to parse distance for axis from: " + component);
                    setter.accept(Optional.empty());
                }
            }
            else
            {
                final String numericalPercentage = component.replace("%", "");
                try
                {
                    setter.accept(Optional.of(parentSize * Double.parseDouble(numericalPercentage)));
                }
                catch (Exception ex)
                {
                    Log.getLogger().error("Failed to parse distance for axis from: " + component);
                    setter.accept(Optional.empty());
                }
            }
        }
    }

    public AxisDistance create()
    {
        return new AxisDistance(left, top, right, bottom);
    }
}