package com.minecolonies.blockout.connector.core.inventory.builder;

import com.minecolonies.blockout.connector.core.inventory.IItemHandlerManager;
import com.minecolonies.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IItemHandlerManagerBuilder
{

    @NotNull
    IItemHandlerManagerBuilder withProvider(@NotNull final IItemHandlerProvider provider);

    @NotNull
    IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @NotNull final
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
    default IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final Entity entity, @Nullable final EnumFacing facing
    )
    {
        return this.withEntityBasedProvider(id, entity.world.provider.getDimension(), entity.getEntityId(), facing);
    }

    @NotNull
    IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int entityId, @Nullable final EnumFacing facing
    );

    @NotNull
    IItemHandlerManager build();
}
