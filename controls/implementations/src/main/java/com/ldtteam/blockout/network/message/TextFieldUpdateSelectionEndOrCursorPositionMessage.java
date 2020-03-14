package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.TextField;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TextFieldUpdateSelectionEndOrCursorPositionMessage implements IBlockOutClientToServerMessage
{

    private static final long serialVersionUID = 4331323556947802191L;

    @NotNull
    private String  controlId;
    private int cursorPosition;
    private int selectionEnd;

    public TextFieldUpdateSelectionEndOrCursorPositionMessage(@NotNull final String controlId, final int cursorPosition, final int selectionEnd)
    {
        this.controlId = controlId;
        this.cursorPosition = cursorPosition;
        this.selectionEnd = selectionEnd;
    }

    @SuppressWarnings({"ConstantConditions", "unused"})
    public TextFieldUpdateSelectionEndOrCursorPositionMessage()
    {
        controlId = null;
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final NetworkEvent.Context ctx)
    {
        final ServerPlayerEntity playerMP = ctx.getSender();
        Validate.notNull(playerMP);

        final IGuiKey guiKey = ProxyHolder.getInstance().getGuiController().getOpenUI(playerMP);

        if (guiKey == null)
        {
            return;
        }

        final RootGuiElement rootGuiElement = (RootGuiElement) ProxyHolder.getInstance().getGuiController().getRoot(guiKey);
        Validate.notNull(rootGuiElement);

        final Optional<TextField> textBoxOptional = rootGuiElement.searchExactElementById(controlId, TextField.class);

        textBoxOptional.ifPresent(textBox -> {
            textBox.setCursorPosition(cursorPosition);
            textBox.setSelectionEnd(selectionEnd);
            rootGuiElement.getUiManager().getFocusManager().setFocusedElement(textBox);
            rootGuiElement.getUiManager().getUpdateManager().markDirty();
        });
    }
}
