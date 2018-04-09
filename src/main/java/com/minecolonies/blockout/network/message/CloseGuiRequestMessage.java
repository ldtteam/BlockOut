package com.minecolonies.blockout.network.message;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.minecolonies.blockout.util.Constants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class CloseGuiRequestMessage implements IBlockOutClientToServerMessage
{
    private static final long serialVersionUID = Constants.SERIAL_VAR_ID;

    @Override
    public void onMessageArrivalAtServer(@NotNull final MessageContext ctx)
    {
        final EntityPlayerMP playerMP = ctx.getServerHandler().player;
        BlockOut.getBlockOut().getProxy().getGuiController().onUiClosed(playerMP.getUniqueID());
    }
}
