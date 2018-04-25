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
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{

    @NotNull
    private final ClientGuiController guiController;

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
    public IBlockAccess getBlockAccessFromDimensionId(@NotNull final int dimId)
    {
        return SideHelper.on(
          () -> Minecraft.getMinecraft().world,
          () -> super.getBlockAccessFromDimensionId(dimId)
        );
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public IRenderManager generateNewRenderManager()
    {
        return new RenderManager();
    }
}
