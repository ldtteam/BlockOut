package com.minecolonies.blockout.connector.core;

import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IGuiController
{

    @NotNull
    IUIElementHost openUI(@NotNull final ResourceLocation id, @NotNull final UUID playerId);

    void onUiClosed(@NotNull final ResourceLocation id, @NotNull final UUID playerId);
}
