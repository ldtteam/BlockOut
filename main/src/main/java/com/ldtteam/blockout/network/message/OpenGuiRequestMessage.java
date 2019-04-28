package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
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
        final IMultiplayerPlayerEntity playerMP = ctx.getSendingPlayer();
        ProxyHolder.getInstance().getGuiController().openUI(playerMP.getId(), getKey());
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
