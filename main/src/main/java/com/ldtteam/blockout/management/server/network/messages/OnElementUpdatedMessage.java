package com.ldtteam.blockout.management.server.network.messages;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.gui.BlockOutGui;
import com.ldtteam.blockout.loader.object.ObjectUIElementData;
import com.ldtteam.blockout.management.UIManager;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
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

            final IUIElementHost root = blockOutGui.getRoot();
            if (!(root instanceof RootGuiElement))
            {
                throw new IllegalStateException("The client root is not a root instance");
            }

            final RootGuiElement rootGuiElement = (RootGuiElement) containedElement;
            final UIManager uiManager = (UIManager) blockOutGui.getRoot().getUiManager();

            uiManager.setRootGuiElement(rootGuiElement);
            rootGuiElement.setUiManager(uiManager);
            blockOutGui.setRoot(rootGuiElement);
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut gui was open.");
        }
    }
}
