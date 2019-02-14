package com.ldtteam.blockout.connector.core.inventory.builder;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.minelaunch.entity.IEntity;
import com.ldtteam.minelaunch.tileentity.IBlockEntity;
import com.ldtteam.minelaunch.util.IEnumFacing;
import com.ldtteam.minelaunch.util.IIdentifier;
import com.ldtteam.minelaunch.util.math.ICoordinate;
import com.ldtteam.minelaunch.world.IDimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IItemHandlerManagerBuilder
{

    @NotNull
    IItemHandlerManagerBuilder withProvider(@NotNull final IItemHandlerProvider provider);

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final IIdentifier id, @NotNull final IBlockEntity tileEntity, @Nullable IEnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, tileEntity.getDimension().getId(), tileEntity.getPosition(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final IIdentifier id, @NotNull final int dimId, @NotNull final ICoordinate blockPos, @Nullable IEnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, dimId, blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final IIdentifier id, @NotNull final int dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @Nullable final
    IEnumFacing facing);

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final IIdentifier id, @NotNull final IDimension world, @NotNull final ICoordinate blockPos, @Nullable IEnumFacing facing
    )
    {
        return this.withTileBasedProvider(id, world.getId(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final IIdentifier id, @NotNull final IEntity entity, @Nullable final IEnumFacing facing
    )
    {
        return this.withEntityBasedProvider(id, entity.getDimension().getId(), entity.getId(), facing);
    }

    @NotNull
    IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final IIdentifier id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final IEnumFacing facing
    );

    @NotNull
    IItemHandlerManagerBuilder withWrapped(
      @NotNull final IIdentifier id, @NotNull final IIdentifier wrappedId, @NotNull final int minSlot, @NotNull final int maxSlotExcluding
    );

    @NotNull
    default IItemHandlerManagerBuilder copyFrom(@NotNull final IItemHandlerManagerBuilder builder)
    {
        return copyFrom(builder.build());
    }

    @NotNull
    IItemHandlerManagerBuilder copyFrom(@NotNull final IItemHandlerManager manager);

    @NotNull
    IItemHandlerManager build();
}
