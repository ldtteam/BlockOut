package com.minecolonies.blockout.connector.core.builder;

import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.connector.core.IGuiKey;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

public interface IGuiKeyBuilder
{

    @NotNull
    IGuiKeyBuilder ofWebResource(@NotNull final URL url);

    @NotNull
    IGuiKeyBuilder ofFile(@NotNull final ResourceLocation location);

    @NotNull
    IGuiKeyBuilder ofClass(@NotNull final Class<?> clazz);

    @NotNull
    default IGuiKeyBuilder usingDefaultData()
    {
        return usingData((builder) -> {
            //NOOP. Produces default builder.
        });
    }

    @NotNull
    IGuiKeyBuilder usingData(@NotNull final Consumer<IBlockOutGuiConstructionDataBuilder> builderConsumer);

    @NotNull
    IGuiKeyBuilder forEntity(@NotNull final UUID entityId);

    @NotNull
    IGuiKeyBuilder forEntity(@NotNull final Entity entity);

    @NotNull
    IGuiKeyBuilder forPosition(@NotNull final int dimensionId, @NotNull final int x, @NotNull final int y, @NotNull final int z);

    @NotNull
    IGuiKeyBuilder forPosition(@NotNull final World world, @NotNull final BlockPos blockPos);

    @NotNull
    IGuiKey build();
}
