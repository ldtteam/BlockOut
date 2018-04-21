package com.minecolonies.blockout.compat;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.inventory.BlockOutContainer;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeFMLEventHandler
{
    @SubscribeEvent
    public void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            EntityPlayerMP playerMP = (EntityPlayerMP) event.player;

            BlockOut.getBlockOut().getProxy().getGuiController().closeUI(playerMP);
        }
    }

    @SubscribeEvent
    public void onTickPlayerTick(final TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (event.player.openContainer instanceof BlockOutContainer)
            {
                BlockOutContainer openContainer = (BlockOutContainer) event.player.openContainer;
                openContainer.reinitializeSlots();
            }
        }
    }
}
