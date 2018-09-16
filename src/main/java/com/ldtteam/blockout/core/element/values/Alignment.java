package com.ldtteam.blockout.core.element.values;

import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public enum Alignment
{
    LEFT(1),
    RIGHT(2),
    TOP(4),
    BOTTOM(8),
    NONE(0);

    private final int bitMask;

    Alignment(final int bitMask) {this.bitMask = bitMask;}

    public static int toInt(@NotNull final EnumSet<Alignment> flags)
    {
        int value = 0;

        for (Alignment alignment : flags)
        {
            value |= alignment.getBitMask();
        }

        return value;
    }

    public int getBitMask()
    {
        return bitMask;
    }

    public static EnumSet<Alignment> fromString(@NotNull final String string)
    {
        try
        {
            final Integer value = Integer.parseUnsignedInt(string);
            return fromInt(value);
        }
        catch (Exception ex)
        {
            //No number that fits the bill.
            final String[] components = string.split(",");
            final EnumSet<Alignment> result = EnumSet.noneOf(Alignment.class);

            for (String component : components)
            {
                try
                {
                    result.add(valueOf(component.trim().toUpperCase()));
                }
                catch (Exception exc)
                {
                    Log.getLogger().error("Failed to parse alignment: " + component);
                }
            }

            return result;
        }
    }

    @NotNull
    public static EnumSet<Alignment> fromInt(final int mask)
    {
        if (mask == 0)
        {
            return EnumSet.of(NONE);
        }

        final EnumSet<Alignment> result = EnumSet.noneOf(Alignment.class);

        for (Alignment alignment : values())
        {
            if ((mask & alignment.getBitMask()) > 0)
            {
                result.add(alignment);
            }
        }

        return result;
    }

    public boolean isActive(@NotNull final IUIElement element)
    {
        return isActive(element.getAlignment());
    }

    public boolean isActive(@NotNull final Set<Alignment> alignments)
    {
        return alignments.contains(this);
    }
}
