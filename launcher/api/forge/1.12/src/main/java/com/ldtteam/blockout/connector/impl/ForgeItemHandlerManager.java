package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerProvider;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.item.handling.ItemHandler;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ForgeItemHandlerManager implements IForgeItemHandlerManager
{
    private final IItemHandlerManager wrapperManager;

    public ForgeItemHandlerManager(final IItemHandlerManager wrapperManager) {this.wrapperManager = wrapperManager;}

    /**
     * Method used to get a {@link IItemHandler} from a given id.
     *
     * @param id The id.
     * @return The {@link IItemHandler} for the given Id;
     */
    @Nullable
    @Override
    public IItemHandler getItemHandlerFromId(@NotNull final ResourceLocation id)
    {
        return ItemHandler.asForge(wrapperManager.getItemHandlerFromId(Identifier.fromForge(id)));
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
        return wrapperManager.getAllItemHandlerIds().stream().map(Identifier::asForge).collect(Collectors.toList());
    }

    /**
     * Getter for the underlying list of Providers.
     *
     * @return The providers.
     */
    @NotNull
    @Override
    public List<IForgeItemHandlerProvider> getAllProviders()
    {
        return wrapperManager.getAllProviders().stream().map(ForgeItemHandlerProvider::new).collect(Collectors.toList());
    }
}
