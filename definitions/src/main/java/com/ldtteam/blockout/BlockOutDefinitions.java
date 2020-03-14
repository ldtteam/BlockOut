package com.ldtteam.blockout;

import com.ldtteam.blockout.connector.common.definition.loader.CommonClassBasedDefinitionLoader;
import com.ldtteam.blockout.connector.common.definition.loader.CommonResourceLocationBasedGuiDefinitionLoader;
import com.ldtteam.blockout.connector.common.definition.loader.CommonWebFileBasedGuiDefinitionLoader;
import com.ldtteam.blockout.plugins.IBlockOutPlugin;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.util.BlockOutDefinitionsConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.net.URL;

public class BlockOutDefinitions implements IBlockOutPlugin {

    @Override
    public ResourceLocation getId() {
        return BlockOutDefinitionsConstants.PLUGIN_ID;
    }

    @Override
    public void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        IProxy.getInstance().getDefinitionLoaderManager().registerDefinitionLoader(ResourceLocation.class, new CommonResourceLocationBasedGuiDefinitionLoader());
        IProxy.getInstance().getDefinitionLoaderManager().registerDefinitionLoader(Class.class, new CommonClassBasedDefinitionLoader());
        IProxy.getInstance().getDefinitionLoaderManager().registerDefinitionLoader(URL.class, new CommonWebFileBasedGuiDefinitionLoader());
    }
}
