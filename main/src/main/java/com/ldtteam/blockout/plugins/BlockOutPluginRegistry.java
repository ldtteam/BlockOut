package com.ldtteam.blockout.plugins;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.ServiceLoader;

public class BlockOutPluginRegistry implements IBlockOutPluginRegistry {

    private static final IBlockOutPluginRegistry INSTANCE = new BlockOutPluginRegistry();

    public static IBlockOutPluginRegistry getInstance() {
        return INSTANCE;
    }

    private static final Logger LOGGER = LogManager.getLogger(BlockOutPluginRegistry.class);

    private final Map<ResourceLocation, IBlockOutPlugin> plugins = Maps.newHashMap();

    @Override
    public void performAutomaticDiscovery()
    {
        LOGGER.warn("Starting automatic classpath scan for BlockOut plugins.");
        final ServiceLoader<IBlockOutPlugin> pluginLoader = ServiceLoader.load(IBlockOutPlugin.class);
        pluginLoader.forEach(p -> {
            this.plugins.put(p.getId(), p);
        });

        LOGGER.info("Finished the loading of BlockOut plugins.");
        LOGGER.info("The following plugins have been loaded:");
        this.plugins.keySet().forEach(id -> LOGGER.info("  > " + id));
    }

    @Override
    public Map<ResourceLocation, IBlockOutPlugin> getPlugins() {
        return plugins;
    }
}
