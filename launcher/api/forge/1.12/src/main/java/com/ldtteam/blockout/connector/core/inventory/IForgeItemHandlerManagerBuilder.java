package com.ldtteam.blockout.connector.core.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IForgeItemHandlerManagerBuilder
{
    @NotNull
    IForgeItemHandlerManagerBuilder withProvider(@NotNull final IForgeItemHandlerProvider provider);

    @NotNull
    default IForgeItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final TileEntity tileEntity, @Nullable EnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, tileEntity.getWorld().provider.getDimension(), tileEntity.getPos(), facing);
    }

    @NotNull
    default IForgeItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final BlockPos blockPos, @Nullable EnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, dimId, blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    IForgeItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @Nullable final
    EnumFacing facing);

    @NotNull
    default IForgeItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final World world, @NotNull final BlockPos blockPos, @Nullable EnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, world.provider.getDimension(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    default IForgeItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final Entity entity, @Nullable final EnumFacing facing
    )
    {
        return this.withEntityBasedProvider(id, entity.getEntityWorld().provider.getDimension(), entity.getUniqueID(), facing);
    }

    @NotNull
    IForgeItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final EnumFacing facing
    );

    @NotNull
    IForgeItemHandlerManagerBuilder withWrapped(
      @NotNull final ResourceLocation id, @NotNull final ResourceLocation wrappedId, @NotNull final int minSlot, @NotNull final int maxSlotExcluding
    );

    @NotNull
    default IForgeItemHandlerManagerBuilder copyFrom(@NotNull final IForgeItemHandlerManagerBuilder builder)
    {
        return copyFrom(builder.build());
    }

    @NotNull
    IForgeItemHandlerManagerBuilder copyFrom(@NotNull final IForgeItemHandlerManager manager);

    @NotNull
    IForgeItemHandlerManager build();
}
