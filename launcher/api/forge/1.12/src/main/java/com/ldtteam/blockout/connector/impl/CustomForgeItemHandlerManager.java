package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraftforge.items.IItemHandler;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.item.handling.ItemHandler;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

class CustomForgeItemHandlerManager implements IItemHandlerManager
{
    private final IForgeItemHandlerManager wrapperForgeItemHandlerManager;

    public CustomForgeItemHandlerManager(final IForgeItemHandlerManager wrapperForgeItemHandlerManager) {this.wrapperForgeItemHandlerManager = wrapperForgeItemHandlerManager;}

    /**
     * Method used to get a {@link IItemHandler} from a given id.
     *
     * @param id The id.
     * @return The {@link IItemHandler} for the given Id;
     */
    @Nullable
    @Override
    public IItemHandler getItemHandlerFromId(@NotNull final IIdentifier id)
    {
        return ItemHandler.fromForge(wrapperForgeItemHandlerManager.getItemHandlerFromId(Identifier.asForge(id)));
    }

    /**
     * Method used to get a list of ids for {@link IItemHandler}.
     *
     * @return The ids of all registered {@link IItemHandler}.
     */
    @NotNull
    @Override
    public List<ResourceLocation> getAllItemHandlerIds()
    {
        return wrapperForgeItemHandlerManager.getAllItemHandlerIds().stream().map(Identifier::fromForge).collect(Collectors.toList());
    }

    /**
     * Getter for the underlying list of Providers.
     *
     * @return The providers.
     */
    @NotNull
    @Override
    public List<IItemHandlerProvider> getAllProviders()
    {
        return wrapperForgeItemHandlerManager.getAllProviders().stream().map(CustomForgeItemHandlerProvider::new).collect(Collectors.toList());
    }
}
