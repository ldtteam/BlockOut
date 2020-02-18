package com.ldtteam.blockout.compat;

import com.ldtteam.blockout.connector.server.ServerGuiController;
import com.ldtteam.blockout.gui.BlockOutContainerGui;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.side.SideExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

public class UpdateHandler
{
    private static Thread updateThread = null;

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

        SideExecutor.runWhenOn(LogicalSide.CLIENT, () -> () -> {
            if (Minecraft.getInstance().currentScreen instanceof BlockOutContainerGui)
            {
                final BlockOutContainerGui blockOutGui = (BlockOutContainerGui) Minecraft.getInstance().currentScreen;
                blockOutGui.getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(blockOutGui.getInstanceData().getRoot());

                ClientTickManager.getInstance().onClientTick();
            }
        });
    }

    public static void onTickServerTick(final TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
        {
            return;
        }

        SideExecutor.runWhenOn(LogicalSide.SERVER, () -> () -> {
            ServerGuiController guiController = (ServerGuiController) ProxyHolder.getInstance().getGuiController();

            guiController.getOpenUis().entrySet().forEach(e -> {
                if (e.getValue().getUiManager().getUpdateManager() instanceof ServerUpdateManager)
                {
                    ServerUpdateManager updateManager = (ServerUpdateManager) e.getValue().getUiManager().getUpdateManager();

                    final StopWatch stopWatch = StopWatch.createStarted();
                    updateManager.updateElement(e.getValue());
                    stopWatch.stop();

                    if (stopWatch.getTime(TimeUnit.MILLISECONDS) > 5 && false)
                    {
                        Log.getLogger().warn("#################################################");
                        Log.getLogger().warn("Update of BO UI took too long: " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms." + e.getKey().getGuiContext().toString());
                        Log.getLogger().warn("#################################################");
                    }

                    if (updateManager.isDirty())
                    {
                        updateManager.onNetworkTick();
                        guiController.getUUIDsOfPlayersWatching(e.getKey()).forEach(uuid -> {
                            final Container blockOutCandidate = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid).openContainer;
                            if (blockOutCandidate instanceof BlockOutContainer)
                            {
                                final BlockOutContainer blockOutContainer = (BlockOutContainer) blockOutCandidate;
                                blockOutContainer.reinitializeSlots();
                            }
                            else
                            {
                                Log.getLogger()
                                  .error("Can not reinitialize slots. Container is not owned by BlockOut.",
                                    new IllegalStateException("Unknown container type: " + blockOutCandidate.getClass().toString()));
                            }
                        });
                    }
                }
            });
        });
    }
}
