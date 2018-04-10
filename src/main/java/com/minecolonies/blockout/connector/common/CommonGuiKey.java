package com.minecolonies.blockout.connector.common;

import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.connector.core.IGuiDefinitionLoader;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommonGuiKey implements IGuiKey
{
    @NotNull
    private final IGuiDefinitionLoader loader;

    @NotNull
    private final transient IBlockOutGuiConstructionData constructionData;

    @NotNull
    private final IContext context;

    private CommonGuiKey()
    {
        this.loader = null;
        this.constructionData = new BlockOutGuiConstructionDataBuilder().build();
        this.context = null;
    }

    public CommonGuiKey(@NotNull final IGuiDefinitionLoader loader, @NotNull final IBlockOutGuiConstructionData constructionData, @NotNull final IContext context)
    {
        this.loader = loader;
        this.constructionData = constructionData;
        this.context = context;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getGuiDefinitionLoader(), getConstructionData(), getGuiContext());
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
        return Objects.equals(getGuiDefinitionLoader(), that.getGuiDefinitionLoader()) &&
                 Objects.equals(getConstructionData(), that.getConstructionData()) &&
                 Objects.equals(getGuiContext(), that.getGuiContext());
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
    public IContext getGuiContext()
    {
        return context;
    }
}
