package com.ldtteam.blockout.proxy;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.ldtteam.blockout.builder.IBuilderManager;
import com.ldtteam.blockout.compat.ITickManager;
import com.ldtteam.blockout.connector.core.*;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.network.INetworkingManager;
import com.ldtteam.blockout.plugins.IBlockOutPluginRegistry;
import com.ldtteam.blockout.reflection.IReflectionManager;
import com.ldtteam.blockout.style.core.IStyleManager;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.ldtteam.blockout.template.ITemplateEngine;
import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

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
    public void onCommonSetup()
    {
        proxy.onCommonSetup();
    }

    @Override
    public void onClientSetup() {
        proxy.onClientSetup();
    }

    @Override
    public void onDedicatedServerSetup() {
        proxy.onDedicatedServerSetup();
    }

    @NotNull
    @Override
    public IBuilderManager getBuilderManager() {
        return proxy.getBuilderManager();
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

    @NotNull
    @Override
    public IDefinitionLoaderManager getDefinitionLoaderManager() {
        return proxy.getDefinitionLoaderManager();
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
    public World getDimensionFromDimensionId(@NotNull final RegistryKey<World> dimId)
    {
        return proxy.getDimensionFromDimensionId(dimId);
    }

    @Override
    @NotNull
    public IRenderManager generateNewRenderManager()
    {
        return proxy.generateNewRenderManager();
    }

    @Override
    @NotNull
    public IFontRendererProxy getFontRenderer()
    {
        return proxy.getFontRenderer();
    }

    @NotNull
    @Override
    public II18nProxy getI18nProxy()
    {
        return proxy.getI18nProxy();
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

    @Override
    @NotNull
    public Color convertToColor(@NotNull final String color)
    {
        return proxy.convertToColor(color);
    }

    @NotNull
    @Override
    public IReflectionManager getReflectionManager()
    {
        return proxy.getReflectionManager();
    }

    @NotNull
    @Override
    public IBlockOutPluginRegistry getPluginRegistry() {
        return proxy.getPluginRegistry();
    }

    @Override
    public void registerFactoryInjectionModule(@NotNull final Module factoryInjectionModule)
    {
        proxy.registerFactoryInjectionModule(factoryInjectionModule);
    }

    @NotNull
    @Override
    public ITickManager getTickManager() {
        return proxy.getTickManager();
    }

    @NotNull
    @Override
    public INetworkingManager getNetworkingManager() {
        return proxy.getNetworkingManager();
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
