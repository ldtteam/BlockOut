package com.minecolonies.blockout.management.server.network.messages;

import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class OnMouseClickBeginMessage implements IBlockOutServerToClientMessage
{
    @NotNull
    private final int         localX;
    @NotNull
    private final int         localY;
    @NotNull
    private final MouseButton button;

    public OnMouseClickBeginMessage(@NotNull final int localX, @NotNull final int localY, @NotNull final MouseButton button)
    {
        this.localX = localX;
        this.localY = localY;
        this.button = button;
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final GuiScreen openGuiScreen = Minecraft.getMinecraft().currentScreen;

        if (openGuiScreen instanceof BlockOutGui)
        {
            final BlockOutGui blockOutGui = (BlockOutGui) openGuiScreen;

            blockOutGui.getRoot().getUiManager().getClickManager().onMouseClickBegin(localX, localY, button);
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
