package com.ldtteam.blockout.connector.core.builder;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.core.IForgeGuiKey;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManagerBuilder;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.context.core.IContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

public interface IForgeGuiKeyBuilder
{
    @NotNull
    IForgeGuiKeyBuilder ofWebResource(@NotNull final URL url);

    @NotNull
    IForgeGuiKeyBuilder ofFile(@NotNull final ResourceLocation location);

    @NotNull
    IForgeGuiKeyBuilder ofClass(@NotNull final Class<?> clazz);

    IForgeGuiKeyBuilder ofDefinition(@NotNull final IGuiDefinitionLoader definitionLoader);

    @NotNull
    default IForgeGuiKeyBuilder usingDefaultData()
    {
        //Calling with no args causes the use of the default data.
        return usingData();
    }

    @NotNull
    IForgeGuiKeyBuilder usingData(@NotNull final Consumer<IBlockOutGuiConstructionDataBuilder>... builderConsumer);

    @NotNull
    IForgeGuiKeyBuilder usingData(@NotNull final IBlockOutGuiConstructionData data);

    @NotNull
    IForgeGuiKeyBuilder withItemHandlerManager(@NotNull final IForgeItemHandlerManager manager);

    @NotNull
    default IForgeGuiKeyBuilder withDefaultItemHandlerManager()
    {
        //Calling with no args causes the default manager to be used.
        return withItemHandlerManager();
    }

    @NotNull
    IForgeGuiKeyBuilder withItemHandlerManager(@NotNull final Consumer<IForgeItemHandlerManagerBuilder>... configurer);

    @NotNull
    IForgeGuiKeyBuilder forEntity(@NotNull final UUID entityId);

    @NotNull
    IForgeGuiKeyBuilder forEntity(@NotNull final Entity entity);

    @NotNull
    IForgeGuiKeyBuilder forPosition(@NotNull final int dimensionId, @NotNull final int x, @NotNull final int y, @NotNull final int z);

    @NotNull
    IForgeGuiKeyBuilder forPosition(@NotNull final World world, @NotNull final BlockPos blockPos);

    @NotNull
    IForgeGuiKeyBuilder forClientSideOnly();

    @NotNull
    IForgeGuiKeyBuilder forContext(@NotNull final IContext context);

    @NotNull
    IForgeGuiKey build();
}
