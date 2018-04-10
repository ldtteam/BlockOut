package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.network.message.core.IBlockOutClientToServerMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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
    public void onMessageArrivalAtServer(@NotNull final MessageContext ctx)
    {
        final EntityPlayerMP playerMP = ctx.getServerHandler().player;
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(playerMP.getUniqueID(), getKey());
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
