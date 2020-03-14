package com.ldtteam.blockout;

import com.ldtteam.blockout.element.core.TemplateInstance;
import com.ldtteam.blockout.element.core.Wrapper;
import com.ldtteam.blockout.plugins.IBlockOutPlugin;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.util.BlockOutControlConceptsConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class BlockOutControlConcepts implements IBlockOutPlugin {

    @Override
    public ResourceLocation getId() {
        return BlockOutControlConceptsConstants.PLUGIN_ID;
    }

    @Override
    public void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        IProxy.getInstance().getFactoryController().registerFactory(new TemplateInstance.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new Wrapper.Factory());
    }
}
