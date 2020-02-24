package com.ldtteam.blockout.connector.common.inventory.provider;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonEntityBasedProvider implements IItemHandlerProvider
{
    private static final long serialVersionUID = 371842663405988787L;

    @NotNull
    private final String id;

    @NotNull
    private final int     dimId;
    @NotNull
    private final int    entityNetworkId;
    @Nullable
    private final Direction facing;

    public CommonEntityBasedProvider(@NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final int entityNetworkId, @Nullable final Direction facing)
    {
        this.id = id.toString();
        this.dimId = dimId;
        this.entityNetworkId = entityNetworkId;
        this.facing = facing;
    }

    @Override
    public int hashCode()
    {
        int result = getId().hashCode();
        result = 31 * result + dimId;
        result = 31 * result + entityNetworkId;
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
        if (entityNetworkId != that.entityNetworkId)
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
    public ResourceLocation getId()
    {
        return new ResourceLocation(id);
    }

    /**
     *
     */
    @Nullable
    @Override
    public IItemHandler get(@NotNull final IItemHandlerManager manager)
    {
        final World blockAccess = ProxyHolder.getInstance().getDimensionFromDimensionId(dimId);
        final Entity entity = blockAccess.getEntityByID(entityNetworkId);

        if (entity == null)
        {
            return EmptyHandler.INSTANCE;
        }

        return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).orElseThrow(() -> new IllegalArgumentException("Entity with network id: " + entityNetworkId + " does not have in inventory at facing: " + facing));
    }
}
