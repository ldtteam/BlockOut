package com.ldtteam.blockout.inventory;

import com.ldtteam.blockout.util.Constants;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(value = Constants.MOD_ID)
public final class ContainerTypes
{

    private ContainerTypes()
    {
        throw new IllegalStateException("Tried to initialize: ContainerTypes but this is a Utility class.");
    }

    @ObjectHolder(value = Constants.MOD_ID)
    public static final ContainerType<BlockOutContainer> BLOCK_OUT_CONTAINER = null;
}
