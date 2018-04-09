package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.connector.core.builder.IGuiKeyBuilder;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public interface IGuiController
{
    void openUI(@NotNull final EntityPlayer player, @NotNull final Consumer<IGuiKeyBuilder> guiKeyBuilderConsumer);

    void openUI(@NotNull final EntityPlayer player, @NotNull final IGuiKey key);

    void openUI(@NotNull final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder> guiKeyBuilderConsumer);

    void openUI(@NotNull final UUID playerId, @NotNull final IGuiKey key);

    void closeUI(@NotNull final EntityPlayer player);

    void closeUI(@NotNull final UUID playerId);
}
