package com.minecolonies.blockout.network.message.core;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBlockOutServerToClientMessage
{

    /**
     * Method called by the network system when this {@link IBlockOutNetworkMessage} has arrived at the {@code Side.CLIENT}.
     *
     * @param ctx The {@link MessageContext} in which the {@link IBlockOutNetworkMessage} arrived.
     * @return The {@link IBlockOutNetworkMessage} that should be send back to the server.
     */
    @Nullable
    IBlockOutNetworkMessage onMessageArrivalAtClient(@NotNull final MessageContext ctx);
}
