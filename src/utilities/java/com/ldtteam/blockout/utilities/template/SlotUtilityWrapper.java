package com.ldtteam.blockout.utilities.template;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class SlotUtilityWrapper
{

    private final ResourceLocation inventory;
    private final ResourceLocation texture;
    private final int index;

    public SlotUtilityWrapper(final ResourceLocation inventory, final ResourceLocation texture, final int index) {
        this.inventory = inventory;
        this.texture = texture;
        this.index = index;
    }

    public ResourceLocation getInventory()
    {
        return inventory;
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    public int getIndex()
    {
        return index;
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

    @Override
    public int hashCode()
    {
        return Objects.hash(getInventory(), getTexture(), getIndex());
    }
}
