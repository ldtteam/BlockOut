package com.minecolonies.blockout.proxy;

import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.connector.core.IUIElementFactoryController;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.style.core.IStyleManager;
import com.minecolonies.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.minecolonies.blockout.template.ITemplateEngine;
import com.minecolonies.blockout.util.color.MultiColoredFontRenderer;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface IProxy
{
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
    World getWorldFromDimensionId(@NotNull final int dimId);

    @SideOnly(Side.CLIENT)
    @NotNull
    IRenderManager generateNewRenderManager();

    void initializeFontRenderer();

    @NotNull
    MultiColoredFontRenderer getFontRenderer();

    @NotNull
    IResourceLoaderManager getResourceLoaderManager();

    @NotNull
    IStyleManager getStyleManager();

    @NotNull
    ITemplateEngine getTemplateEngine();
}
