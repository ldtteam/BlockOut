package com.ldtteam.blockout.connector.core.inventory;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IForgeItemHandlerProvider
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
     * @param manager The {@link IForgeItemHandlerManager} to get other {@link IItemHandler} from.
     * @return The {@link IItemHandler} that is provided by this provider.
     */
    @Nullable
    IItemHandler get(IForgeItemHandlerManager manager);
}
