package com.minecolonies.blockout.connector.server;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.network.message.CloseGuiCommandMessage;
import com.minecolonies.blockout.network.message.OpenGuiCommandMessage;
import com.minecolonies.blockout.util.Log;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ServerGuiController implements IGuiController
{

    private final Map<IGuiKey, IUIElementHost> openUis        = new HashMap<>();
    private final Map<IGuiKey, List<UUID>>     watchers       = new HashMap<>();
    private final Map<UUID, IGuiKey>           playerWatching = new HashMap<>();

    @Override
    public void openUI(@NotNull final IGuiKey key, @NotNull final UUID playerId)
    {
        onUiClosed(playerId);

        final EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);
        if (player == null)
        {
            Log.getLogger().warn("Failed to open UI for: " + playerId.toString() + ". Could not Identify player.");
            return;
        }

        if (player instanceof FakePlayer)
        {
            //NOOP Return.
            return;
        }

        final IUIElementData elementData = BlockOut.getBlockOut().getProxy().getLoaderManager().loadData(key.getGuiDefinitionLoader());

        if (elementData == null)
        {
            Log.getLogger().error("Failed to open UI for: " + playerId.toString() + " from: " + key.toString() + ". No loader compatible.");
            return;
        }

        if (!openUis.containsKey(key))
        {
            final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(elementData);
            if (!(element instanceof IUIElementHost))
            {
                Log.getLogger().error("Failed to open UI for: " + playerId.toString() + " from: " + key.toString() + ". Component is not a Host.");
                return;
            }

            final IUIElementHost host = (IUIElementHost) element;
            openUis.put(key, host);
        }

        watchers.putIfAbsent(key, new ArrayList<>()).add(playerId);
        playerWatching.put(playerId, key);

        openGui(key, elementData, player);
    }

    @Override
    public void onUiClosed(@NotNull final UUID playerId)
    {
        final EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);
        if (player == null)
        {
            Log.getLogger().warn("Failed to close UI for: " + playerId.toString() + ". Could not Identify player.");
            return;
        }

        if (player instanceof FakePlayer)
        {
            //NOOP Return.
            return;
        }

        if (playerWatching.containsKey(playerId))
        {
            final IGuiKey currentlyWatching = playerWatching.get(playerId);
            watchers.get(currentlyWatching).remove(playerId);
            if (watchers.get(currentlyWatching).isEmpty())
            {
                watchers.remove(currentlyWatching);
                openUis.remove(currentlyWatching);
            }
        }

        NetworkManager.sendTo(new CloseGuiCommandMessage(), player);
    }

    private void openGui(@NotNull final IGuiKey key, @NotNull final IUIElementData data, @NotNull final EntityPlayerMP playerMP)
    {
        playerMP.getNextWindowId();
        playerMP.closeContainer();
        playerMP.openContainer = new BlockOutContainer(key, openUis.get(key));
        playerMP.openContainer.windowId = playerMP.currentWindowId;
        playerMP.openContainer.addListener(playerMP);

        NetworkManager.sendTo(new OpenGuiCommandMessage(key, data), playerMP);

        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
    }
}
