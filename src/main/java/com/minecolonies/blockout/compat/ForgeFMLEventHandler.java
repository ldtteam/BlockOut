package com.minecolonies.blockout.compat;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.server.ServerGuiController;
import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.management.server.update.ServerUpdateManager;
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.SideHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        SideHelper.onClient(() -> {
            if (Minecraft.getMinecraft().currentScreen instanceof BlockOutGui)
            {
                BlockOutGui currentScreen = (BlockOutGui) Minecraft.getMinecraft().currentScreen;
                currentScreen.getRoot().getUiManager().getUpdateManager().updateElement(currentScreen.getRoot());

                ClientTickManager.getInstance().onClientTick();
            }
        });
    }

    @SubscribeEvent
    public static void onTickServerTick(final TickEvent.ServerTickEvent event)
    {
        SideHelper.onServer(() -> {
            if (BlockOut.getBlockOut().getProxy().getGuiController() instanceof ServerGuiController)
            {
                ServerGuiController guiController = (ServerGuiController) BlockOut.getBlockOut().getProxy().getGuiController();

                guiController.getOpenRoots().forEach(r -> {
                    if (r.getUiManager().getUpdateManager() instanceof ServerUpdateManager)
                    {
                        ServerUpdateManager updateManager = (ServerUpdateManager) r.getUiManager().getUpdateManager();
                        updateManager.updateElement(r);
                        updateManager.onNetworkTick();
                    }
                });
            }
        });
    }
}
