package com.ldtteam.blockout.context;

import com.ldtteam.blockout.context.core.IContext;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PositionContext implements IContext
{

    private static final long serialVersionUID = -5202348208706760620L;

    private int dimensionId;

    private int x;
    private int y;
    private int z;

    public PositionContext()
    {
    }

    public PositionContext(final int dimensionId, final int x, final int y, final int z)
    {
        this.dimensionId = dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionContext(@NotNull final World world, @NotNull final BlockPos pos)
    {
        this.dimensionId = 0;//world.getDimension().getType().getId();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    @Override
    public int hashCode()
    {
        int result = getDimensionId();
        result = 31 * result + getX();
        result = 31 * result + getY();
        result = 31 * result + getZ();
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof PositionContext))
        {
            return false;
        }

        final PositionContext that = (PositionContext) o;

        if (getDimensionId() != that.getDimensionId())
        {
            return false;
        }
        if (getX() != that.getX())
        {
            return false;
        }
        if (getY() != that.getY())
        {
            return false;
        }
        return getZ() == that.getZ();
    }

    @Override
    public String toString()
    {
        return "PositionContext{" +
                 "dimensionId=" + dimensionId +
                 ", x=" + x +
                 ", y=" + y +
                 ", z=" + z +
                 '}';
    }

    public int getDimensionId()
    {
        return dimensionId;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }
}
