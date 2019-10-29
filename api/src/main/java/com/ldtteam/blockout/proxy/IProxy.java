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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
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

    void onPreInit();

    @NotNull
    IGuiController getGuiController();

    @NotNull
    ILoaderManager getLoaderManager();

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
    IReflectionManager getReflectionManager();

    void registerFactoryInjectionModule(@NotNull final Module factoryInjectionModule);

}
