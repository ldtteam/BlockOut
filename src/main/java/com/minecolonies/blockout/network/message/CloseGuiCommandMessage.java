package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class CloseGuiCommandMessage implements IBlockOutServerToClientMessage
{
    public CloseGuiCommandMessage()
    {
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
