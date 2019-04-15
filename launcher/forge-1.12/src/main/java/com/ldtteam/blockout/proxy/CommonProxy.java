package com.ldtteam.blockout.proxy;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ldtteam.blockout.connector.common.CommonFactoryController;
import com.ldtteam.blockout.connector.common.CommonLoaderManager;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.ILoaderManager;
import com.ldtteam.blockout.connector.core.IUIElementFactoryController;
import com.ldtteam.blockout.connector.server.ServerGuiController;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.binding.engine.SimpleBindingEngine;
import com.ldtteam.blockout.loader.factory.modules.BaseFactoryInjectionModule;
import com.ldtteam.blockout.loader.factory.modules.ElementDataFactoryInjectionModule;
import com.ldtteam.blockout.loader.factory.modules.NBTFactoryInjectionModule;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.server.network.ServerNetworkManager;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.reflection.IReflectionManager;
import com.ldtteam.blockout.reflection.ReflectionManager;
import com.ldtteam.blockout.style.core.IStyleManager;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.ldtteam.blockout.style.simple.SimpleFileBasedStyleManager;
import com.ldtteam.blockout.style.simple.SimpleResourceLoaderManager;
import com.ldtteam.blockout.template.ITemplateEngine;
import com.ldtteam.blockout.template.SimpleTemplateEngine;
import com.ldtteam.blockout.util.SideHelper;
import com.ldtteam.blockout.util.image.ImageUtil;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.client.renderer.font.IFontRenderer;
import com.ldtteam.jvoxelizer.dimension.IDimension;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.dimension.Dimension;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Set;

public class CommonProxy implements IProxy
{
    private final ServerGuiController     controller;
    private final CommonLoaderManager     commonLoaderManager;
    private final CommonFactoryController commonFactoryController;
    private final Set<Module>             factoryInjectionModules = Sets.newHashSet();

    private Injector injector;

    public CommonProxy()
    {
        controller = new ServerGuiController();
        commonLoaderManager = new CommonLoaderManager();
        commonFactoryController = new CommonFactoryController();
    }

    @Override
    public void onPreInit()
    {
        factoryInjectionModules.add(new BaseFactoryInjectionModule());
        factoryInjectionModules.add(new ElementDataFactoryInjectionModule());
        factoryInjectionModules.add(new NBTFactoryInjectionModule());
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
    public InputStream getResourceStream(@NotNull final IIdentifier location) throws Exception
    {
        final String modId = location.getDomain().toLowerCase();
        String path = "assets/" + modId + "/" + location.getPath();

        final Object mod;
        if (modId.equalsIgnoreCase("minecraft"))
        {
            mod = SideHelper.on(() -> Minecraft.getMinecraft(), () -> FMLCommonHandler.instance().getMinecraftServerInstance());
        }
        else
        {
            mod = Loader.instance().getIndexedModList().get(modId).getMod();
        }

        final Package pack = mod.getClass().getPackage();
        if (pack != null)
        {
            final int packageDepth = pack.getName().split("\\.").length;
            for (int i = 0; i < packageDepth; i++)
            {
                path = "../" + path;
            }
        }

        return mod.getClass().getResourceAsStream(path);
    }

    @NotNull
    @Override
    public Vector2d getImageSize(@NotNull final IIdentifier location)
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
    public IDimension getDimensionFromDimensionId(@NotNull final int dimId)
    {
        return Dimension.fromForge(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimId));
    }

    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public IRenderManager generateNewRenderManager()
    {
        return null;
    }

    @Nullable
    @Override
    public IFontRenderer getFontRenderer()
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

    @NotNull
    @Override
    public IGuiController getClientSideOnlyGuiController()
    {
        return null;
    }

    @NotNull
    @Override
    public IBindingEngine getBindingEngine()
    {
        return SimpleBindingEngine.getInstance();
    }

    @NotNull
    @Override
    public Injector getInjector()
    {
        if (injector == null)
        {
            injector = Guice.createInjector(factoryInjectionModules);
        }

        return injector;
    }

    @NotNull
    @Override
    public String convertToColorCode(@NotNull final String input)
    {
        return input;
    }

    @NotNull
    @Override
    public IReflectionManager getReflectionManager()
    {
        return ReflectionManager.getInstance();
    }

    @Override
    public void registerFactoryInjectionModule(@NotNull final Module factoryInjectionModule)
    {
        factoryInjectionModules.add(factoryInjectionModule);
    }
}
