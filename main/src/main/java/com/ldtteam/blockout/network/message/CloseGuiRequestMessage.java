package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
import org.jetbrains.annotations.NotNull;

public class CloseGuiRequestMessage implements IBlockOutClientToServerMessage
{

    @Override
    public void onMessageArrivalAtServer(@NotNull final IMessageContext ctx)
    {
        final IMultiplayerPlayerEntity playerMP = ctx.getSendingPlayer();
        ProxyHolder.getInstance().getGuiController().closeUI(playerMP.getId());
    }
}
