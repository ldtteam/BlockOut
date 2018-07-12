package com.minecolonies.blockout.management.server.network.messages;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.element.root.RootGuiElement;
import com.minecolonies.blockout.gui.BlockOutGui;
import com.minecolonies.blockout.loader.object.ObjectUIElementData;
import com.minecolonies.blockout.management.UIManager;
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
            if (!(containedElement instanceof RootGuiElement))
            {
                throw new IllegalStateException("The synced element is not a root.");
            }

            final IUIElementHost iuiManager = blockOutGui.getRoot();
            if (!(iuiManager instanceof RootGuiElement))
            {
                throw new IllegalStateException("The client side ui manager is not a UIManager instance");
            }

            final RootGuiElement rootGuiElement = (RootGuiElement) containedElement;
            final UIManager uiManager = (UIManager) blockOutGui.getRoot().getUiManager();

            uiManager.setRootGuiElement(rootGuiElement);
            rootGuiElement.setUiManager(uiManager);
            blockOutGui.setRoot(rootGuiElement);
            blockOutGui.initGui();
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
