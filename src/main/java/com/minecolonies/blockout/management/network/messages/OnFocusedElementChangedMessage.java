package com.minecolonies.blockout.management.network.messages;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.minecolonies.blockout.gui.BlockOutGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnFocusedElementChangedMessage implements IBlockOutServerToClientMessage
{
    private ResourceLocation focusedElementId;

    public OnFocusedElementChangedMessage() {
        focusedElementId = null;
    }

    public OnFocusedElementChangedMessage(@Nullable final IUIElement focusedElement)
    {
        this();

        if (focusedElement != null)
        {
            focusedElementId = focusedElement.getId();
        }
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final GuiScreen openGuiScreen = Minecraft.getMinecraft().currentScreen;

        if (openGuiScreen instanceof BlockOutGui)
        {
            BlockOutGui blockOutGui = (BlockOutGui) openGuiScreen;

            if (focusedElementId != null)
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
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
