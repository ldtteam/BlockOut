package com.ldtteam.blockout.connector.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ldtteam.blockout.connector.common.CommonGuiInstantiationController;
import com.ldtteam.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.object.ObjectUIElementData;
import com.ldtteam.blockout.network.message.CloseGuiCommandMessage;
import com.ldtteam.blockout.network.message.OpenGuiCommandMessage;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ServerGuiController implements IGuiController
{

    private final Map<IGuiKey, IRootGuiElement> openUis        = new HashMap<>();
    private final Map<IGuiKey, List<UUID>>     watchers       = new HashMap<>();
    private final Map<UUID, IGuiKey>           playerWatching = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void openUI(
      @NotNull final PlayerEntity player, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(player, builder.build());
    }

    @Override
    public void openUI(@NotNull final PlayerEntity player, @NotNull final IGuiKey key)
    {
        openUI(player.getUniqueID(), key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(playerId, builder.build());
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final IGuiKey key)
    {
        closeUI(playerId);

        final ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(playerId);
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

        IRootGuiElement host;

        if (!openUis.containsKey(key))
        {
            try
            {
                host = CommonGuiInstantiationController.getInstance().instantiateNewGui(key);
            }
            catch (IllegalArgumentException ex)
            {
                Log.getLogger().error("Failed to build guitemp for: " + playerId, ex);
                return;
            }

            openUis.put(key, host);
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
    public void closeUI(@NotNull final PlayerEntity player)
    {
        closeUI(player.getUniqueID());
    }

    @Override
    public void closeUI(@NotNull final UUID playerId)
    {
        final ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(playerId);
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
            IProxy.getInstance().getNetworkingManager().sendTo(new CloseGuiCommandMessage(), player);
        }
        catch (Exception ex)
        {
            Log.getLogger().info("Could not send close message. Player might have logged out when UI was open.");
        }
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final PlayerEntity player)
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
    public IRootGuiElement getRoot(@NotNull final IGuiKey guiKey)
    {
        return openUis.get(guiKey);
    }

    private void openGui(@NotNull final IGuiKey key, @NotNull final IRootGuiElement rootGuiElement, @NotNull final ServerPlayerEntity playerMP)
    {
        playerMP.getNextWindowId();
        playerMP.closeScreen();

        final IUIElementData<?> dataCandidate = ProxyHolder.getInstance().getFactoryController().getDataFromElement(rootGuiElement);
        if (dataCandidate instanceof ObjectUIElementData)
        {
            IProxy.getInstance().getNetworkingManager().sendTo(new OpenGuiCommandMessage(key, (ObjectUIElementData) dataCandidate, playerMP.currentWindowId),
                    playerMP);
        }
        else
        {
            throw new IllegalArgumentException("Cannot serialize given element into a serializable form of data");
        }


        playerMP.openContainer = new BlockOutContainer(key, openUis.get(key), playerMP.currentWindowId);
        playerMP.openContainer.addListener(playerMP);


        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
    }

    /**
     * Returns a list of all UUIDs that are watching this UI.
     *
     * @param key The key for the guitemp.
     * @return The watching players.
     */
    public ImmutableList<UUID> getUUIDsOfPlayersWatching(@NotNull final IGuiKey key)
    {
        if (watchers.get(key) == null)
        {
            return ImmutableList.of();
        }

        return ImmutableList.copyOf(watchers.get(key));
    }

    public ImmutableMap<IGuiKey, IRootGuiElement> getOpenUis()
    {
        return ImmutableMap.copyOf(openUis);
    }

    public ImmutableList<IRootGuiElement> getOpenRoots()
    {
        return ImmutableList.copyOf(openUis.values());
    }
}
