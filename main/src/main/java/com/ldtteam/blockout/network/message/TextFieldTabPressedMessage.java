package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.TextField;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.jvoxelizer.entity.player.IMultiplayerPlayerEntity;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TextFieldTabPressedMessage implements IBlockOutClientToServerMessage
{

    private String nextControlId = "";

    public TextFieldTabPressedMessage(final String nextControlId)
    {
        this.nextControlId = nextControlId;
    }

    public TextFieldTabPressedMessage()
    {
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final IMessageContext ctx)
    {
        final IMultiplayerPlayerEntity playerMP = ctx.getServerHandler().getPlayer();
        final IGuiKey guiKey = BlockOut.getBlockOut().getProxy().getGuiController().getOpenUI(playerMP);

        if (guiKey == null)
        {
            return;
        }

        final RootGuiElement rootGuiElement = (RootGuiElement) BlockOut.getBlockOut().getProxy().getGuiController().getRoot(guiKey);
        final Optional<IUIElement> optionalNextElement = rootGuiElement.searchExactElementById(nextControlId, IUIElement.class);
        optionalNextElement.ifPresent(rootGuiElement.getUiManager().getFocusManager()::setFocusedElement);
    }
}
