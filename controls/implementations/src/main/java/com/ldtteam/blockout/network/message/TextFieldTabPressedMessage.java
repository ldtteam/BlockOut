package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class TextFieldTabPressedMessage implements IBlockOutClientToServerMessage
{

    private static final long serialVersionUID = -7709741531384954224L;

    private String nextControlId = "";

    public TextFieldTabPressedMessage(final String nextControlId)
    {
        this.nextControlId = nextControlId;
    }

    public TextFieldTabPressedMessage()
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
        final Optional<IUIElement> optionalNextElement = Objects.requireNonNull(rootGuiElement).searchExactElementById(nextControlId, IUIElement.class);
        optionalNextElement.ifPresent(rootGuiElement.getUiManager().getFocusManager()::setFocusedElement);
    }
}
