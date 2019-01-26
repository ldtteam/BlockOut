package com.ldtteam.blockout.utilities.template;

import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

public class GridUtilityWrapper<T>
{

    private final List<T>          entries;
    private final ResourceLocation template;

    public GridUtilityWrapper(final List<T> entries, final ResourceLocation template)
    {
        this.entries = entries;
        this.template = template;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getEntries(), getTemplate());
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
        final GridUtilityWrapper<?> that = (GridUtilityWrapper<?>) o;
        return Objects.equals(getEntries(), that.getEntries()) &&
                 Objects.equals(getTemplate(), that.getTemplate());
    }

    public List<T> getEntries()
    {
        return entries;
    }

    public ResourceLocation getTemplate()
    {
        return template;
    }
}
