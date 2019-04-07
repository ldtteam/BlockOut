package com.ldtteam.blockout.utilities.template;

import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

public class GridUtilityWrapper<T>
{

    private final List<T>     entries;
    private final IIdentifier template;

    public GridUtilityWrapper(final List<T> entries, final IIdentifier template)
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

    public IIdentifier getTemplate()
    {
        return template;
    }
}
