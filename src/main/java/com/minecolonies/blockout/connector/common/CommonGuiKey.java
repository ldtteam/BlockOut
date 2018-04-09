package com.minecolonies.blockout.connector.common;

import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.context.core.IContext;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CommonGuiKey implements IGuiKey
{

    @NotNull
    private final String definition;

    @NotNull
    private final IContext context;

    public CommonGuiKey(@NotNull final ResourceLocation guiDefinition, @NotNull final IContext context)
    {
        this.domain = guiDefinition.getResourceDomain();
        this.path = guiDefinition.getResourcePath();
        this.context = context;
    }

    @NotNull
    @Override
    public ResourceLocation getGuiDefinition()
    {
        return new ResourceLocation(domain, path);
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionData getConstructionData()
    {
        return null;
    }

    @NotNull
    @Override
    public IContext getGuiContext()
    {
        return context;
    }

    @Override
    public int hashCode()
    {
        int result = domain.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + context.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof CommonGuiKey))
        {
            return false;
        }

        final CommonGuiKey that = (CommonGuiKey) o;

        if (!domain.equals(that.domain))
        {
            return false;
        }
        if (!path.equals(that.path))
        {
            return false;
        }
        return context.equals(that.context);
    }

    @Override
    public int compareTo(@NotNull final IGuiKey o)
    {
        return this.equals(o) ? 0 : -1;
    }
}
