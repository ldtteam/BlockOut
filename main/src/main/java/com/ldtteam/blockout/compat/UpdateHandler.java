package com.ldtteam.blockout.compat;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.server.ServerGuiController;
import com.ldtteam.blockout.gui.BlockOutGuiData;
import com.ldtteam.blockout.inventory.BlockOutContainerData;
import com.ldtteam.blockout.inventory.BlockOutContainerLogic;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.IGameEngine;
import com.ldtteam.jvoxelizer.client.gui.IGuiContainer;
import com.ldtteam.jvoxelizer.common.gameevent.event.player.IPlayerEvent;
import com.ldtteam.jvoxelizer.common.gameevent.event.ITickEvent;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
import com.ldtteam.jvoxelizer.inventory.IContainer;
import com.ldtteam.jvoxelizer.util.distribution.executor.IDistributionExecutor;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

public class UpdateHandler
{
    private static Thread updateThread = null;

    public static void onPlayerLoggedOut(final IPlayerEvent.IPlayerLoggedOutEvent event)
    {
        if (event.getPlayer() instanceof IMultiplayerPlayerEntity)
        {
            IMultiplayerPlayerEntity playerMP = (IMultiplayerPlayerEntity) event.getPlayer();

            BlockOut.getBlockOut().getProxy().getGuiController().closeUI(playerMP);
        }
    }

    public static void onTickClientTick(final ITickEvent.IClientTickEvent event)
    {
        if (event.getPhase() != ITickEvent.Phase.END)
        {
            return;
        }

        IDistributionExecutor.onClient(() -> {
            if (IGameEngine.getInstance().getCurrentGui() instanceof IGuiContainer && IGameEngine.getInstance().getCurrentGui().getInstanceData() instanceof BlockOutGuiData)
            {
                IGuiContainer<BlockOutGuiData> currentScreen = (IGuiContainer<BlockOutGuiData>) IGameEngine.getInstance().getCurrentGui();
                currentScreen.getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(currentScreen.getInstanceData().getRoot());

                ClientTickManager.getInstance().onClientTick();
            }
        });
    }

    public static void onTickServerTick(final ITickEvent.IServerTickEvent event)
    {
        IDistributionExecutor.onServer(() -> {
            ServerGuiController guiController = (ServerGuiController) BlockOut.getBlockOut().getProxy().getGuiController();

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
                            final IContainer<?> blockOutCandidate = IGameEngine.getInstance().getCurrentServerInstance().getPlayerManager().getById(uuid).getOpenContainer();
                            if (blockOutCandidate.getInstanceData() instanceof BlockOutContainerData)
                            {
                                final IContainer<BlockOutContainerData> blockOutContainer = (IContainer<BlockOutContainerData>) blockOutCandidate;
                                BlockOutContainerLogic.reinitializeSlots(blockOutContainer);
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
