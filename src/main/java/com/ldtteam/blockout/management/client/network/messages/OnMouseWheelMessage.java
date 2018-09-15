package com.ldtteam.blockout.management.client.network.messages;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.util.Log;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class OnMouseWheelMessage implements IBlockOutClientToServerMessage
{
    @NotNull
    private final int localX;
    @NotNull
    private final int localY;
    @NotNull
    private final int deltaWheel;

    public OnMouseWheelMessage(@NotNull final int localX, @NotNull final int localY, @NotNull final int deltaWheel)
    {
        this.localX = localX;
        this.localY = localY;
        this.deltaWheel = deltaWheel;
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final MessageContext ctx)
    {
        final EntityPlayerMP player = ctx.getServerHandler().player;
        final IGuiKey key = BlockOut.getBlockOut().getProxy().getGuiController().getOpenUI(player);
        if (key == null)
        {
            Log.getLogger().error("Player is not watching a BlockOut gui.");
            return;
        }

        final RootGuiElement rootGuiElement = BlockOut.getBlockOut().getProxy().getGuiController().getRoot(key);
        if (rootGuiElement == null)
        {
            Log.getLogger().error("Player seems to be watching an unknown Gui.");
            return;
        }

        rootGuiElement.getUiManager().getScrollManager().onMouseWheel(localX, localY, deltaWheel);
    }
}
