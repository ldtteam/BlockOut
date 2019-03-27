package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import org.jetbrains.annotations.NotNull;

public class CloseGuiCommandMessage implements IBlockOutServerToClientMessage
{
    public CloseGuiCommandMessage()
    {
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final IMessageContext ctx)
    {
        IGameEngine.getInstance().displayGuiScreen(null);
    }
}
