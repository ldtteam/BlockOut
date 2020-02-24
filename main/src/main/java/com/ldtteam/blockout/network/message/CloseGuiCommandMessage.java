package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CloseGuiCommandMessage implements IBlockOutServerToClientMessage
{

    private static final long serialVersionUID = 2087308830216529056L;

    public CloseGuiCommandMessage()
    {
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final NetworkEvent.Context ctx)
    {
        Minecraft.getInstance().displayGuiScreen(null);
    }
}
