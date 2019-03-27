package com.ldtteam.blockout.connector.core.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public interface IItemHandlerManager extends Serializable
{

    /**
     * Method used to get a {@link IItemHandler} from a given id.
     *
     * @param id The id.
     * @return The {@link IItemHandler} for the given Id;
     */
    @Nullable
    IItemHandler getItemHandlerFromId(@NotNull final IIdentifier id);

    /**
     * Method used to get a list of ids for {@link IItemHandler}.
     *
     * @return The ids of all registered {@link IItemHandler}.
     */
    @NotNull
    List<IIdentifier> getAllItemHandlerIds();

    /**
     * Getter for the underlying list of Providers.
     *
     * @return The providers.
     */
    @NotNull
    List<IItemHandlerProvider> getAllProviders();
}
