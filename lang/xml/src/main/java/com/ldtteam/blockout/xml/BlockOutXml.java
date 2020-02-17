package com.ldtteam.blockout.xml;

import com.ldtteam.blockout.plugins.IBlockOutPlugin;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.xml.util.BlockOutXmlConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * The BlockOut plugin for loading XML based UIs.
 */
public class BlockOutXml implements IBlockOutPlugin
{

    @Override
    public ResourceLocation getId() {
        return BlockOutXmlConstants.PLUGIN_ID;
    }

    @Override
    public void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        IProxy.getInstance().getLoaderManager().registerLoader(new XmlLoader());
    }
}
