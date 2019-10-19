package com.ldtteam.blockout.helpers.template;

import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;

import java.util.Objects;

public class SlotUtilityWrapper
{
    private final IIdentifier inventory;
    private final IIdentifier texture;
    private final int         index;

    public SlotUtilityWrapper(final IIdentifier inventory, final IIdentifier texture, final int index)
    {
        this.inventory = inventory;
        this.texture = texture;
        this.index = index;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getInventory(), getTexture(), getIndex());
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
        final SlotUtilityWrapper that = (SlotUtilityWrapper) o;
        return getIndex() == that.getIndex() &&
                 Objects.equals(getInventory(), that.getInventory()) &&
                 Objects.equals(getTexture(), that.getTexture());
    }

    public IIdentifier getInventory()
    {
        return inventory;
    }

    public IIdentifier getTexture()
    {
        return texture;
    }

    public int getIndex()
    {
        return index;
    }
}