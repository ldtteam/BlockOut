package com.ldtteam.blockout.management.server.network.messages;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.gui.BlockOutGuiData;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnFocusedElementChangedMessage implements IBlockOutServerToClientMessage
{
    private String focusedElementId;

    public OnFocusedElementChangedMessage(@Nullable final IUIElement focusedElement)
    {
        this();

        if (focusedElement != null)
        {
            focusedElementId = focusedElement.getId();
        }
    }

    public OnFocusedElementChangedMessage()
    {
        focusedElementId = "";
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final IMessageContext ctx)
    {
        final IGui<?> openGuiScreen = IGameEngine.getInstance().getCurrentGui();

        if (openGuiScreen instanceof IGuiContainer && openGuiScreen.getInstanceData() instanceof BlockOutGuiData)
        {
            final IGuiContainer<BlockOutGuiData> blockOutGui = (IGuiContainer<BlockOutGuiData>) openGuiScreen;

            if (focusedElementId != null && !focusedElementId.isEmpty())
            {
                blockOutGui.getInstanceData().getRoot()
                  .getUiManager()
                  .getFocusManager()
                  .setFocusedElement(blockOutGui.getInstanceData().getRoot()
                                       .searchExactElementById(focusedElementId)
                                       .orElseThrow(() -> new IllegalStateException("Tried to focus unknown element."))
                  );
            }
            else
            {
                blockOutGui.getInstanceData().getRoot()
                  .getUiManager()
                  .getFocusManager()
                  .setFocusedElement(null);
            }
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut guitemp was open.");
        }
    }
}
