package com.minecolonies.blockout;

import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.element.simple.Button;
import com.minecolonies.blockout.element.simple.Image;
import com.minecolonies.blockout.element.simple.Label;
import com.minecolonies.blockout.element.simple.Slot;
import com.minecolonies.blockout.loader.json.JsonLoader;
import com.minecolonies.blockout.loader.object.loader.ObjectUIElementLoader;
import com.minecolonies.blockout.loader.xml.XMLLoader;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.proxy.IProxy;
import com.minecolonies.blockout.style.resources.ImageResource;
import com.minecolonies.blockout.style.resources.ItemStackResource;
import com.minecolonies.blockout.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
        return proxy;
    }

    /**
     * Event handler for forge pre init event.
     *
     * @param event the forge pre init event.
     */
    @Mod.EventHandler
    public void preInit(@NotNull final FMLPreInitializationEvent event)
    {
        NetworkManager.init();

        getProxy().getLoaderManager().registerLoader(new JsonLoader());
        getProxy().getLoaderManager().registerLoader(new XMLLoader());
        getProxy().getLoaderManager().registerLoader(new ObjectUIElementLoader());

        getProxy().getFactoryController().registerFactory(new RootGuiElement.Factory());
        getProxy().getFactoryController().registerFactory(new Image.Factory());
        getProxy().getFactoryController().registerFactory(new Slot.Factory());
        getProxy().getFactoryController().registerFactory(new Button.Factory());
        getProxy().getFactoryController().registerFactory(new Label.Factory());

        getProxy().getResourceLoaderManager().registerTypeLoader(new ImageResource.Loader());
        getProxy().getResourceLoaderManager().registerTypeLoader(new ItemStackResource.Loader());
    }

    @Mod.EventHandler
    public void onInit(final FMLInitializationEvent event)
    {
        getProxy().initializeFontRenderer();
    }
}
