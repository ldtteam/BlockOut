package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManagerBuilder;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerProvider;
import com.ldtteam.blockout.connector.core.inventory.builder.IItemHandlerManagerBuilder;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.facing.Facing;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

class ForgeItemHandlerManagerBuilder implements IForgeItemHandlerManagerBuilder
{
    private final IItemHandlerManagerBuilder wrappedBuilder;

    ForgeItemHandlerManagerBuilder(final IItemHandlerManagerBuilder wrappedBuilder) {this.wrappedBuilder = wrappedBuilder;}

    @NotNull
    @Override
    public IForgeItemHandlerManagerBuilder withProvider(@NotNull final IForgeItemHandlerProvider provider)
    {
        return new ForgeItemHandlerManagerBuilder(wrappedBuilder.withProvider(new CustomForgeItemHandlerProvider(provider)));
    }

    @NotNull
    @Override
    public IForgeItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @Nullable final EnumFacing facing)
    {
        return new ForgeItemHandlerManagerBuilder(wrappedBuilder.withTileBasedProvider(
          Identifier.fromForge(id),
          dimId,
          x,
          y,
          z,
          Facing.fromForge(facing)
        ));
    }

    @NotNull
    @Override
    public IForgeItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final EnumFacing facing)
    {
        return new ForgeItemHandlerManagerBuilder(wrappedBuilder.withEntityBasedProvider(
          Identifier.fromForge(id),
          dimId,
          entityId,
          Facing.fromForge(facing)
        ));
    }

    @NotNull
    @Override
    public IForgeItemHandlerManagerBuilder withWrapped(
      @NotNull final ResourceLocation id, @NotNull final ResourceLocation wrappedId, @NotNull final int minSlot, @NotNull final int maxSlotExcluding)
    {
        return new ForgeItemHandlerManagerBuilder(wrappedBuilder.withWrapped(
          Identifier.fromForge(id),
          Identifier.fromForge(id),
          minSlot,
          maxSlotExcluding
        ));
    }

    @NotNull
    @Override
    public IForgeItemHandlerManagerBuilder copyFrom(@NotNull final IForgeItemHandlerManager manager)
    {
        return new ForgeItemHandlerManagerBuilder(wrappedBuilder.copyFrom(
          new CustomForgeItemHandlerManager(manager)
        ));
    }

    @NotNull
    @Override
    public IForgeItemHandlerManager build()
    {
        return new ForgeItemHandlerManager(wrappedBuilder.build());
    }
}
