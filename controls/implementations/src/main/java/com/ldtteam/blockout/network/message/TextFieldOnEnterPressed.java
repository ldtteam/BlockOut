package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.element.simple.TextField;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TextFieldOnEnterPressed implements IBlockOutClientToServerMessage
{

    private static final long serialVersionUID = -1134506085105255805L;

    private String controlId = "";

    public TextFieldOnEnterPressed(final String controlId)
    {
        this.controlId = controlId;
    }

    public TextFieldOnEnterPressed()
    {
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final NetworkEvent.Context ctx)
    {
        final ServerPlayerEntity playerMP = ctx.getSender();
        final IGuiKey guiKey = ProxyHolder.getInstance().getGuiController().getOpenUI(playerMP);

        if (guiKey == null)
        {
            return;
        }

        final IRootGuiElement rootGuiElement = ProxyHolder.getInstance().getGuiController().getRoot(guiKey);
        final Optional<TextField> optionalTextBox = rootGuiElement.searchExactElementById(controlId, TextField.class);
        optionalTextBox.ifPresent(TextField::raiseOnEnterPressed);
    }
}
