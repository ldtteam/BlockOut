package com.ldtteam.blockout.management.server.network.messages;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.gui.BlockOutContainerGui;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.network.NetworkEvent;
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
    public void onMessageArrivalAtClient(@NotNull final NetworkEvent.Context ctx)
    {
        final Screen openGuiScreen = Minecraft.getInstance().currentScreen;

        if (openGuiScreen instanceof BlockOutContainerGui)
        {
            final BlockOutContainerGui blockOutGui = (BlockOutContainerGui) openGuiScreen;

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
