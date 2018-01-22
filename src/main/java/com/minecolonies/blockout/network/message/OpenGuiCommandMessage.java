package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.network.message.core.IBlockOutNetworkMessage;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenGuiCommandMessage implements IBlockOutServerToClientMessage
{

    @Nullable
    @Override
    public IBlockOutNetworkMessage onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
    }
}
