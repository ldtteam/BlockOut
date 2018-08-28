package com.minecolonies.blockout.network.message.core;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutServerToClientMessage extends IBlockOutNetworkMessage
{

    /**
     * Method called by the network system when this {@link IBlockOutNetworkMessage} has arrived at the {@code Side.CLIENT}.
     *
     * @param ctx The {@link MessageContext} in which the {@link IBlockOutNetworkMessage} arrived.
     */
    void onMessageArrivalAtClient(@NotNull final MessageContext ctx);

    @Override
    default void onArrived(final MessageContext ctx)
    {
        onMessageArrivalAtClient(ctx);
    }
}
