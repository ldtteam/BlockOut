package com.minecolonies.blockout.management.server.network.messages;

import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class OnKeyPressedMessage implements IBlockOutServerToClientMessage
{
    @NotNull
    private final int         character;
    @NotNull
    private final KeyboardKey key;

    public OnKeyPressedMessage(@NotNull final int character, @NotNull final KeyboardKey key)
    {
        this.character = character;
        this.key = key;
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final GuiScreen openGuiScreen = Minecraft.getMinecraft().currentScreen;

        if (openGuiScreen instanceof BlockOutGui)
        {
            final BlockOutGui blockOutGui = (BlockOutGui) openGuiScreen;

            blockOutGui.getRoot().getUiManager().getKeyManager().onKeyPressed(character, key);
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
