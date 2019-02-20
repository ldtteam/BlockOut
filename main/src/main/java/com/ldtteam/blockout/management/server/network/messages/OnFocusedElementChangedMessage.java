package com.ldtteam.blockout.management.server.network.messages;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.gui.BlockOutGui;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final GuiScreen openGuiScreen = Minecraft.getMinecraft().currentScreen;

        if (openGuiScreen instanceof BlockOutGui)
        {
            final BlockOutGui blockOutGui = (BlockOutGui) openGuiScreen;

            if (focusedElementId != null && !focusedElementId.isEmpty())
            {
                blockOutGui.getRoot()
                  .getUiManager()
                  .getFocusManager()
                  .setFocusedElement(blockOutGui.getRoot()
                                       .searchExactElementById(focusedElementId)
                                       .orElseThrow(() -> new IllegalStateException("Tried to focus unknown element."))
                  );
            }
            else
            {
                blockOutGui.getRoot()
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
