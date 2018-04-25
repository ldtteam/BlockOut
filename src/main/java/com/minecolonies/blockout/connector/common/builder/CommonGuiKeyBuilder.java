package com.minecolonies.blockout.connector.common.builder;

import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.connector.common.CommonGuiKey;
import com.minecolonies.blockout.connector.common.definition.loader.CommonClassBasedDefinitionLoader;
import com.minecolonies.blockout.connector.common.definition.loader.CommonResourceLocationBasedGuiDefinitionLoader;
import com.minecolonies.blockout.connector.common.definition.loader.CommonWebFileBasedGuiDefinitionLoader;
import com.minecolonies.blockout.connector.common.inventory.builder.CommonItemHandlerManagerBuilder;
import com.minecolonies.blockout.connector.core.IGuiDefinitionLoader;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.core.builder.IGuiKeyBuilder;
import com.minecolonies.blockout.connector.core.inventory.IItemHandlerManager;
import com.minecolonies.blockout.connector.core.inventory.builder.IItemHandlerManagerBuilder;
import com.minecolonies.blockout.context.EntityContext;
import com.minecolonies.blockout.context.PositionContext;
import com.minecolonies.blockout.context.core.IContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

public class CommonGuiKeyBuilder implements IGuiKeyBuilder
{
    @NotNull
    private IGuiDefinitionLoader         guiDefinitionLoader;
    @NotNull
    private IBlockOutGuiConstructionData constructionData;
    @NotNull
    private IContext                     context;
    @NotNull
    private IItemHandlerManager          itemHandlerManager;

    @NotNull
    @Override
    public IGuiKeyBuilder ofWebResource(@NotNull final URL url)
    {
        this.guiDefinitionLoader = new CommonWebFileBasedGuiDefinitionLoader(url);
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder ofFile(@NotNull final ResourceLocation location)
    {
        this.guiDefinitionLoader = new CommonResourceLocationBasedGuiDefinitionLoader(location);
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
    public IGuiKeyBuilder usingData(@NotNull final Consumer<IBlockOutGuiConstructionDataBuilder> builderConsumer)
    {
        final BlockOutGuiConstructionDataBuilder builder = new BlockOutGuiConstructionDataBuilder();
        builderConsumer.accept(builder);

        this.constructionData = builder.build();

        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder usingData(@NotNull final IBlockOutGuiConstructionData data)
    {
        this.constructionData = data;
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder withItemHandlerManager(@NotNull final IItemHandlerManager manager)
    {
        this.itemHandlerManager = manager;
        return this;
    }

    @NotNull
    @Override
    public IGuiKeyBuilder withItemHandlerManager(@NotNull final Consumer<IItemHandlerManagerBuilder> configurer)
    {
        final CommonItemHandlerManagerBuilder builder = new CommonItemHandlerManagerBuilder();
        configurer.accept(builder);

        return withItemHandlerManager(builder.build());
    }

    @NotNull
    @Override
    public IGuiKeyBuilder withDefaultItemHandlerManager()
    {
        this.itemHandlerManager = new CommonItemHandlerManagerBuilder().build();
        return this;
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
    public IGuiKeyBuilder forEntity(@NotNull final Entity entity)
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
    public IGuiKeyBuilder forPosition(@NotNull final World world, @NotNull final BlockPos blockPos)
    {
        this.context = new PositionContext(world, blockPos);
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
        return new CommonGuiKey(guiDefinitionLoader, constructionData, context, itemHandlerManager);
    }
}
