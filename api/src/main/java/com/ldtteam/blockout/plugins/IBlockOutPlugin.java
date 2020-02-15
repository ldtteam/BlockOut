package com.ldtteam.blockout.plugins;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * A single plugin for BlockOut,
 * its methods are called async, during mod loading. Keep that in mind.
 */
public interface IBlockOutPlugin
{

    /**
     * Called during common setup.
     *
     * @param commonSetupEvent The common setup event.
     */
    void onCommonSetup(FMLCommonSetupEvent commonSetupEvent);
}
