package com.ldtteam.blockout.connector.core.inventory;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IForgeItemHandlerManager
{
    /**
     * Method used to get a {@link IItemHandler} from a given id.
     *
     * @param id The id.
     * @return The {@link IItemHandler} for the given Id;
     */
    @Nullable
    IItemHandler getItemHandlerFromId(@NotNull final ResourceLocation id);

    /**
     * Method used to get a list of ids for {@link IItemHandler}.
     *
     * @return The ids of all registered {@link IItemHandler}.
     */
    @NotNull
    List<ResourceLocation> getAllItemHandlerIds();

    /**
     * Getter for the underlying list of Providers.
     *
     * @return The providers.
     */
    @NotNull
    List<IForgeItemHandlerProvider> getAllProviders();
}
