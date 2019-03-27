package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenGuiRequestMessage implements IBlockOutClientToServerMessage
{
    @NotNull
    private IGuiKey key;

    public OpenGuiRequestMessage()
    {
    }

    public OpenGuiRequestMessage(@NotNull final IGuiKey key)
    {
        this.key = key;
    }

    @Nullable
    @Override
    public void onMessageArrivalAtServer(@NotNull final IMessageContext ctx)
    {
        final IMultiplayerPlayerEntity playerMP = ctx.getServerHandler().getPlayer();
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(playerMP.getId(), getKey());
    }

    @NotNull
    public IGuiKey getKey()
    {
        return key;
    }

    public void setKey(@NotNull final IGuiKey key)
    {
        this.key = key;
    }
}
