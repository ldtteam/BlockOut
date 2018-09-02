package com.minecolonies.blockout.connector.common.inventory.provider;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CommonEntityBasedProvider implements IItemHandlerProvider
{
    @NotNull
    private final String id;

    @NotNull
    private final int        dimId;
    @NotNull
    private final UUID       entityId;
    @Nullable
    private final EnumFacing facing;

    public CommonEntityBasedProvider(@NotNull final ResourceLocation id, @NotNull final int dimId, @NotNull final UUID entityId, @Nullable final EnumFacing facing)
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
    public ResourceLocation getId()
    {
        return new ResourceLocation(id);
    }

    /**
     *
     * @return
     */
    @Nullable
    @Override
    public IItemHandler get()
    {
        final World blockAccess = BlockOut.getBlockOut().getProxy().getWorldFromDimensionId(dimId);
        final Entity entity = blockAccess.loadedEntityList.stream().filter(e -> e.getPersistentID().equals(entityId)).findFirst().orElse(null);

        if (entity == null)
        {
            return new EmptyHandler();
        }

        return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
    }
}
