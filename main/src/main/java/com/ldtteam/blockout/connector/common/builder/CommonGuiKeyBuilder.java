package com.ldtteam.blockout.connector.common.builder;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.common.CommonGuiKey;
import com.ldtteam.blockout.connector.common.definition.loader.CommonClassBasedDefinitionLoader;
import com.ldtteam.blockout.connector.common.definition.loader.CommonIIdentifierBasedGuiDefinitionLoader;
import com.ldtteam.blockout.connector.common.definition.loader.CommonWebFileBasedGuiDefinitionLoader;
import com.ldtteam.blockout.connector.common.inventory.builder.CommonItemHandlerManagerBuilder;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.builder.IItemHandlerManagerBuilder;
import com.ldtteam.blockout.context.ClientSideOnlyContext;
import com.ldtteam.blockout.context.EntityContext;
import com.ldtteam.blockout.context.PositionContext;
import com.ldtteam.blockout.context.core.IContext;
import com.ldtteam.jvoxelizer.dimension.IDimension;
import com.ldtteam.jvoxelizer.entity.IEntity;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.jvoxelizer.util.math.coordinate.block.IBlockCoordinate;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class CommonGuiKeyBuilder implements IGuiKeyBuilder
{
    @NotNull
    private IGuiDefinitionLoader                guiDefinitionLoader;
    @NotNull
    private IBlockOutGuiConstructionDataBuilder blockOutGuiConstructionDataBuilder = new BlockOutGuiConstructionDataBuilder();
    @NotNull
    private IContext                            context;
    @NotNull
    private IItemHandlerManagerBuilder          iItemHandlerManagerBuilder         = new CommonItemHandlerManagerBuilder();

    @NotNull
    @Override
    public IGuiKeyBuilder ofWebResource(@NotNull final URL url)
    {
        this.guiDefinitionLoader = new CommonWebFileBasedGuiDefinitionLoader(url);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder ofFile(@NotNull final IIdentifier location)
    {
        this.guiDefinitionLoader = new CommonIIdentifierBasedGuiDefinitionLoader(location);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder ofClass(@NotNull final Class<?> clazz)
    {
        this.guiDefinitionLoader = new CommonClassBasedDefinitionLoader(clazz);
        return this;
    }

    @Override
    public IGuiKeyBuilder ofDefinition(@NotNull final IGuiDefinitionLoader definitionLoader)
    {
        this.guiDefinitionLoader = definitionLoader;
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder usingData(@NotNull final Consumer<IBlockOutGuiConstructionDataBuilder>... builderConsumer)
    {
        Arrays.stream(builderConsumer).forEach(consumer -> consumer.accept(blockOutGuiConstructionDataBuilder));
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder usingData(@NotNull final IBlockOutGuiConstructionData data)
    {
        this.blockOutGuiConstructionDataBuilder.copyFrom(data);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder withItemHandlerManager(@NotNull final IItemHandlerManager manager)
    {
        this.iItemHandlerManagerBuilder.copyFrom(manager);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder withItemHandlerManager(@NotNull final Consumer<IItemHandlerManagerBuilder>... configurer)
    {
        final CommonItemHandlerManagerBuilder builder = new CommonItemHandlerManagerBuilder();
        Arrays.stream(configurer).forEach(consumer -> consumer.accept(builder));

        return withItemHandlerManager(builder.build());
    }

    @NotNull
    @Override
    public IGuiKeyBuilder forEntity(@NotNull final UUID entityId)
    {
        this.context = new EntityContext(entityId);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder forEntity(@NotNull final IEntity entity)
    {
        this.context = new EntityContext(entity);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder forPosition(@NotNull final int dimensionId, @NotNull final int x, @NotNull final int y, @NotNull final int z)
    {
        this.context = new PositionContext(dimensionId, x, y, z);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder forPosition(@NotNull final IDimension world, @NotNull final IBlockCoordinate blockPos)
    {
        this.context = new PositionContext(world, blockPos);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder forClientSideOnly()
    {
        this.context = new ClientSideOnlyContext();
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder forContext(@NotNull final IContext context)
    {
        this.context = context;
        return this;
    }

    @NotNull
    @Override
    public IGuiKey build()
    {
        return new CommonGuiKey(
          guiDefinitionLoader,
          blockOutGuiConstructionDataBuilder.build(),
          context,
          iItemHandlerManagerBuilder.build()
        );
    }
}
