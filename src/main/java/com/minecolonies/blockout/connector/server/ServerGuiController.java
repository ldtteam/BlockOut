package com.minecolonies.blockout.connector.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.minecolonies.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.connector.core.builder.IGuiKeyBuilder;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.event.injector.EventHandlerInjector;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.management.UIManager;
import com.minecolonies.blockout.network.NetworkManager;
import com.minecolonies.blockout.network.message.CloseGuiCommandMessage;
import com.minecolonies.blockout.network.message.OpenGuiCommandMessage;
import com.minecolonies.blockout.util.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ServerGuiController implements IGuiController
{

    private final Map<IGuiKey, RootGuiElement> openUis        = new HashMap<>();
    private final Map<IGuiKey, List<UUID>>     watchers       = new HashMap<>();
    private final Map<UUID, IGuiKey>           playerWatching = new HashMap<>();

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
        closeUI(playerId);

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

        RootGuiElement host;

        if (!openUis.containsKey(key))
        {
            final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(elementData);
            if (!(element instanceof RootGuiElement))
            {
                Log.getLogger().error("Failed to open UI for: " + playerId.toString() + " from: " + key.toString() + ". Component is not a Host.");
                return;
            }

            host = (RootGuiElement) element;

            DependencyObjectInjector.inject(host, key.getConstructionData());
            host.getAllCombinedChildElements().values().forEach(c -> DependencyObjectInjector.inject(c, key.getConstructionData()));

            EventHandlerInjector.inject(host, key.getConstructionData());
            host.getAllCombinedChildElements().values().forEach(c -> EventHandlerInjector.inject(c, key.getConstructionData()));

            host.setUiManager(new UIManager(host, key));

            openUis.put(key, host);
            host.getUiManager().getUpdateManager().updateElement(host);
        }
        else
        {
            host = openUis.get(key);
        }

        watchers.putIfAbsent(key, new ArrayList<>());
        watchers.get(key).add(playerId);
        playerWatching.put(playerId, key);

        openGui(key, host, player);
    }

    @Override
    public void closeUI(@NotNull final EntityPlayer player)
    {
        closeUI(player.getUniqueID());
    }

    @Override
    public void closeUI(@NotNull final UUID playerId)
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
            playerWatching.remove(playerId);

            if (watchers.get(currentlyWatching).isEmpty())
            {
                watchers.remove(currentlyWatching);
                openUis.remove(currentlyWatching);
            }
        }

        try
        {
            NetworkManager.sendTo(new CloseGuiCommandMessage(), player);
        }
        catch (Exception ex)
        {
            Log.getLogger().info("Could not send close message. Player might have logged out when UI was open.");
        }
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final EntityPlayer player)
    {
        return getOpenUI(player.getUniqueID());
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final UUID player)
    {
        return playerWatching.get(player);
    }

    @Nullable
    @Override
    public RootGuiElement getRoot(@NotNull final IGuiKey guiKey)
    {
        return openUis.get(guiKey);
    }

    private void openGui(@NotNull final IGuiKey key, @NotNull final RootGuiElement rootGuiElement, @NotNull final EntityPlayerMP playerMP)
    {
        playerMP.getNextWindowId();
        playerMP.closeContainer();

        NetworkManager.sendTo(new OpenGuiCommandMessage(key, BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(rootGuiElement), playerMP.currentWindowId),
          playerMP);

        playerMP.openContainer = new BlockOutContainer(key, openUis.get(key), playerMP.currentWindowId);
        playerMP.openContainer.addListener(playerMP);

        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
    }

    /**
     * Returns a list of all UUIDs that are watching this UI.
     *
     * @param key The key for the gui.
     * @return The watching players.
     */
    public ImmutableList<UUID> getUUIDsOfPlayersWatching(@NotNull final IGuiKey key)
    {
        return ImmutableList.copyOf(watchers.get(key));
    }

    public ImmutableMap<IGuiKey, RootGuiElement> getOpenUis()
    {
        return ImmutableMap.copyOf(openUis);
    }

    public ImmutableList<RootGuiElement> getOpenRoots()
    {
        return ImmutableList.copyOf(openUis.values());
    }
}
