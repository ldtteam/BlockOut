package com.ldtteam.blockout.management.client.network.messages;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class OnKeyPressedMessage implements IBlockOutClientToServerMessage
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
    public void onMessageArrivalAtServer(@NotNull final MessageContext ctx)
    {
        final EntityPlayerMP player = ctx.getServerHandler().player;
        final IGuiKey key = BlockOut.getBlockOut().getProxy().getGuiController().getOpenUI(player);
        if (key == null)
        {
            Log.getLogger().error("Player is not watching a BlockOut guitemp.");
            return;
        }

        final RootGuiElement rootGuiElement = (RootGuiElement) BlockOut.getBlockOut().getProxy().getGuiController().getRoot(key);
        if (rootGuiElement == null)
        {
            Log.getLogger().error("Player seems to be watching an unknown Gui.");
            return;
        }

        rootGuiElement.getUiManager().getKeyManager().onKeyPressed(character, this.key);
    }
}
