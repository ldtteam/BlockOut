package com.ldtteam.blockout.util.mouse;

import java.util.HashMap;
import java.util.Map;

public enum MouseButton
{
    LEFT,
    RIGHT,
    OTHER;

    private static final Map<Integer, MouseButton> mappings = new HashMap<>();

    static
    {
        mappings.put(0, LEFT);
        mappings.put(1, RIGHT);
    }

    public static MouseButton getForCode(final int code)
    {
        if (mappings.containsKey(code))
        {
            return mappings.get(code);
        }

        return OTHER;
    }
}
