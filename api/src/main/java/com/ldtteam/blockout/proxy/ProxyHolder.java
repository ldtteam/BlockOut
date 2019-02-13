package com.ldtteam.blockout.proxy;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.ILoaderManager;
import com.ldtteam.blockout.connector.core.IUIElementFactoryController;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.reflection.IReflectionManager;
import com.ldtteam.blockout.style.core.IStyleManager;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.ldtteam.blockout.template.ITemplateEngine;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class ProxyHolder implements IProxy
{
    private static ProxyHolder ourInstance = new ProxyHolder();
    private        IProxy      proxy;

    private ProxyHolder()
    {
    }

    public static ProxyHolder getInstance()
    {
        return ourInstance;
    }

    @Override
    public void onPreInit()
    {
        proxy.onPreInit();
    }

    @Override
    @NotNull
    public IGuiController getGuiController()
    {
        return proxy.getGuiController();
    }

    @Override
    @NotNull
    public ILoaderManager getLoaderManager()
    {
        return proxy.getLoaderManager();
    }

    @Override
    @NotNull
    public IUIElementFactoryController getFactoryController()
    {
        return proxy.getFactoryController();
    }

    @Override
    @NotNull
    public InputStream getResourceStream(@NotNull final ResourceLocation location) throws Exception
    {
        return proxy.getResourceStream(location);
    }

    @Override
    @NotNull
    public Vector2d getImageSize(@NotNull final ResourceLocation location)
    {
        return proxy.getImageSize(location);
    }

    @Override
    @NotNull
    public INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key)
    {
        return proxy.generateNewNetworkManagerForGui(key);
    }

    @Override
    @NotNull
    public IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager)
    {
        return proxy.generateNewUpdateManager(manager);
    }

    @Override
    @NotNull
    public World getWorldFromDimensionId(@NotNull final int dimId)
    {
        return proxy.getWorldFromDimensionId(dimId);
    }

    @Override
    @NotNull
    @SideOnly(Side.CLIENT)
    public IRenderManager generateNewRenderManager()
    {
        return proxy.generateNewRenderManager();
    }

    @Override
    public void initializeFontRenderer()
    {
        proxy.initializeFontRenderer();
    }

    @Override
    @Nullable
    public MultiColoredFontRenderer getFontRenderer()
    {
        return proxy.getFontRenderer();
    }

    @Override
    @NotNull
    public IResourceLoaderManager getResourceLoaderManager()
    {
        return proxy.getResourceLoaderManager();
    }

    @Override
    @NotNull
    public IStyleManager getStyleManager()
    {
        return proxy.getStyleManager();
    }

    @Override
    @NotNull
    public ITemplateEngine getTemplateEngine()
    {
        return proxy.getTemplateEngine();
    }

    @Override
    @NotNull
    public IGuiController getClientSideOnlyGuiController()
    {
        return proxy.getClientSideOnlyGuiController();
    }

    @Override
    @NotNull
    public IBindingEngine getBindingEngine()
    {
        return proxy.getBindingEngine();
    }

    @NotNull
    @Override
    public Injector getInjector()
    {
        return proxy.getInjector();
    }

    @NotNull
    @Override
    public String convertToColorCode(@NotNull final String input)
    {
        return proxy.convertToColorCode(input);
    }

    @NotNull
    @Override
    public IReflectionManager getReflectionManager()
    {
        return proxy.getReflectionManager();
    }

    @Override
    public void registerFactoryInjectionModule(@NotNull final Module factoryInjectionModule)
    {
        proxy.registerFactoryInjectionModule(factoryInjectionModule);
    }

    public Boolean isReady()
    {
        return proxy != null;
    }

    public void setProxy(final IProxy proxy)
    {
        this.proxy = proxy;
    }
}
