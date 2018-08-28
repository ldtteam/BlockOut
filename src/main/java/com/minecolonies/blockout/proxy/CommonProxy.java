package com.minecolonies.blockout.proxy;

import com.minecolonies.blockout.connector.common.CommonFactoryController;
import com.minecolonies.blockout.connector.common.CommonLoaderManager;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.connector.core.IUIElementFactoryController;
import com.minecolonies.blockout.connector.server.ServerGuiController;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.management.server.network.ServerNetworkManager;
import com.minecolonies.blockout.management.server.update.ServerUpdateManager;
import com.minecolonies.blockout.style.core.IStyleManager;
import com.minecolonies.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.minecolonies.blockout.style.simple.SimpleFileBasedStyleManager;
import com.minecolonies.blockout.style.simple.SimpleResourceLoaderManager;
import com.minecolonies.blockout.template.ITemplateEngine;
import com.minecolonies.blockout.template.SimpleTemplateEngine;
import com.minecolonies.blockout.util.color.MultiColoredFontRenderer;
import com.minecolonies.blockout.util.image.ImageUtil;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class CommonProxy implements IProxy
{
    private final ServerGuiController     controller;
    private final CommonLoaderManager     commonLoaderManager;
    private final CommonFactoryController commonFactoryController;

    public CommonProxy()
    {
        controller = new ServerGuiController();
        commonLoaderManager = new CommonLoaderManager();
        commonFactoryController = new CommonFactoryController();
    }

    @Override
    public void onPreInit()
    {
        //TODO: Create split style loader.
    }

    @NotNull
    @Override
    public IGuiController getGuiController()
    {
        return controller;
    }

    @NotNull
    @Override
    public ILoaderManager getLoaderManager()
    {
        return commonLoaderManager;
    }

    @NotNull
    @Override
    public IUIElementFactoryController getFactoryController()
    {
        return commonFactoryController;
    }

    @Override
    @NotNull
    public InputStream getResourceStream(@NotNull final ResourceLocation location) throws Exception
    {
        final String modId = location.getResourceDomain().toLowerCase();
        final String path = "assets/" + modId + "/" + location.getResourcePath();

        final Object mod = Loader.instance().getIndexedModList().get(modId).getMod();
        return mod.getClass().getResourceAsStream(path);
    }

    @NotNull
    @Override
    public Vector2d getImageSize(@NotNull final ResourceLocation location)
    {
        return ImageUtil.getImageDimensions(location);
    }

    @NotNull
    @Override
    public INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key)
    {
        return new ServerNetworkManager(key);
    }

    @NotNull
    @Override
    public IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager)
    {
        return new ServerUpdateManager(manager);
    }

    @NotNull
    @Override
    public IBlockAccess getBlockAccessFromDimensionId(@NotNull final int dimId)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimId);
    }

    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public IRenderManager generateNewRenderManager()
    {
        return null;
    }

    @Override
    public void initializeFontRenderer()
    {
        //NOOP
    }

    @NotNull
    @Override
    public MultiColoredFontRenderer getFontRenderer()
    {
        //noinspection ConstantConditions
        return null;
    }

    @NotNull
    @Override
    public IResourceLoaderManager getResourceLoaderManager()
    {
        return SimpleResourceLoaderManager.getInstance();
    }

    @NotNull
    @Override
    public IStyleManager getStyleManager()
    {
        return SimpleFileBasedStyleManager.getInstance();
    }

    @NotNull
    @Override
    public ITemplateEngine getTemplateEngine()
    {
        return SimpleTemplateEngine.getInstance();
    }
}
