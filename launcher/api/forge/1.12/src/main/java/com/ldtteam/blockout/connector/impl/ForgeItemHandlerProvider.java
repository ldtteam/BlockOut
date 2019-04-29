package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerProvider;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.item.handling.ItemHandler;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ForgeItemHandlerProvider implements IForgeItemHandlerProvider
{
    private final IItemHandlerProvider wrappedProvider;

    ForgeItemHandlerProvider(final IItemHandlerProvider wrappedProvider) {this.wrappedProvider = wrappedProvider;}

    /**
     * The id of the provider.
     *
     * @return The id.
     */
    @NotNull
    @Override
    public ResourceLocation getId()
    {
        return Identifier.asForge(wrappedProvider.getId());
    }

    /**
     * Method to get the {@link IItemHandler} from this provider.
     *
     * @param manager The {@link IForgeItemHandlerManager} to get other {@link IItemHandler} from.
     * @return The {@link IItemHandler} that is provided by this provider.
     */
    @Nullable
    @Override
    public IItemHandler get(final IForgeItemHandlerManager manager)
    {
        return ItemHandler.asForge(wrappedProvider.get(new CustomForgeItemHandlerManager(manager)));
    }
}
