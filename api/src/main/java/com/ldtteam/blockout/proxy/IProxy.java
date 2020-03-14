package com.ldtteam.blockout.proxy;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.ldtteam.blockout.builder.IBuilderManager;
import com.ldtteam.blockout.compat.IClientTickManager;
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
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface IProxy
{
    static IProxy getInstance()
    {
        return ProxyHolder.getInstance();
    }

    void onCommonSetup();

    void onClientSetup();

    void onDedicatedServerSetup();

    @NotNull
    IBuilderManager getBuilderManager();

    @NotNull
    IGuiController getGuiController();

    @NotNull
    ILoaderManager getLoaderManager();

    @NotNull
    IDefinitionLoaderManager getDefinitionLoaderManager();

    @NotNull
    IUIElementFactoryController getFactoryController();

    @NotNull
    InputStream getResourceStream(@NotNull ResourceLocation location) throws Exception;

    @NotNull
    Vector2d getImageSize(@NotNull final ResourceLocation location);

    @NotNull
    INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key);

    @NotNull
    IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager);

    @NotNull
    World getDimensionFromDimensionId(@NotNull final int dimId);

    @NotNull
    IRenderManager generateNewRenderManager();

    @NotNull
    IFontRendererProxy getFontRenderer();

    @NotNull
    II18nProxy getI18nProxy();

    @NotNull
    IResourceLoaderManager getResourceLoaderManager();

    @NotNull
    IStyleManager getStyleManager();

    @NotNull
    ITemplateEngine getTemplateEngine();

    @NotNull
    IGuiController getClientSideOnlyGuiController();

    @NotNull
    IBindingEngine getBindingEngine();

    @NotNull
    Injector getInjector();

    @NotNull
    String convertToColorCode(@NotNull final String input);

    @NotNull
    Color convertToColor(@NotNull String color);

    @NotNull
    IReflectionManager getReflectionManager();

    @NotNull
    IBlockOutPluginRegistry getPluginRegistry();

    @NotNull
    IClientTickManager getClientTickManager();

    @NotNull
    INetworkingManager getNetworkingManager();

    void registerFactoryInjectionModule(@NotNull final Module factoryInjectionModule);

}
