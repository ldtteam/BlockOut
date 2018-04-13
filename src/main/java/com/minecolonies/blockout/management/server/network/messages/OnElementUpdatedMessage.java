package com.minecolonies.blockout.management.server.network.messages;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.loader.object.ObjectUIElementData;
import com.minecolonies.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class OnElementUpdatedMessage implements IBlockOutServerToClientMessage
{
    @NotNull
    private final ObjectUIElementData elementData;

    public OnElementUpdatedMessage(@NotNull final ObjectUIElementData elementData)
    {
        this.elementData = elementData;
    }

    @Override
    public void onMessageArrivalAtClient(@NotNull final MessageContext ctx)
    {
        final GuiScreen openGuiScreen = Minecraft.getMinecraft().currentScreen;

        if (openGuiScreen instanceof BlockOutGui)
        {
            final BlockOutGui blockOutGui = (BlockOutGui) openGuiScreen;
            final IUIElement containedElement = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(elementData);
            final String id = containedElement.getId();

            blockOutGui.getRoot().searchExactElementById(id).ifPresent(target -> {
                if (blockOutGui.getRoot().equals(target))
                {
                    if (target instanceof IUIElementHost)
                    {
                        blockOutGui.setRoot((IUIElementHost) target);
                    }
                    else
                    {
                        throw new IllegalStateException("Given target is not a content root. Update not possible");
                    }
                }
                else
                {
                    final IUIElementHost targetParent = target.getParent();
                    targetParent.remove(target.getId());
                    targetParent.put(target.getId(), target);
                }
            });
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
