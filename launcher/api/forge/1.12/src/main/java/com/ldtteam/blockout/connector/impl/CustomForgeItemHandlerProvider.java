package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.connector.core.inventory.IForgeItemHandlerProvider;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraftforge.items.IItemHandler;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.item.handling.ItemHandler;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CustomForgeItemHandlerProvider implements IItemHandlerProvider
{
    private final IForgeItemHandlerProvider wrappedForgeProvider;

    public CustomForgeItemHandlerProvider(final IForgeItemHandlerProvider wrappedForgeProvider) {this.wrappedForgeProvider = wrappedForgeProvider;}

    /**
     * The id of the provider.
     *
     * @return The id.
     */
    @NotNull
    @Override
    public ResourceLocation getId()
    {
        return Identifier.fromForge(wrappedForgeProvider.getId());
    }

    /**
     * Method to get the {@link IItemHandler} from this provider.
     *
     * @param manager The {@link IItemHandlerManager} to get other {@link IItemHandler} from.
     * @return The {@link IItemHandler} that is provided by this provider.
     */
    @Nullable
    @Override
    public IItemHandler get(final IItemHandlerManager manager)
    {
        return ItemHandler.fromForge(wrappedForgeProvider.get(new ForgeItemHandlerManager(manager)));
    }
}
