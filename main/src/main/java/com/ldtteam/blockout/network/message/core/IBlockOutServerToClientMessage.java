package com.ldtteam.blockout.network.message.core;

import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutServerToClientMessage extends IBlockOutNetworkMessage
{

    @Override
    default void onArrived(final NetworkEvent.Context ctx)
    {
        onMessageArrivalAtClient(ctx);
    }

    /**
     * Method called by the network system when this {@link IBlockOutNetworkMessage} has arrived at the {@code Side.CLIENT}.
     *
     * @param ctx The {@link NetworkEvent.Context} in which the {@link IBlockOutNetworkMessage} arrived.
     */
    void onMessageArrivalAtClient(@NotNull final NetworkEvent.Context ctx);
}
