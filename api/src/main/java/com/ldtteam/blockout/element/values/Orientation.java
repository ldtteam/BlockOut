package com.ldtteam.blockout.element.values;

import java.util.HashMap;
import java.util.Map;

public enum Orientation
{
    TOP_BOTTOM("tb", "topbottom", "top_bottom", "top bottom", "vertical"),
    BOTTOM_TOP("bt", "bottomtop", "bottom_top", "bottom top", "vertical_inverted"),
    LEFT_RIGHT("lr", "leftright", "left_right", "left right", "horizontal"),
    RIGHT_LEFT("rl", "rightleft", "right_left", "right left", "horizontal_inverted");

    private static final Map<String, Orientation> shorthandMap = new HashMap<>();
    static
    {
        for (final Orientation orientation : Orientation.values())
        {
            for (final String shortHand : orientation.getShortHands())
            {
                shorthandMap.put(shortHand, orientation);
            }
        }
    }

    private final String[] shortHands;

    Orientation(final String... shortHands)
    {
        if (shortHands.length == 0)
        {
            throw new IllegalArgumentException("No shorthands defined.");
        }

        this.shortHands = shortHands;
    }

    public static Orientation fromString(final String string)
    {
        if (!shorthandMap.containsKey(string))
        {
            throw new IllegalArgumentException("Not a valid shorthand for a Orientation");
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
