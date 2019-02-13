package com.ldtteam.guidebook_test.items;

import net.minecraft.item.Item;

/**
 * Handles simple things that all item need.
 */
public abstract class AbstractItemMinecolonies extends Item
{
    /**
     * The name of the item.
     */
    private final String name;

    /**
     * Sets the name, creative tab, and registers the item.
     *
     * @param name The name of this item
     */
    public AbstractItemMinecolonies(final String name)
    {
        super();
        this.name = name;
        super.setTranslationKey("guidebook_test" + "." + this.name);
        setRegistryName("guidebook_test", this.name);
    }

    /**
     * Returns the name of the item.
     *
     * @return Name of the item.
     */
    public final String getName()
    {
        return name;
    }
}
