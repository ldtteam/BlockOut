package com.minecolonies.blockout.management.server.network.messages;

import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class OnMouseScrollMessage implements IBlockOutServerToClientMessage
{
    @NotNull
    private final int localX;
    @NotNull
    private final int localY;
    @NotNull
    private final int deltaScroll;

    public OnMouseScrollMessage(@NotNull final int localX, @NotNull final int localY, @NotNull final int deltaScroll)
    {
        this.localX = localX;
        this.localY = localY;
        this.deltaScroll = deltaScroll;
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final GuiScreen openGuiScreen = Minecraft.getMinecraft().currentScreen;

        if (openGuiScreen instanceof BlockOutGui)
        {
            final BlockOutGui blockOutGui = (BlockOutGui) openGuiScreen;

            blockOutGui.getRoot().getUiManager().getScrollManager().onMouseScroll(localX, localY, deltaScroll);
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
