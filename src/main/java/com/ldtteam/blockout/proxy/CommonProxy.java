package com.ldtteam.blockout.proxy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Module;
import com.ldtteam.blockout.connector.common.CommonFactoryController;
import com.ldtteam.blockout.connector.common.CommonLoaderManager;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.ILoaderManager;
import com.ldtteam.blockout.connector.core.IUIElementFactoryController;
import com.ldtteam.blockout.connector.server.ServerGuiController;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.binding.engine.SimpleBindingEngine;
import com.ldtteam.blockout.management.server.network.ServerNetworkManager;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.style.core.IStyleManager;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.ldtteam.blockout.style.simple.SimpleFileBasedStyleManager;
import com.ldtteam.blockout.style.simple.SimpleResourceLoaderManager;
import com.ldtteam.blockout.template.ITemplateEngine;
import com.ldtteam.blockout.template.SimpleTemplateEngine;
import com.ldtteam.blockout.util.SideHelper;
import com.ldtteam.blockout.util.color.MultiColoredFontRenderer;
import com.ldtteam.blockout.util.image.ImageUtil;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class CommonProxy implements IProxy
{
    private final ServerGuiController     controller;
    private final CommonLoaderManager     commonLoaderManager;
    private final CommonFactoryController commonFactoryController;
    private final Set<Module>             factoryInjectionModules = Sets.newHashSet();

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
        final String modId = location.getNamespace().toLowerCase();
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
    public World getWorldFromDimensionId(@NotNull final int dimId)
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
    public Set<Module> getFactoryInjectionModules()
    {
        return ImmutableSet.copyOf(factoryInjectionModules);
    }

    @Override
    public void registerFactoryInjectionModule(@NotNull final Module factoryInjectionModule)
    {
        factoryInjectionModules.add(factoryInjectionModule);
    }
}
