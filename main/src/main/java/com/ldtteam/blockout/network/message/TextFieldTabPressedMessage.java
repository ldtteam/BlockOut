package com.ldtteam.blockout.network.message;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
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
        final IGuiKey guiKey = ProxyHolder.getInstance().getGuiController().getOpenUI(playerMP);

        if (guiKey == null)
        {
            return;
        }

        final RootGuiElement rootGuiElement = (RootGuiElement) ProxyHolder.getInstance().getGuiController().getRoot(guiKey);
        final Optional<IUIElement> optionalNextElement = rootGuiElement.searchExactElementById(nextControlId, IUIElement.class);
        optionalNextElement.ifPresent(rootGuiElement.getUiManager().getFocusManager()::setFocusedElement);
    }
}
