package com.ldtteam.blockout.network.message.core;

import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutClientToServerMessage extends IBlockOutNetworkMessage
{

    @Override
    default void onArrived(final NetworkEvent.Context ctx)
    {
        onMessageArrivalAtServer(ctx);
    }

    /**
     * Method called by the network system when this {@link IBlockOutNetworkMessage} has arrived at the {@code Side.SERVER}
     *
     * @param ctx The {@link NetworkEvent.Context} in which the {@link IBlockOutNetworkMessage} arrived.
     */
    void onMessageArrivalAtServer(@NotNull final NetworkEvent.Context ctx);
}
