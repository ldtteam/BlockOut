package com.ldtteam.blockout.json;

import com.ldtteam.blockout.json.util.BlockOutJsonConstants;
import com.ldtteam.blockout.plugins.IBlockOutPlugin;
import com.ldtteam.blockout.proxy.IProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * The BlockOut plugin for JSON based UIs.
 */
public class BlockOutJson implements IBlockOutPlugin
{
    @Override
    public ResourceLocation getId() {
        return BlockOutJsonConstants.PLUGIN_ID;
    }

    @Override
    public void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        IProxy.getInstance().getLoaderManager().registerLoader(new JsonLoader());
    }
}
