package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class CloseGuiCommandMessage implements IBlockOutServerToClientMessage
{
    private static final long serialVersionUID = Constants.SERIAL_VAR_ID;

    public CloseGuiCommandMessage()
    {
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
