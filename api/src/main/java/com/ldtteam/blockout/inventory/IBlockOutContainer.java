package com.ldtteam.blockout.inventory;

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
