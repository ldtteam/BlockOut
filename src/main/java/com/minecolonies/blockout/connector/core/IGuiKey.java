package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.builder.core.IBlockOutGuiConstructionData;
import com.minecolonies.blockout.connector.core.inventory.IItemHandlerManager;
import com.minecolonies.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface IGuiKey extends Serializable
{

    @NotNull
    IGuiDefinitionLoader getGuiDefinitionLoader();

    @NotNull
    IBlockOutGuiConstructionData getConstructionData();

    @NotNull
    IItemHandlerManager getItemHandlerManager();

    @NotNull
    IContext getGuiContext();
}
