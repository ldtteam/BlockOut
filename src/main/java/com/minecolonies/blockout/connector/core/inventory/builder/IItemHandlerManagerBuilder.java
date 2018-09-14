package com.minecolonies.blockout.connector.core.inventory.builder;

import com.minecolonies.blockout.connector.core.inventory.IItemHandlerManager;
import com.minecolonies.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IItemHandlerManagerBuilder
{

    @NotNull
    IItemHandlerManagerBuilder withProvider(@NotNull final IItemHandlerProvider provider);

    @NotNull
    IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @Nullable final
    EnumFacing facing);

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final TileEntity tileEntity, @Nullable EnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, tileEntity.getWorld().provider.getDimension(), tileEntity.getPos(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final BlockPos blockPos, @Nullable EnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, dimId, blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final World world, @NotNull final BlockPos blockPos, @Nullable EnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, world.provider.getDimension(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final Entity entity, @Nullable final EnumFacing facing
    )
    {
        return this.withEntityBasedProvider(id, entity.world.provider.getDimension(), entity.getPersistentID(), facing);
    }

    @NotNull
    IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final EnumFacing facing
    );

    @NotNull
    IItemHandlerManagerBuilder withWrapped(
      @NotNull final ResourceLocation id, @NotNull final ResourceLocation wrappedId, @NotNull final int minSlot, @NotNull final int maxSlotExcluding
    );

    @NotNull
    IItemHandlerManager build();
}
