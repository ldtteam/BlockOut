package com.ldtteam.blockout.plugins;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface IBlockOutPluginRegistry {

    void performAutomaticDiscovery();

    Map<ResourceLocation, IBlockOutPlugin> getPlugins();
}
