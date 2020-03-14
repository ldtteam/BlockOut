package com.ldtteam.blockout.builder;

import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;

public class CommonBuilderManager implements IBuilderManager {

    public CommonBuilderManager() {
    }

    @Override
    public IBlockOutGuiConstructionDataBuilder getConstructionData() {
        return new BlockOutGuiConstructionDataBuilder();
    }
}
