package com.ldtteam.blockout.connector.core;

import com.ldtteam.blockout.connector.core.builder.IForgeGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.connector.impl.ForgeGuiController;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A wrapper for the {@link IGuiController} that deals with wrapping JVoxelizer based classes and types automatically.
 */
public interface IForgeGuiController
{
    /**
     * Getter for the instance, allows easy access to the controller.
     * @return The instance.
     */
    static IForgeGuiController getInstance()
    {
        return new ForgeGuiController(IGuiController.getInstance());
    }

    /** Opens a UI for a given player based on the logic provided by the callback to construct a {@link IForgeGuiKey}
     *
     * @param player The player to open the UI for.
     * @param guiKeyBuilderConsumer The construction logic.
     */
    void openUI(@NotNull final EntityPlayer player, @NotNull final Consumer<IForgeGuiKeyBuilder>... guiKeyBuilderConsumer);

    /**
     * Opens a UI for a given player based on the key given.
     *
     * @param player The player to open the UI for.
     * @param key The gui key to open.
     */
    void openUI(@NotNull final EntityPlayer player, @NotNull final IForgeGuiKey key);

}
