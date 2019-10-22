package com.ldtteam.blockout.inventory;

import com.ldtteam.blockout.gui.BlockOutGuiData;

/**
 * Represents a BlockOut container with its additional data.
 */
public interface IBlockOutContainer
{
    /**
     * Getter that gives access to BOs additional data.
     * @return The data used by the container.
     */
    BlockOutContainerData getInstanceData();
}
