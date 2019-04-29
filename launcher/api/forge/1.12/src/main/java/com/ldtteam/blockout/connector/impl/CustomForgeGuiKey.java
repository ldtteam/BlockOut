package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.connector.core.IForgeGuiKey;
import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.context.core.IContext;
import org.jetbrains.annotations.NotNull;

class CustomForgeGuiKey implements IGuiKey
{

    private final IForgeGuiKey wrappedForgeGuiKey;

    CustomForgeGuiKey(final IForgeGuiKey wrappedForgeGuiKey) {this.wrappedForgeGuiKey = wrappedForgeGuiKey;}

    @NotNull
    @Override
    public IGuiDefinitionLoader getGuiDefinitionLoader()
    {
        return wrappedForgeGuiKey.getGuiDefinitionLoader();
    }

    @NotNull
    @Override
    public IBlockOutGuiConstructionData getConstructionData()
    {
        return wrappedForgeGuiKey.getConstructionData();
    }

    @NotNull
    @Override
    public IItemHandlerManager getItemHandlerManager()
    {
        return new CustomForgeItemHandlerManager(wrappedForgeGuiKey.getItemHandlerManager());
    }

    @NotNull
    @Override
    public IContext getGuiContext()
    {
        return wrappedForgeGuiKey.getGuiContext();
    }
}
