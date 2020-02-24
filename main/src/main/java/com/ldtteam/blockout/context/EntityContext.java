package com.ldtteam.blockout.context;

import com.ldtteam.blockout.context.core.IContext;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityContext implements IContext
{

    private static final long serialVersionUID = -3408028208705079936L;

    @NotNull
    private UUID entityId = new UUID(0, 0);

    public EntityContext()
    {
    }

    public EntityContext(@NotNull final UUID entityId)
    {
        this.entityId = entityId;
    }

    public EntityContext(@NotNull final Entity entity)
    {
        this.entityId = entity.getUniqueID();
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
