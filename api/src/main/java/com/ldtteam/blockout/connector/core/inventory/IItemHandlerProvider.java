package com.ldtteam.blockout.connector.core.inventory;

import net.minecraftforge.items.IItemHandler;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public interface IItemHandlerProvider extends Serializable
{

    /**
     * The id of the provider.
     *
     * @return The id.
     */
    @NotNull
    ResourceLocation getId();

    /**
     * Method to get the {@link IItemHandler} from this provider.
     * @param manager The {@link IItemHandlerManager} to get other {@link IItemHandler} from.
     * @return The {@link IItemHandler} that is provided by this provider.
     */
    @Nullable
    IItemHandler get(IItemHandlerManager manager);
}
