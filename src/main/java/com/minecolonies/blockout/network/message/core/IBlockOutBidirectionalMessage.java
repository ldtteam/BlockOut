package com.minecolonies.blockout.network.message.core;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public interface IBlockOutBidirectionalMessage extends IBlockOutClientToServerMessage, IBlockOutServerToClientMessage
{

    //Since we inherit from interfaces that both override onArrived in there in own way. We merge their behaviour here again.
    @Override
    default void onArrived(final MessageContext ctx)
    {
        final Side side = FMLCommonHandler.instance().getSide();
        if (side == Side.CLIENT)
        {
            onMessageArrivalAtClient(ctx);
        }
        else
        {
            onMessageArrivalAtServer(ctx);
        }
    }
}
