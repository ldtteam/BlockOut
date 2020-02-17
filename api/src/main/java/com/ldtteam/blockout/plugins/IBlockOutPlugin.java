package com.ldtteam.blockout.plugins;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

/**
 * A single plugin for BlockOut,
 * its methods are called async, during mod loading. Keep that in mind.
 */
public interface IBlockOutPlugin
{

    /**
     * The id of the plugin.
     *
     * @return The id.
     */
    ResourceLocation getId();

    /**
     * Called during common setup.
     *
     * @param commonSetupEvent The common setup event.
     */
    default void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        //Noop
    }

    /**
     * Called during the client setup.
     *
     * @param clientSetupEvent The client setup event.
     */
    default void onClientSetup(final FMLClientSetupEvent clientSetupEvent) {
        //Noop
    }

    /**
     * Called during the dedicated setup.
     *
     * @param dedicatedServerSetupEvent The dedicated setup event.
     */
    default void onDedicatedServerSetup(final FMLDedicatedServerSetupEvent dedicatedServerSetupEvent) {
        //Noop
    }
}
