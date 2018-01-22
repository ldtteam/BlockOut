package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.minecolonies.blockout.network.message.core.IBlockOutNetworkMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenGuiRequestMessage implements IBlockOutClientToServerMessage
{

    private ResourceLocation requestedGuiLocation;

    public OpenGuiRequestMessage(final ResourceLocation requestedGuiLocation)
    {
        this.requestedGuiLocation = requestedGuiLocation;
    }

    @Nullable
    @Override
    public IBlockOutNetworkMessage onMessageArrivalAtServer(@NotNull final MessageContext ctx)
    {
        return null;
    }
}
