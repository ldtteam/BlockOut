package com.ldtteam.blockout;

import com.ldtteam.blockout.element.advanced.list.factory.ListFactory;
import com.ldtteam.blockout.element.advanced.template.Template;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.*;
import com.ldtteam.blockout.plugins.IBlockOutPlugin;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.util.BlockOutControlImplementationsConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class BlockOutControlImplementations implements IBlockOutPlugin {

    @Override
    public ResourceLocation getId() {
        return BlockOutControlImplementationsConstants.PLUGIN_ID;
    }

    @Override
    public void onCommonSetup(final FMLCommonSetupEvent commonSetupEvent) {
        IProxy.getInstance().getFactoryController().registerFactory(new RootGuiElement.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new Image.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new Slot.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new Button.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new CheckBox.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new Label.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new TextField.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new BlockStateIcon.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new RangeSelector.Factory());

        IProxy.getInstance().getFactoryController().registerFactory(new Region.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new Template.Factory());
        IProxy.getInstance().getFactoryController().registerFactory(new ItemIcon.Factory());

        IProxy.getInstance().getFactoryController().registerFactory(new ListFactory());
    }
}
