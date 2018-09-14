package com.minecolonies.blockout.util;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiController;

/**
 * BlockOut helper class to avoid chaining common method calls.
 */
public class BlockOutHelper
{
    /**
     * Private constructor to hide implicit public one.
     */
    private BlockOutHelper()
    {
        /*
         * Intentionally left empty.
         */
    }

    /**
     * Get the GUI controller from BlockOut over the proxy.
     * @return an IGuiController.
     */
    public static IGuiController getGuiController()
    {
        return BlockOut.getBlockOut().getProxy().getGuiController();
    }
}
