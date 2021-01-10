package com.ldtteam.blockout.compat;

import com.ldtteam.blockout.connector.server.ServerGuiController;
import com.ldtteam.blockout.gui.BlockOutContainerGui;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import net.minecraftforge.fml.DistExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UpdateHandler
{
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.getPlayer() instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity playerMP = (ServerPlayerEntity) event.getPlayer();

            ProxyHolder.getInstance().getGuiController().closeUI(playerMP);
        }
    }

    public static void onTickClientTick(final TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
        {
            return;
        }

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().currentScreen instanceof BlockOutContainerGui)
            {
                final BlockOutContainerGui blockOutGui = (BlockOutContainerGui) Minecraft.getInstance().currentScreen;
                blockOutGui.getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(blockOutGui.getInstanceData().getRoot());

                IProxy.getInstance().getTickManager().onTick();
            }
        });
    }

    public static void onTickServerTick(final TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
        {
            return;
        }

        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            ServerGuiController guiController = (ServerGuiController) ProxyHolder.getInstance().getGuiController();

            guiController.getOpenUis().forEach((key, value) -> {
                if (value.getUiManager().getUpdateManager() instanceof ServerUpdateManager) {
                    ServerUpdateManager updateManager = (ServerUpdateManager) value.getUiManager().getUpdateManager();

                    final StopWatch stopWatch = StopWatch.createStarted();
                    updateManager.updateElement(value);
                    stopWatch.stop();

                    if (stopWatch.getTime(TimeUnit.MILLISECONDS) > 5 && false) {
                        Log.getLogger().warn("#################################################");
                        Log.getLogger().warn("Update of BO UI took too long: " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms." + key.getGuiContext().toString());
                        Log.getLogger().warn("#################################################");
                    }

                    if (updateManager.isDirty()) {
                        updateManager.onNetworkTick();
                        guiController.getUUIDsOfPlayersWatching(key).forEach(uuid -> {
                            final Container blockOutCandidate = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid)).openContainer;
                            if (blockOutCandidate instanceof BlockOutContainer) {
                                final BlockOutContainer blockOutContainer = (BlockOutContainer) blockOutCandidate;
                                blockOutContainer.reinitializeSlots();
                            } else {
                                Log.getLogger()
                                        .error("Can not reinitialize slots. Container is not owned by BlockOut.",
                                                new IllegalStateException("Unknown container type: " + blockOutCandidate.getClass().toString()));
                            }
                        });
                    }
                }
            });

            IProxy.getInstance().getTickManager().onTick();
        });
    }
}
