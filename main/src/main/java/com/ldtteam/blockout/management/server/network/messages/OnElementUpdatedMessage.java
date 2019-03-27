package com.ldtteam.blockout.management.server.network.messages;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.gui.BlockOutGuiData;
import com.ldtteam.blockout.inventory.BlockOutContainerData;
import com.ldtteam.blockout.inventory.BlockOutContainerLogic;
import com.ldtteam.blockout.loader.object.ObjectUIElementData;
import com.ldtteam.blockout.management.UIManager;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
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
    public void onMessageArrivalAtClient(@NotNull final IMessageContext ctx)
    {
        final IGui<?> openGuiScreen = IGameEngine.getInstance().getCurrentGui();

        if (!(openGuiScreen instanceof IGuiContainer))
        {
            throw new IllegalStateException("No container open!");
        }

        final IGuiContainer<?> openContainerScreen = (IGuiContainer<?>) openGuiScreen;

        if (openContainerScreen.getInstanceData() instanceof BlockOutGuiData)
        {
            final BlockOutGuiData blockOutGui = (BlockOutGuiData) openContainerScreen.getInstanceData();
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
            final IUIElement focusedElement = uiManager.getFocusManager().getFocusedElement();
            if (focusedElement != null)
            {
                uiManager.getFocusManager().setFocusedElement(rootGuiElement.searchExactElementById(focusedElement.getId()).orElse(null));
            }

            rootGuiElement.setUiManager(uiManager);
            blockOutGui.setRoot(rootGuiElement);
            blockOutGui.getRoot().getUiManager().getRenderManager().setGui(blockOutGui);
            blockOutGui.getRoot().getUiManager().getUpdateManager().updateElement(blockOutGui.getRoot());
            openContainerScreen.initGui();

            final IContainer<BlockOutContainerData> container = (IContainer<BlockOutContainerData>) openContainerScreen.getContainer();
            BlockOutContainerLogic.reinitializeSlots(container);
        }
        else
        {
            throw new IllegalStateException("Server thinks a BlockOut guitemp was open.");
        }
    }
}
