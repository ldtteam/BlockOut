package com.minecolonies.blockout.connector.core;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IGuiController
{
    void openUI(@NotNull final IGuiKey key, @NotNull final UUID playerId);

    void onUiClosed(@NotNull final UUID playerId);
}
