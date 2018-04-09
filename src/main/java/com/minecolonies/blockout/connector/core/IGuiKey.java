package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface IGuiKey extends Serializable, Comparable<IGuiKey>
{

    @NotNull
    String getGuiDefinition();

    @NotNull
    IBlockOutGuiConstructionData getConstructionData();

    @NotNull
    IContext getGuiContext();
}
