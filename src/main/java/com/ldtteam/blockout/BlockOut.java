package com.ldtteam.blockout;

import com.ldtteam.blockout.element.advanced.List;
import com.ldtteam.blockout.element.advanced.TemplateInstance;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.*;
import com.ldtteam.blockout.element.template.Template;
import com.ldtteam.blockout.json.JsonLoader;
import com.ldtteam.blockout.loader.object.loader.ObjectUIElementLoader;
import com.ldtteam.blockout.network.NetworkManager;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.style.resources.ImageResource;
import com.ldtteam.blockout.style.resources.ItemStackResource;
import com.ldtteam.blockout.style.resources.TemplateResource;
import com.ldtteam.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.jetbrains.annotations.NotNull;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION,
  dependencies = Constants.FORGE_VERSION, acceptedMinecraftVersions = Constants.MC_VERSION)
public class BlockOut
{

    public static BlockOut getBlockOut()
    {
        return blockOut;
    }

    public static boolean isDebugging() {return Constants.DEBUG.equals("@DEBUG@");}

    @SidedProxy(clientSide = Constants.PROXY_CLIENT, serverSide = Constants.PROXY_COMMON)
    private static IProxy proxy;

    @Mod.Instance
    private static BlockOut blockOut;

    public IProxy getProxy()
    {
        return ProxyHolder.getInstance();
    }

    /**
     * Event handler for forge pre init event.
     *
     * @param event the forge pre init event.
     */
    @Mod.EventHandler
    public void preInit(@NotNull final FMLPreInitializationEvent event)
    {
        ProxyHolder.getInstance().setProxy(proxy);
        NetworkManager.init();

        getProxy().getLoaderManager().registerLoader(new JsonLoader());
        //getProxy().getLoaderManager().registerLoader(new XMLLoader());
        getProxy().getLoaderManager().registerLoader(new ObjectUIElementLoader());

        getProxy().getFactoryController().registerFactory(new RootGuiElement.Factory());
        getProxy().getFactoryController().registerFactory(new Image.Factory());
        getProxy().getFactoryController().registerFactory(new Slot.Factory());
        getProxy().getFactoryController().registerFactory(new Button.Factory());
        getProxy().getFactoryController().registerFactory(new CheckBox.Factory());
        getProxy().getFactoryController().registerFactory(new Label.Factory());
        getProxy().getFactoryController().registerFactory(new TextField.Factory());
        getProxy().getFactoryController().registerFactory(new TemplateInstance.Factory());

        getProxy().getFactoryController().registerFactory(new Region.Factory());
        getProxy().getFactoryController().registerFactory(new Template.Factory());
        getProxy().getFactoryController().registerFactory(new ItemIcon.Factory());

        getProxy().getFactoryController().registerFactory(new List.Factory());

        getProxy().getResourceLoaderManager().registerTypeLoader(new ImageResource.Loader());
        getProxy().getResourceLoaderManager().registerTypeLoader(new ItemStackResource.Loader());
        getProxy().getResourceLoaderManager().registerTypeLoader(new TemplateResource.Loader());
    }

    @Mod.EventHandler
    public void onInit(final FMLInitializationEvent event)
    {
        getProxy().initializeFontRenderer();
    }

    @Mod.EventHandler
    public void onFMLPostInitialization(final FMLPostInitializationEvent event)
    {
        getProxy().getStyleManager().loadStyles();
    }
}
