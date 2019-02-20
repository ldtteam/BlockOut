package com.ldtteam.blockout.network.message.core;

import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
import com.ldtteam.jvoxelizer.util.distribution.executor.IDistributionExecutor;

public interface IBlockOutBidirectionalMessage extends IBlockOutClientToServerMessage, IBlockOutServerToClientMessage
{

    //Since we inherit from interfaces that both override onArrived in there in own way. We merge their behaviour here again.
    @Override
    default void onArrived(final IMessageContext ctx)
    {
        IDistributionExecutor.on(
          () -> onMessageArrivalAtClient(ctx),
          () -> onMessageArrivalAtServer(ctx)
        );
    }
}
