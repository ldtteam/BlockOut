package com.ldtteam.blockout.connector.common;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommonGuiKey implements IGuiKey
{
    private static final long serialVersionUID = 8752574389047506304L;

    @NotNull
    private final Object source;

    @NotNull
    private final transient IBlockOutGuiConstructionData constructionData;

    @NotNull
    private final IContext context;

    @NotNull
    private final IItemHandlerManager itemHandlerManager;

    private CommonGuiKey()
    {
        this.source = null;
        this.constructionData = new BlockOutGuiConstructionDataBuilder().build();
        this.context = null;
        this.itemHandlerManager = null;
    }

    public CommonGuiKey(
      @NotNull final Object source,
      @NotNull final IBlockOutGuiConstructionData constructionData,
      @NotNull final IContext context,
      @NotNull final IItemHandlerManager itemHandlerManager)
    {
        this.source = source;
        this.constructionData = constructionData;
        this.context = context;
        this.itemHandlerManager = itemHandlerManager;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CommonGuiKey that = (CommonGuiKey) o;

        if (!Objects.equals(source, that.source)) return false;
        if (!getConstructionData().equals(that.getConstructionData())) return false;
        if (!context.equals(that.context)) return false;
        return getItemHandlerManager().equals(that.getItemHandlerManager());
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + getConstructionData().hashCode();
        result = 31 * result + context.hashCode();
        result = 31 * result + getItemHandlerManager().hashCode();
        return result;
    }

    @NotNull
    @Override
    public Object getDataSource()
    {
        return source;
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
