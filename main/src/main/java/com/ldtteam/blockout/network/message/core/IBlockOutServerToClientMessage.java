package com.ldtteam.blockout.network.message.core;

import org.jetbrains.annotations.NotNull;

public interface IBlockOutServerToClientMessage extends IBlockOutNetworkMessage
{

    @Override
    default void onArrived(final IMessageContext ctx)
    {
        onMessageArrivalAtClient(ctx);
    }

    /**
     * Method called by the network system when this {@link IBlockOutNetworkMessage} has arrived at the {@code Side.CLIENT}.
     *
     * @param ctx The {@link IMessageContext} in which the {@link IBlockOutNetworkMessage} arrived.
     */
    void onMessageArrivalAtClient(@NotNull final IMessageContext ctx);
}
