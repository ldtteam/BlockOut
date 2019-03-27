package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.TextField;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TextFieldOnEnterPressed implements IBlockOutClientToServerMessage
{
    private String controlId = "";

    public TextFieldOnEnterPressed(final String controlId)
    {
        this.controlId = controlId;
    }

    public TextFieldOnEnterPressed()
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
        final Optional<TextField> optionalTextBox = rootGuiElement.searchExactElementById(controlId, TextField.class);
        optionalTextBox.ifPresent(TextField::raiseOnEnterPressed);
    }
}
