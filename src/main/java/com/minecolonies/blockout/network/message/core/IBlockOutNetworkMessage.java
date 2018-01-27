package com.minecolonies.blockout.network.message.core;

import com.minecolonies.blockout.BlockOut;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.Externalizable;
import java.io.Serializable;

public interface IBlockOutNetworkMessage extends Serializable
{
    default void onMessage(final MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT && this instanceof IBlockOutClientToServerMessage)
        {
            BlockOut.getBlockOut().getProxy().getTaskExecutorForMessage(ctx).addScheduledTask(() -> ((IBlockOutClientToServerMessage) this).onMessageArrivalAtServer(ctx));
        }

        if (ctx.side == Side.CLIENT && this instanceof IBlockOutServerToClientMessage)
        {
            BlockOut.getBlockOut().getProxy().getTaskExecutorForMessage(ctx).addScheduledTask(() -> ((IBlockOutServerToClientMessage) this).onMessageArrivalAtClient(ctx));
        }
    }
}
