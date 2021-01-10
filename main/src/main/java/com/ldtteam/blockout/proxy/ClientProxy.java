package com.ldtteam.blockout.proxy;

import com.ldtteam.blockout.compat.CommonTickManager;
import com.ldtteam.blockout.compat.ITickManager;
import com.ldtteam.blockout.connector.client.ClientGuiController;
import com.ldtteam.blockout.connector.client.ClientSideOnlyGuiController;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.font.MultiColoredFontRenderer;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.client.network.ClientNetworkManager;
import com.ldtteam.blockout.management.client.render.RenderManager;
import com.ldtteam.blockout.management.client.update.NoOpUpdateManager;
import com.ldtteam.blockout.management.network.INetworkManager;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.management.server.network.ServerNetworkManager;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.util.color.ColorUtils;
import net.minecraftforge.fml.DistExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class ClientProxy extends CommonProxy {
    @NotNull
    private final ClientGuiController guiController;
    @NotNull
    private final ClientSideOnlyGuiController clientSideOnlyGuiController;
    @NotNull
    private FontRenderer multiColoredFontRenderer;
    @NotNull
    private final CommonTickManager clientSideTickManager;

    public ClientProxy()
    {
        guiController = new ClientGuiController();
        clientSideOnlyGuiController = new ClientSideOnlyGuiController();
        clientSideTickManager = new CommonTickManager();
    }

    @Override
    public void onClientSetup() {
        this.initializeFontRenderer();
    }

    @NotNull
    @Override
    public IGuiController getGuiController()
    {
        return DistExecutor.runForDist(
                () -> () -> guiController,
                () -> super::getGuiController
        );
    }

    @NotNull
    @Override
    public InputStream getResourceStream(@NotNull final ResourceLocation location) throws Exception
    {
        return Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
    }

    @NotNull
    @Override
    public INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key)
    {
        return DistExecutor.runForDist(
                () -> ClientNetworkManager::new,
                () -> () -> new ServerNetworkManager(key)
        );
    }

    @NotNull
    @Override
    public IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager)
    {
        return DistExecutor.runForDist(
                () -> NoOpUpdateManager::new,
                () -> () -> new ServerUpdateManager(manager)
        );
    }

    @NotNull
    @Override
    public World getDimensionFromDimensionId(@NotNull final int dimId)
    {
        return DistExecutor.runForDist(
                () -> () -> Minecraft.getInstance().world,
                () -> () -> super.getDimensionFromDimensionId(dimId)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IRenderManager generateNewRenderManager()
    {
        return new RenderManager();
    }

    private void initializeFontRenderer()
    {
        multiColoredFontRenderer = new MultiColoredFontRenderer(
                Minecraft.getInstance().getTextureManager(),
                Minecraft.getInstance().fontRenderer.font
        );
    }

    @NotNull
    @Override
    public IFontRendererProxy getFontRenderer()
    {
        return new IFontRendererProxy() {
            @Override
            public void drawSplitString(final String translatedContents, final int x, final int y, final int maxWidth, final int color) {
                multiColoredFontRenderer.drawSplitString(translatedContents, x, y, maxWidth, color);
            }

            @Override
            public String trimStringToWidth(final String stringToTrim, final int maxWidth, final boolean reverse) {
                return multiColoredFontRenderer.trimStringToWidth(stringToTrim, maxWidth, reverse);
            }

            @Override
            public int drawStringWithShadow(final String stringToDraw, final float x, final float y, final int colorRGB) {
                return multiColoredFontRenderer.drawStringWithShadow(stringToDraw, x, y, colorRGB);
            }

            @Override
            public int drawString(final String stringToDraw, final float x, final float y, final int colorRGB) {
                return multiColoredFontRenderer.drawString(stringToDraw, x, y, colorRGB);
            }

            @Override
            public int getFontHeight() {
                return multiColoredFontRenderer.FONT_HEIGHT;
            }

            @Override
            public int getStringWidth(final String string) {
                return multiColoredFontRenderer.getStringWidth(string);
            }
        };
    }

    @NotNull
    @Override
    public IGuiController getClientSideOnlyGuiController()
    {
        return DistExecutor.runForDist(
                () -> () -> clientSideOnlyGuiController,
                () -> () -> null
        );
    }

    @NotNull
    @Override
    public String convertToColorCode(@NotNull final String input)
    {
        return ColorUtils.convertToFontRendererColor(input);
    }

    @Override
    public ITickManager getTickManager() {
        return DistExecutor.runForDist(() -> () -> this.clientSideTickManager, () -> super::getTickManager);
    }
}
