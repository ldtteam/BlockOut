package com.minecolonies.blockout.connector.common;

import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.connector.core.IGuiDefinitionLoader;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.core.inventory.IItemHandlerManager;
import com.minecolonies.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

public class CommonGuiKey implements IGuiKey
{
    @NotNull
    private final IGuiDefinitionLoader loader;

    @NotNull
    private final transient IBlockOutGuiConstructionData constructionData;

    @NotNull
    private final IContext context;

    @NotNull
    private final IItemHandlerManager itemHandlerManager;

    private CommonGuiKey()
    {
        this.loader = null;
        this.constructionData = new BlockOutGuiConstructionDataBuilder().build();
        this.context = null;
        this.itemHandlerManager = null;
    }

    public CommonGuiKey(
      @NotNull final IGuiDefinitionLoader loader,
      @NotNull final IBlockOutGuiConstructionData constructionData,
      @NotNull final IContext context,
      @NotNull final IItemHandlerManager itemHandlerManager)
    {
        this.loader = loader;
        this.constructionData = constructionData;
        this.context = context;
        this.itemHandlerManager = itemHandlerManager;
    }

    @Override
    public int hashCode()
    {
        int result = loader.hashCode();
        result = 31 * result + getConstructionData().hashCode();
        result = 31 * result + context.hashCode();
        result = 31 * result + getItemHandlerManager().hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final CommonGuiKey that = (CommonGuiKey) o;

        if (!loader.equals(that.loader))
        {
            return false;
        }
        if (!getConstructionData().equals(that.getConstructionData()))
        {
            return false;
        }
        if (!context.equals(that.context))
        {
            return false;
        }
        return getItemHandlerManager().equals(that.getItemHandlerManager());
    }

    @NotNull
    @Override
    public IGuiDefinitionLoader getGuiDefinitionLoader()
    {
        return loader;
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionData getConstructionData()
    {
        return constructionData;
    }

    @NotNull
    @Override
    public IItemHandlerManager getItemHandlerManager()
    {
        return itemHandlerManager;
    }

    @NotNull
    @Override
    public IContext getGuiContext()
    {
        return context;
    }
}
