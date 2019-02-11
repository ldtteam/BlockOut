package com.ldtteam.blockout.utilities.template;

import net.minecraft.block.state.IBlockState;

public class BlockStateUtilityWrapper
{

    private final IBlockState blockState;

    public BlockStateUtilityWrapper(final IBlockState blockState) {this.blockState = blockState;}

    public IBlockState getBlockState()
    {
        return blockState;
    }
}
