package com.ldtteam.blockout.connector.common.inventory.builder;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.connector.common.inventory.CommonItemHandlerManager;
import com.ldtteam.blockout.connector.common.inventory.provider.CommonEntityBasedProvider;
import com.ldtteam.blockout.connector.common.inventory.provider.CommonRangedBasedProvider;
import com.ldtteam.blockout.connector.common.inventory.provider.CommonTileBasedProvider;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.blockout.connector.core.inventory.builder.IItemHandlerManagerBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CommonItemHandlerManagerBuilder implements IItemHandlerManagerBuilder
{

    @NotNull
    private final List<IItemHandlerProvider> providerList = Lists.newArrayList();

    @NotNull
    @Override
    public IItemHandlerManagerBuilder withProvider(@NotNull final IItemHandlerProvider provider)
    {
        providerList.add(provider);
        return this;
    }

    @NotNull
    @Override
    public IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @Nullable final EnumFacing facing)
    {
        return withProvider(new CommonTileBasedProvider(id, dimId, x, y, z, facing));
    }

    @NotNull
    @Override
    public IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final EnumFacing facing)
    {
        return withProvider(new CommonEntityBasedProvider(id, dimId, entityId, facing));
    }

    @NotNull
    @Override
    public IItemHandlerManagerBuilder withWrapped(
      @NotNull final ResourceLocation id, @NotNull final ResourceLocation wrappedId, @NotNull final int minSlot, @NotNull final int maxSlotExcluding)
    {
        return withProvider(new CommonRangedBasedProvider(id.toString(), wrappedId.toString(), minSlot, maxSlotExcluding));
    }

    @NotNull
    @Override
    public IItemHandlerManagerBuilder copyFrom(@NotNull final IItemHandlerManager manager)
    {
        this.providerList.addAll(manager.getAllProviders());
        return this;
    }

    @NotNull
    @Override
    public IItemHandlerManager build()
    {
        return new CommonItemHandlerManager(providerList);
    }
}
