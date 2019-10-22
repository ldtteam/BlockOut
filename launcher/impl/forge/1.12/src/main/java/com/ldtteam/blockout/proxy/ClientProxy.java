package com.ldtteam.blockout.proxy;

import com.ldtteam.blockout.client.util.color.ColorProvider;
import com.ldtteam.blockout.connector.client.ClientGuiController;
import com.ldtteam.blockout.connector.client.ClientSideOnlyGuiController;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.client.network.ClientNetworkManager;
import com.ldtteam.blockout.management.client.render.RenderManager;
import com.ldtteam.blockout.management.client.update.NoOpUpdateManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.server.network.ServerNetworkManager;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.util.SideHelper;
import com.ldtteam.blockout.util.color.ColorUtils;
import com.ldtteam.blockout.util.color.IColor;
import com.ldtteam.jvoxelizer.client.renderer.font.IFontRenderer;
import com.ldtteam.jvoxelizer.core.provider.holder.ProviderResolver;
import com.ldtteam.jvoxelizer.dimension.IDimension;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.client.renderer.font.FontRenderer;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.dimension.Dimension;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.util.identifier.Identifier;
import com.ldtteam.jvoxelizer.util.distribution.executor.IDistributionExecutor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class ClientProxy extends CommonProxy
{

    @NotNull
    private final ClientGuiController         guiController;
    @NotNull
    private final ClientSideOnlyGuiController clientSideOnlyGuiController;
    @NotNull
    private       IFontRenderer               multiColoredFontRenderer;

    public ClientProxy()
    {
        guiController = new ClientGuiController();
        clientSideOnlyGuiController = new ClientSideOnlyGuiController();
    }

    @Override
    public void onPreInit()
    {
        super.onPreInit();
        ProviderResolver.getInstance().registerProvider(IColor.class, ColorProvider.getInstance());
    }

    @NotNull
    @Override
    public IGuiController getGuiController()
    {
        return IDistributionExecutor.on(
          () -> guiController,
          super::getGuiController
        );
    }

    @NotNull
    @Override
    public InputStream getResourceStream(@NotNull final ResourceLocation location) throws Exception
    {
        return Minecraft.getMinecraft().getResourceManager().getResource(Identifier.asForge(location)).getInputStream();
    }

    @NotNull
    @Override
    public INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key)
    {
        return IDistributionExecutor.on(
          ClientNetworkManager::new,
          () -> new ServerNetworkManager(key)
        );
    }

    @NotNull
    @Override
    public IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager)
    {
        return IDistributionExecutor.on(
          NoOpUpdateManager::new,
          () -> new ServerUpdateManager(manager)
        );
    }

    @NotNull
    @Override
    public IDimension getDimensionFromDimensionId(@NotNull final int dimId)
    {
        return SideHelper.on(
          () -> Dimension.fromForge(Minecraft.getMinecraft().world),
          () -> super.getDimensionFromDimensionId(dimId)
        );
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public IRenderManager generateNewRenderManager()
    {
        return new RenderManager();
    }

    /*@Override
    public void initializeFontRenderer()
    {
        multiColoredFontRenderer =
          new MultiColoredFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine);
        if (Minecraft.getMinecraft().gameSettings.language != null)
        {
            multiColoredFontRenderer.setUnicodeFlag(
              Minecraft.getMinecraft().getLanguageManager().isCurrentLocaleUnicode() || Minecraft.getMinecraft().gameSettings.forceUnicodeFont);
            multiColoredFontRenderer.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
        }
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(multiColoredFontRenderer);
    }*/

    @NotNull
    @Override
    public IFontRenderer getFontRenderer()
    {
        return FontRenderer.fromForge(Minecraft.getMinecraft().fontRenderer);
    }

    @NotNull
    @Override
    public IGuiController getClientSideOnlyGuiController()
    {
        return SideHelper.on(
          () -> clientSideOnlyGuiController,
          () -> null
        );
    }

    @NotNull
    @Override
    public String convertToColorCode(@NotNull final String input)
    {
        return ColorUtils.convertToFontRendererColor(input);
    }
}
