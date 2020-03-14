package com.ldtteam.blockout.connector.core;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface IGuiKey extends Serializable
{

    @NotNull
    Object getDataSource();

    @NotNull
    IBlockOutGuiConstructionData getConstructionData();

    @NotNull
    IItemHandlerManager getItemHandlerManager();

    @NotNull
    IContext getGuiContext();
}
