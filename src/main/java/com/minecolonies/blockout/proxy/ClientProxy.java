package com.minecolonies.blockout.proxy;

import com.minecolonies.blockout.connector.client.ClientGuiController;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.management.client.network.ClientNetworkManager;
import com.minecolonies.blockout.management.client.render.RenderManager;
import com.minecolonies.blockout.management.client.update.NoOpUpdateManager;
import com.minecolonies.blockout.management.server.network.ServerNetworkManager;
import com.minecolonies.blockout.management.server.update.ServerUpdateManager;
import com.minecolonies.blockout.util.SideHelper;
import com.minecolonies.blockout.util.color.MultiColoredFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{

    @NotNull
    private       MultiColoredFontRenderer multiColoredFontRenderer;
    @NotNull
    private final ClientGuiController      guiController;

    public ClientProxy() {guiController = new ClientGuiController();}

    @NotNull
    @Override
    public IGuiController getGuiController()
    {
        return SideHelper.on(
          () -> guiController,
          super::getGuiController
        );
    }

    @NotNull
    @Override
    public InputStream getResourceStream(@NotNull final ResourceLocation location) throws Exception
    {
        return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
    }

    @NotNull
    @Override
    public INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key)
    {
        return SideHelper.on(
          ClientNetworkManager::new,
          () -> new ServerNetworkManager(key)
        );
    }

    @NotNull
    @Override
    public IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager)
    {
        return SideHelper.on(
          NoOpUpdateManager::new,
          () -> new ServerUpdateManager(manager)
        );
    }

    @NotNull
    @Override
    public World getWorldFromDimensionId(@NotNull final int dimId)
    {
        return SideHelper.on(
          () -> Minecraft.getMinecraft().world,
          () -> super.getWorldFromDimensionId(dimId)
        );
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public IRenderManager generateNewRenderManager()
    {
        return new RenderManager();
    }

    @Override
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
    }

    @NotNull
    @Override
    public MultiColoredFontRenderer getFontRenderer()
    {
        return multiColoredFontRenderer;
    }
}
