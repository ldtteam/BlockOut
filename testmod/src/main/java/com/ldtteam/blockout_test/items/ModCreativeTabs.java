package com.ldtteam.blockout_test.items;

import com.ldtteam.blockout_test.EventHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Class used to handle the creativeTab of minecolonies.
 */
public final class ModCreativeTabs
{
    public static final CreativeTabs BOTEST = new CreativeTabs("blockout_test")
    {

        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(EventHandler.guidebook);
        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }
    };

    /**
     * Private constructor to hide the implicit one.
     */
    private ModCreativeTabs()
    {
        /*
         * Intentionally left empty.
         */
    }
}
