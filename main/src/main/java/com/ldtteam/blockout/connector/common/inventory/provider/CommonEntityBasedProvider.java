package com.ldtteam.blockout.connector.common.inventory.provider;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CommonEntityBasedProvider implements IItemHandlerProvider
{
    @NotNull
    private final String id;

    @NotNull
    private final int     dimId;
    @NotNull
    private final UUID    entityId;
    @Nullable
    private final IFacing facing;

    public CommonEntityBasedProvider(@NotNull final IIdentifier id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final IFacing facing)
    {
        this.id = id.toString();
        this.dimId = dimId;
        this.entityId = entityId;
        this.facing = facing;
    }

    @Override
    public int hashCode()
    {
        int result = getId().hashCode();
        result = 31 * result + dimId;
        result = 31 * result + entityId.hashCode();
        result = 31 * result + (facing != null ? facing.hashCode() : 0);
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

        final CommonEntityBasedProvider that = (CommonEntityBasedProvider) o;

        if (dimId != that.dimId)
        {
            return false;
        }
        if (!getId().equals(that.getId()))
        {
            return false;
        }
        if (!entityId.equals(that.entityId))
        {
            return false;
        }
        return facing == that.facing;
    }

    /**
     * The id of the provider.
     *
     * @return The id.
     */
    @NotNull
    @Override
    public IIdentifier getId()
    {
        return IIdentifier.create(id);
    }

    /**
     *
     */
    @Nullable
    @Override
    public IItemHandler get(@NotNull final IItemHandlerManager manager)
    {
        final IDimension<?> blockAccess = BlockOut.getBlockOut().getProxy().getDimensionFromDimensionId(dimId);
        final IEntity entity = blockAccess.getLoadedEntities().stream().filter(e -> e.getId().equals(entityId)).findFirst().orElse(null);

        if (entity == null)
        {
            return IItemHandler.createEmpty();
        }

        if (!entity.hasCapability(ICapability.getItemHandlerCapability(), facing))
            throw new IllegalStateException("Gui created with an entity inventory for an entity that has no entity.");

        return entity.getCapability(ICapability.getItemHandlerCapability(), facing);
    }
}
