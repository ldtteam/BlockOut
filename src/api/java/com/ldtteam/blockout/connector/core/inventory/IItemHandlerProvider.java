package com.ldtteam.blockout.connector.core.inventory;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
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
     *
     * @return
     */
    @Nullable
    IItemHandler get(IItemHandlerManager manager);
}
