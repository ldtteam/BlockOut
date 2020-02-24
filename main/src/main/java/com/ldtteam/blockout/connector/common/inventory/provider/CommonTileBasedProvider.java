package com.ldtteam.blockout.connector.common.inventory.provider;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonTileBasedProvider implements IItemHandlerProvider
{

    private static final long serialVersionUID = -2390884778025679771L;

    @NotNull
    private final String id;

    @NotNull
    private final int dimId;
    @NotNull
    private final int x;
    @NotNull
    private final int y;
    @NotNull
    private final int z;

    @Nullable
    private final Direction facing;

    public CommonTileBasedProvider(
      @NotNull final ResourceLocation id,
      @NotNull final int dimId,
      @NotNull final int x,
      @NotNull final int y,
      @NotNull final int z,
      @Nullable final Direction facing)
    {
        this.id = id.toString();
        this.dimId = dimId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
    }

    @Override
    public int hashCode()
    {
        int result = getId().hashCode();
        result = 31 * result + dimId;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (facing != null ? facing.hashCode() : 0);
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

        final CommonTileBasedProvider that = (CommonTileBasedProvider) o;

        if (dimId != that.dimId)
        {
            return false;
        }
        if (x != that.x)
        {
            return false;
        }
        if (y != that.y)
        {
            return false;
        }
        if (z != that.z)
        {
            return false;
        }
        if (!getId().equals(that.getId()))
        {
            return false;
        }
        return facing == that.facing;
    }

    @NotNull
    @Override
    public ResourceLocation getId()
    {
        return new ResourceLocation(id);
    }

    @Nullable
    @Override
    public IItemHandler get(@NotNull final IItemHandlerManager manager)
    {
        final IBlockReader blockAccess = ProxyHolder.getInstance().getDimensionFromDimensionId(dimId);
        final TileEntity tileEntity = blockAccess.getTileEntity(new BlockPos(x,y,z));

        if (tileEntity == null)
        {
            return null;
        }

        return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).orElse(null);
    }
}
