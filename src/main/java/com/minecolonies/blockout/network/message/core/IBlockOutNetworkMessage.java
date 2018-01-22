package com.minecolonies.blockout.network.message.core;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public interface IBlockOutNetworkMessage
{
    default IBlockOutNetworkMessage onMessage(final MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT && this instanceof IBlockOutClientToServerMessage)
        {
            return ((IBlockOutClientToServerMessage) this).onMessageArrivalAtServer(ctx);
        }

        if (ctx.side == Side.CLIENT && this instanceof IBlockOutServerToClientMessage)
        {
            return ((IBlockOutServerToClientMessage) this).onMessageArrivalAtClient(ctx);
        }

        return null;
    }
}
