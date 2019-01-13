package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.TextBox;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TextBoxUpdateScrollOrCursorPositionMessage implements IBlockOutClientToServerMessage
{
    private String controlId;
    private Integer cursorPosition;
    private Integer selectionEnd;

    public TextBoxUpdateScrollOrCursorPositionMessage(final String controlId, final Integer cursorPosition, final Integer selectionEnd)
    {
        this.controlId = controlId;
        this.cursorPosition = cursorPosition;
        this.selectionEnd = selectionEnd;
    }

    public TextBoxUpdateScrollOrCursorPositionMessage()
    {
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final MessageContext ctx)
    {
        final EntityPlayerMP playerMP = ctx.getServerHandler().player;
        final IGuiKey guiKey = BlockOut.getBlockOut().getProxy().getGuiController().getOpenUI(playerMP);

        if (guiKey == null)
        {
            return;
        }

        final RootGuiElement rootGuiElement = (RootGuiElement) BlockOut.getBlockOut().getProxy().getGuiController().getRoot(guiKey);
        final Optional<TextBox> textBoxOptional = rootGuiElement.searchExactElementById(controlId, TextBox.class);

        textBoxOptional.ifPresent(textBox -> {
            textBox.setCursorPosition(cursorPosition);
            textBox.setSelectionEnd(selectionEnd);
        });
    }
}
