package com.minecolonies.blockout.connector.client;

import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.network.message.CloseGuiRequestMessage;
import com.minecolonies.blockout.network.message.OpenGuiRequestMessage;
import com.minecolonies.blockout.util.Log;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClientGuiController implements IGuiController
{
    @Override
    public void openUI(@NotNull final IGuiKey key, @NotNull final UUID playerId)
    {
        if (!playerId.equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            Log.getLogger().warn("Cannot open UI of other player.");
            return;
        }

        NetworkManager.sendToServer(new OpenGuiRequestMessage());
    }

    @Override
    public void onUiClosed(@NotNull final UUID playerId)
    {
        if (!playerId.equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            Log.getLogger().warn("Cannot close UI of other player.");
            return;
        }

        NetworkManager.sendToServer(new CloseGuiRequestMessage());
    }
}
