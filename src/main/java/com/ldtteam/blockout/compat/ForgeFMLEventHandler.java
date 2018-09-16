package com.ldtteam.blockout.compat;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.server.ServerGuiController;
import com.ldtteam.blockout.gui.IBlockOutGui;
import com.ldtteam.blockout.inventory.BlockOutContainer;
import com.ldtteam.blockout.management.server.update.ServerUpdateManager;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.SideHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeFMLEventHandler
{
    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            EntityPlayerMP playerMP = (EntityPlayerMP) event.player;

            BlockOut.getBlockOut().getProxy().getGuiController().closeUI(playerMP);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onTickClientTick(final TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        SideHelper.onClient(() -> {
            if (Minecraft.getMinecraft().currentScreen instanceof IBlockOutGui)
            {
                IBlockOutGui currentScreen = (IBlockOutGui) Minecraft.getMinecraft().currentScreen;
                currentScreen.getRoot().getUiManager().getUpdateManager().updateElement(currentScreen.getRoot());

                ClientTickManager.getInstance().onClientTick();
            }
        });
    }

    private static Thread updateThread = null;

    @SubscribeEvent
    public static void onTickServerTick(final TickEvent.ServerTickEvent event)
    {
        SideHelper.onServer(() -> {
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
                        Log.getLogger().warn("Update of BO UI took too long: " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms." +e.getKey().getGuiContext().toString());
                        Log.getLogger().warn("#################################################");
                    }

                    if (updateManager.isDirty())
                    {
                        updateManager.onNetworkTick();
                        guiController.getUUIDsOfPlayersWatching(e.getKey()).forEach(uuid -> {
                            final Container blockOutCandidate = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid).openContainer;
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
