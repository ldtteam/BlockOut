package com.ldtteam.blockout.style;

import com.ldtteam.blockout.plugins.IBlockOutPlugin;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.style.resources.ItemStackResource;
import com.ldtteam.blockout.style.resources.TemplateResource;
import com.ldtteam.blockout.style.util.BlockOutStylingConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class BlockOutStyling implements IBlockOutPlugin {

    @Override
    public ResourceLocation getId() {
        return BlockOutStylingConstants.PLUGIN_ID;
    }

    @Override
    public void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        IProxy.getInstance().getResourceLoaderManager().registerTypeLoader(new ImageResource.Loader());
        IProxy.getInstance().getResourceLoaderManager().registerTypeLoader(new ItemStackResource.Loader());
        IProxy.getInstance().getResourceLoaderManager().registerTypeLoader(new TemplateResource.Loader());
    }
}
