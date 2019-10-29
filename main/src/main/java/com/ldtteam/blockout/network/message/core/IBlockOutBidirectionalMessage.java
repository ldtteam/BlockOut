package com.ldtteam.blockout.network.message.core;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IBlockOutBidirectionalMessage extends IBlockOutClientToServerMessage, IBlockOutServerToClientMessage
{

    //Since we inherit from interfaces that both override onArrived in there in own way. We merge their behaviour here again.
    @Override
    default void onArrived(final NetworkEvent.Context ctx)
    {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> onMessageArrivalAtClient(ctx));

        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> onMessageArrivalAtServer(ctx));
    }
}
