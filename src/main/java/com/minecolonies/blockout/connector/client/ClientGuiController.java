package com.minecolonies.blockout.connector.client;

import com.minecolonies.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.core.builder.IGuiKeyBuilder;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.network.message.CloseGuiRequestMessage;
import com.minecolonies.blockout.network.message.OpenGuiRequestMessage;
import com.minecolonies.blockout.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class ClientGuiController implements IGuiController
{
    @Override
    public void openUI(
      @NotNull final EntityPlayer player, @NotNull final Consumer<IGuiKeyBuilder> guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        guiKeyBuilderConsumer.accept(builder);

        openUI(player, builder.build());
    }

    @Override
    public void openUI(@NotNull final EntityPlayer player, @NotNull final IGuiKey key)
    {
        openUI(player.getUniqueID(), key);
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder> guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        guiKeyBuilderConsumer.accept(builder);

        openUI(playerId, builder.build());
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final IGuiKey key)
    {
        if (!playerId.equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            Log.getLogger().warn("Cannot open UI of other player.");
            return;
        }

        NetworkManager.sendToServer(new OpenGuiRequestMessage());
    }

    @Override
    public void closeUI(@NotNull final EntityPlayer player)
    {
        closeUI(player.getUniqueID());
    }

    @Override
    public void closeUI(@NotNull final UUID playerId)
    {
        if (!playerId.equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            Log.getLogger().warn("Cannot close UI of other player.");
            return;
        }

        NetworkManager.sendToServer(new CloseGuiRequestMessage());
    }
}
