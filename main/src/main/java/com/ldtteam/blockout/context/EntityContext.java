package com.ldtteam.blockout.context;

import com.ldtteam.blockout.context.core.IContext;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.jvoxelizer.entity.IEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityContext implements IContext
{
    private static final long serialVersionUID = Constants.SERIAL_VAR_ID;

    @NotNull
    private UUID entityId = new UUID(0, 0);

    public EntityContext()
    {
    }

    public EntityContext(@NotNull final UUID entityId)
    {
        this.entityId = entityId;
    }

    public EntityContext(@NotNull final IEntity entity)
    {
        this.entityId = entity.getId();
    }

    @Override
    public int hashCode()
    {
        return getEntityId().hashCode();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof EntityContext))
        {
            return false;
        }

        final EntityContext that = (EntityContext) o;

        return getEntityId().equals(that.getEntityId());
    }

    @Override
    public String toString()
    {
        return "EntityContext{" +
                 "entityId=" + entityId +
                 '}';
    }

    @NotNull
    public UUID getEntityId()
    {
        return entityId;
    }
}
