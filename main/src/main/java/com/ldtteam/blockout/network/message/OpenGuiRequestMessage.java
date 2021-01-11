package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class OpenGuiRequestMessage implements IBlockOutClientToServerMessage
{

    private static final long serialVersionUID = 8196679915085058303L;

    @NotNull
    private IGuiKey key;

    @SuppressWarnings("ConstantConditions")
    public OpenGuiRequestMessage()
    {
        key = null;
    }

    public OpenGuiRequestMessage(@NotNull final IGuiKey key)
    {
        this.key = key;
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final NetworkEvent.Context ctx)
    {
        final ServerPlayerEntity playerMP = ctx.getSender();
        ProxyHolder.getInstance().getGuiController().openUI(playerMP.getUniqueID(), getKey());
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
