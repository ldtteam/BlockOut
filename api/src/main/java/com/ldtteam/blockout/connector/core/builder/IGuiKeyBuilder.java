package com.ldtteam.blockout.connector.core.builder;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.builder.IItemHandlerManagerBuilder;
import com.ldtteam.blockout.context.core.IContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
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

    IGuiKeyBuilder ofDefinition(@NotNull final IGuiDefinitionLoader definitionLoader);

    @NotNull
    default IGuiKeyBuilder usingDefaultData()
    {
        //Calling with no args causes the use of the default data.
        return usingData();
    }

    @NotNull
    IGuiKeyBuilder usingData(@NotNull final Consumer<IBlockOutGuiConstructionDataBuilder>... builderConsumer);

    @NotNull
    IGuiKeyBuilder usingData(@NotNull final IBlockOutGuiConstructionData data);

    @NotNull
    IGuiKeyBuilder withItemHandlerManager(@NotNull final IItemHandlerManager manager);

    @NotNull
    default IGuiKeyBuilder withDefaultItemHandlerManager()
    {
        //Calling with no args causes the default manager to be used.
        return withItemHandlerManager();
    }

    @NotNull
    IGuiKeyBuilder withItemHandlerManager(@NotNull final Consumer<IItemHandlerManagerBuilder>... configurer);

    @NotNull
    IGuiKeyBuilder forEntity(@NotNull final UUID entityId);

    @NotNull
    IGuiKeyBuilder forEntity(@NotNull final Entity entity);

    @NotNull
    IGuiKeyBuilder forPosition(@NotNull final int dimensionId, @NotNull final int x, @NotNull final int y, @NotNull final int z);

    @NotNull
    IGuiKeyBuilder forPosition(@NotNull final World world, @NotNull final BlockPos blockPos);

    @NotNull
    IGuiKeyBuilder forClientSideOnly();

    @NotNull
    IGuiKeyBuilder forContext(@NotNull final IContext context);

    @NotNull
    IGuiKey build();
}
