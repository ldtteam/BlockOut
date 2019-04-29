package com.ldtteam.blockout.connector.impl;

import com.ldtteam.blockout.connector.core.IForgeGuiController;
import com.ldtteam.blockout.connector.core.IForgeGuiKey;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.builder.IForgeGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class ForgeGuiController implements IForgeGuiController
{

    private final IGuiController wrappedController;

    public ForgeGuiController(final IGuiController wrappedController) {this.wrappedController = wrappedController;}

    /**
     * Opens a UI for a given player based on the logic provided by the callback to construct a {@link IForgeGuiKey}
     *
     * @param player                The player to open the UI for.
     * @param guiKeyBuilderConsumer The construction logic.
     */
    @Override
    public void openUI(
      @NotNull final EntityPlayer player, @NotNull final Consumer<IForgeGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        wrappedController.openUI(
          PlayerEntity.fromForge(player),
          Arrays.stream(guiKeyBuilderConsumer)
            .map(keyLogic -> (Consumer<IGuiKeyBuilder>) iGuiKeyBuilder -> keyLogic.accept(new ForgeGuiKeyBuilder(iGuiKeyBuilder)))
            .toArray((IntFunction<Consumer<IGuiKeyBuilder>[]>) Consumer[]::new)
        );
    }

    /**
     * Opens a UI for a given player based on the key given.
     *
     * @param player The player to open the UI for.
     * @param key    The gui key to open.
     */
    @Override
    public void openUI(@NotNull final EntityPlayer player, @NotNull final IForgeGuiKey key)
    {
        wrappedController.openUI(
          PlayerEntity.fromForge(player),
          new CustomForgeGuiKey(key)
        );
    }
}
