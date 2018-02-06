package com.minecolonies.blockout.context;

import com.minecolonies.blockout.context.core.IContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PositionContext implements IContext
{

    private int dimensionId;

    private int x;
    private int y;
    private int z;

    public PositionContext()
    {
    }

    public PositionContext(final int dimensionId, final int x, final int y, final int z) {
        this.dimensionId = dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionContext(@NotNull final World world, @NotNull final BlockPos pos)
    {
        this.dimensionId = world.provider.getDimension();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
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

    @Override
    public int compareTo(@NotNull final IContext o)
    {
        return equals(o) ? 0 : -1;
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
    public int hashCode()
    {
        int result = getDimensionId();
        result = 31 * result + getX();
        result = 31 * result + getY();
        result = 31 * result + getZ();
        return result;
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
}
