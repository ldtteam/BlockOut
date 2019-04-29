package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.connector.core.IForgeGuiKey;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

class ForgeGuiKey implements IForgeGuiKey
{

    private final IGuiKey wrappedKey;

    ForgeGuiKey(final IGuiKey wrappedKey) {this.wrappedKey = wrappedKey;}

    @NotNull
    @Override
    public IGuiDefinitionLoader getGuiDefinitionLoader()
    {
        return wrappedKey.getGuiDefinitionLoader();
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionData getConstructionData()
    {
        return wrappedKey.getConstructionData();
    }

    @NotNull
    @Override
    public IForgeItemHandlerManager getItemHandlerManager()
    {
        return new ForgeItemHandlerManager(wrappedKey.getItemHandlerManager());
    }

    @NotNull
    @Override
    public IContext getGuiContext()
    {
        return wrappedKey.getGuiContext();
    }
}
