package com.ldtteam.blockout.connector.core;

import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public interface IGuiController
{
    static IGuiController getInstance()
    {
        return ProxyHolder.getInstance().getGuiController();
    }

    @SuppressWarnings("unchecked")
    void openUI(@NotNull final PlayerEntity player, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer);

    void openUI(@NotNull final PlayerEntity player, @NotNull final IGuiKey key);

    @SuppressWarnings("unchecked")
    void openUI(@NotNull final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer);

    void openUI(@NotNull final UUID playerId, @NotNull final IGuiKey key);

    void closeUI(@NotNull final PlayerEntity player);

    void closeUI(@NotNull final UUID playerId);

    @Nullable
    IGuiKey getOpenUI(@NotNull final PlayerEntity player);

    @Nullable
    IGuiKey getOpenUI(@NotNull final UUID player);

    @Nullable
    IRootGuiElement getRoot(@NotNull final IGuiKey guiKey);
}
