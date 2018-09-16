package com.ldtteam.blockout.core.element.values;

import java.util.HashMap;
import java.util.Map;

public enum ControlDirection
{
    TOP_BOTTOM("tb", "topbottom", "top_bottom", "top bottom", "vertical"),
    BOTTOM_TOP("bt", "bottomtop", "bottom_top", "bottom top", "vertical_inverted"),
    LEFT_RIGHT("lr", "leftright", "left_right", "left right", "horizontal"),
    RIGHT_LEFT("rl", "rightleft", "right_left", "right left", "horizontal_inverted");

    private static final Map<String, ControlDirection> shorthandMap = new HashMap<>();
    static
    {
        for (final ControlDirection controlDirection : ControlDirection.values())
        {
            for (final String shortHand : controlDirection.getShortHands())
            {
                shorthandMap.put(shortHand, controlDirection);
            }
        }
    }

    private final String[] shortHands;

    ControlDirection(final String... shortHands)
    {
        if (shortHands.length == 0)
        {
            throw new IllegalArgumentException("No shorthands defined.");
        }

        this.shortHands = shortHands;
    }

    public static ControlDirection fromString(final String string)
    {
        if (!shorthandMap.containsKey(string))
        {
            throw new IllegalArgumentException("Not a valid shorthand for a ControlDirection");
        }

        return shorthandMap.get(string);
    }

    @Override
    public String toString()
    {
        return getShortHands()[0];
    }

    public String[] getShortHands()
    {
        return shortHands;
    }
}
