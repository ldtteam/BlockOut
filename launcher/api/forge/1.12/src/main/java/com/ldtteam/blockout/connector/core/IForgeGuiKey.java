package com.ldtteam.blockout.connector.core;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

public interface IForgeGuiKey
{
    @NotNull
    IGuiDefinitionLoader getGuiDefinitionLoader();

    @NotNull
    IBlockOutGuiConstructionData getConstructionData();

    @NotNull
    IForgeItemHandlerManager getItemHandlerManager();

    @NotNull
    IContext getGuiContext();
}
