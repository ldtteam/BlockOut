package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.core.*;
import com.ldtteam.blockout.connector.core.builder.IForgeGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManagerBuilder;
import com.ldtteam.blockout.connector.core.inventory.builder.IItemHandlerManagerBuilder;
import com.ldtteam.blockout.context.core.IContext;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.dimension.Dimension;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.math.coordinate.block.BlockCoordinate;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntFunction;

class ForgeGuiKeyBuilder implements IForgeGuiKeyBuilder
{

    private final IGuiKeyBuilder wrappedBuilder;

    ForgeGuiKeyBuilder(final IGuiKeyBuilder wrappedBuilder) {this.wrappedBuilder = wrappedBuilder;}

    @NotNull
    @Override
    public IForgeGuiKeyBuilder ofWebResource(@NotNull final URL url)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.ofWebResource(url));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder ofFile(@NotNull final ResourceLocation location)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.ofFile(Identifier.fromForge(location)));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder ofClass(@NotNull final Class<?> clazz)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.ofClass(clazz));
    }

    @Override
    public IForgeGuiKeyBuilder ofDefinition(@NotNull final IGuiDefinitionLoader definitionLoader)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.ofDefinition(definitionLoader));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder usingData(@NotNull final Consumer<IBlockOutGuiConstructionDataBuilder>... builderConsumer)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.usingData(builderConsumer));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder usingData(@NotNull final IBlockOutGuiConstructionData data)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.usingData(data));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder withItemHandlerManager(@NotNull final IForgeItemHandlerManager manager)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.withItemHandlerManager(new CustomForgeItemHandlerManager(manager)));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder withItemHandlerManager(@NotNull final Consumer<IForgeItemHandlerManagerBuilder>... configurer)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.withItemHandlerManager(
          Arrays.stream(configurer)
            .map(configLogic -> (Consumer<IItemHandlerManagerBuilder>) iItemHandlerManagerBuilder -> configLogic.accept(new ForgeItemHandlerManagerBuilder(
              iItemHandlerManagerBuilder)))
            .toArray((IntFunction<Consumer<IItemHandlerManagerBuilder>[]>) Consumer[]::new)
        ));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder forEntity(@NotNull final UUID entityId)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.forEntity(entityId));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder forEntity(@NotNull final Entity entity)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.forEntity(entity.getUniqueID()));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder forPosition(@NotNull final int dimensionId, @NotNull final int x, @NotNull final int y, @NotNull final int z)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.forPosition(dimensionId, x, y, z));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder forPosition(@NotNull final World world, @NotNull final BlockPos blockPos)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.forPosition(Dimension.fromForge(world), BlockCoordinate.fromForge(blockPos)));
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder forClientSideOnly()
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.forClientSideOnly());
    }

    @NotNull
    @Override
    public IForgeGuiKeyBuilder forContext(@NotNull final IContext context)
    {
        return new ForgeGuiKeyBuilder(wrappedBuilder.forContext(context));
    }

    @NotNull
    @Override
    public IForgeGuiKey build()
    {
        return new ForgeGuiKey(wrappedBuilder.build());
    }
}
