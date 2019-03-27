package com.ldtteam.blockout.network.message.core;

import com.ldtteam.blockout.network.NetworkManager;

import java.io.Serializable;

public interface IBlockOutNetworkMessage extends Serializable
{
    /**
     * Entrance point to the Message API called by the Network system to tell the message that it arrived on the target side.
     * However this call is still done on the Network thread. As such marshalling is still needed.
     *
     * @param ctx The {@link IMessageContext} in which the message arrived.
     */
    default void onMessage(final IMessageContext ctx)
    {
        NetworkManager.getExecutor(ctx).queue(() -> onArrived(ctx));
    }

    /**
     * Entrance point of the MessageAPI after marshalling to the {@link IExecutor} has been completed.
     * Method calls to this method have be thread safe.
     * <p>
     * As such it is generally recommended to override this method.
     *
     * @param ctx The {@link IMessageContext} in which the message arrived.
     */
    void onArrived(final IMessageContext ctx);
}
