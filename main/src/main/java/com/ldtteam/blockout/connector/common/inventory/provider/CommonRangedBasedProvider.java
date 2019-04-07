package com.ldtteam.blockout.connector.common.inventory.provider;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.jvoxelizer.item.handling.IItemHandler;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonRangedBasedProvider implements IItemHandlerProvider
{
    private final String id;
    private final String wrappedId;
    private final int    minSlot;
    private final int    maxSlotExlcuding;

    public CommonRangedBasedProvider(final String id, final String wrappedId, final int minSlot, final int maxSlotExlcuding)
    {
        this.id = id;
        this.wrappedId = wrappedId;
        this.minSlot = minSlot;
        this.maxSlotExlcuding = maxSlotExlcuding;

        if (id.equalsIgnoreCase(wrappedId))
        {
            throw new IllegalArgumentException("Can not reference yourself when ranging");
        }
    }

    @NotNull
    @Override
    public IIdentifier getId()
    {
        return IIdentifier.create(id);
    }

    @Nullable
    @Override
    public IItemHandler get(final IItemHandlerManager manager)
    {
        final IItemHandler other = manager.getItemHandlerFromId(IIdentifier.create(wrappedId));

        return IItemHandler.ranged(other, minSlot, maxSlotExlcuding);
    }
}
