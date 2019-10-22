package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.inventory.BlockOutContainerData;
import com.ldtteam.blockout.inventory.slot.BlockOutSlotData;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.ldtteam.jvoxelizer.IGameEngine;
import com.ldtteam.jvoxelizer.client.gui.IGuiContainer;
import com.ldtteam.jvoxelizer.client.gui.IGuiScreen;
import com.ldtteam.jvoxelizer.client.gui.logic.builder.IGuiContainerBuilder;
import com.ldtteam.jvoxelizer.client.gui.logic.builder.IGuiScreenBuilder;
import com.ldtteam.jvoxelizer.client.gui.logic.builder.contexts.*;
import com.ldtteam.jvoxelizer.client.mouse.IMouse;
import com.ldtteam.jvoxelizer.client.renderer.opengl.IOpenGl;
import com.ldtteam.jvoxelizer.core.logic.TypedPipelineElementContext;
import com.ldtteam.jvoxelizer.core.logic.VoidPipelineElementContext;
import com.ldtteam.jvoxelizer.inventory.IContainer;

public class BlockOutGuiLogic
{
    public static IGuiContainer<BlockOutGuiData> create(IGuiKey key, IUIElementHost host, IContainer<BlockOutContainerData> container)
    {
        final IGuiContainerBuilder<?, BlockOutGuiData, IGuiContainer<BlockOutGuiData>> builder = IGuiContainerBuilder.create(new BlockOutGuiData(key, host), container);

        final IGuiContainer<BlockOutGuiData> gui = builder
                 .HandleMouseInput(BlockOutGuiLogic::handleMouseInputContainer)
                 .InitGui(BlockOutGuiLogic::initContainerGui)
                 .DrawScreen(BlockOutGuiLogic::drawScreenContainer)
                 .DrawGuiContainerForegroundLayer(BlockOutGuiLogic::drawGuiContainerForegroundLayer)
                 .DrawGuiContainerBackgroundLayer(BlockOutGuiLogic::drawGuiContainerBackgroundLayer)
                 .DrawSlot(BlockOutGuiLogic::drawSlot)
                 .MouseClicked(BlockOutGuiLogic::mouseClickedContainer)
                 .MouseClickMove(BlockOutGuiLogic::mouseClickMoveContainer)
                 .MouseReleased(BlockOutGuiLogic::mouseReleasedContainer)
                 .IsMouseOverSlot(BlockOutGuiLogic::isMouseOverSlot)
                 .KeyTyped(BlockOutGuiLogic::keyTypedContainer)
                 .UpdateScreen(BlockOutGuiLogic::updateScreen)
                 .build();

        host.getUiManager().getRenderManager().setGuiData(gui.getInstanceData());

        return gui;
    }

    public static IGuiScreen<BlockOutGuiData> createClientSideOnly(IGuiKey key, IUIElementHost host)
    {
        final IGuiScreenBuilder<?, BlockOutGuiData, IGuiScreen<BlockOutGuiData>> builder = IGuiScreenBuilder.create(new BlockOutGuiData(key, host));

        final IGuiScreen<BlockOutGuiData> gui = builder
          .HandleMouseInput(BlockOutGuiLogic::handleMouseInputScreen)
          .InitGui(BlockOutGuiLogic::initScreenGui)
          .DrawScreen(BlockOutGuiLogic::drawScreenScreen)
          .MouseClicked(BlockOutGuiLogic::mouseClickedScreen)
          .MouseClickMove(BlockOutGuiLogic::mouseClickMoveScreen)
          .MouseReleased(BlockOutGuiLogic::mouseReleasedScreen)
          .KeyTyped(BlockOutGuiLogic::keyTypedScreen)
          .UpdateScreen(BlockOutGuiLogic::updateScreen)
          .build();
        
        host.getUiManager().getRenderManager().setGuiData(gui.getInstanceData());

        return gui;
    }

    private static void handleMouseInputContainer(final VoidPipelineElementContext<HandleMouseInputContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) ((IMouse.getX() * context.getInstance().getWidth() / IGameEngine.getInstance().getDisplayWidth()) * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) ((context.getInstance().getHeight() - IMouse.getY() * context.getInstance().getHeight() / IGameEngine.getInstance().getDisplayHeight() - 1) * context.getInstanceData().getScaleFactor().getY());

        int delta = IMouse.getDWheel();
        if (delta != 0)
        {
            if (!context.getInstanceData().getRoot().getUiManager()
                   .getClientSideScrollManager()
                   .onMouseWheel((int) (scaledMouseX - (context.getInstanceData().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstanceData().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), delta))
            {
                context.getInstanceData().getRoot().getUiManager()
                  .getNetworkManager()
                  .onMouseWheel((int) (scaledMouseX - (context.getInstanceData().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstanceData().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), delta);
            }
        }

        context.callSuper();
    }

    private static void handleMouseInputScreen(final VoidPipelineElementContext<HandleMouseInputContext, IGuiScreen<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) ((IMouse.getX() * context.getInstance().getWidth() / IGameEngine.getInstance().getDisplayWidth()) * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) ((context.getInstance().getHeight() - IMouse.getY() * context.getInstance().getHeight() / IGameEngine.getInstance().getDisplayHeight() - 1) * context.getInstanceData().getScaleFactor().getY());

        int delta = IMouse.getDWheel();
        if (delta != 0)
        {
            if (!context.getInstanceData().getRoot().getUiManager()
                   .getClientSideScrollManager()
                   .onMouseWheel(scaledMouseX, scaledMouseY, delta))
            {
                context.getInstanceData().getRoot().getUiManager()
                  .getScrollManager()
                  .onMouseWheel(scaledMouseX, scaledMouseY, delta);
            }
        }

        context.callSuper();
    }

    private static void drawSlot(final VoidPipelineElementContext<DrawSlotContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        if (context.getInstanceData().isDrawing() && context.getContext().getSlot() instanceof BlockOutSlotData)
        {
            return;
        }

        context.callSuper();
    }

    private static void mouseClickedContainer(final VoidPipelineElementContext<MouseClickedContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickBegin(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getMouseButton())))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickBegin((int) (scaledMouseX - (context.getInstanceData().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstanceData().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(context.getContext().getMouseButton()));
        }
    }

    private static void mouseClickedScreen(final VoidPipelineElementContext<MouseClickedContext, IGuiScreen<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickBegin(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getMouseButton())))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getClickManager()
              .onMouseClickBegin(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getMouseButton()));
        }
    }

    private static void mouseClickMoveContainer(final VoidPipelineElementContext<MouseClickMoveContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickMove(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getClickedMouseButton()), context.getContext().getTimeSinceLastClick()))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickMove((int) (scaledMouseX - (context.getInstanceData().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstanceData().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(context.getContext().getClickedMouseButton()), context.getContext().getTimeSinceLastClick());
        }
    }

    private static void mouseClickMoveScreen(final VoidPipelineElementContext<MouseClickMoveContext, IGuiScreen<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickMove(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getClickedMouseButton()), context.getContext().getTimeSinceLastClick()))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getClickManager()
              .onMouseClickMove(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getClickedMouseButton()), context.getContext().getTimeSinceLastClick());
        }
    }

    private static void mouseReleasedContainer(final VoidPipelineElementContext<MouseReleasedContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getState())))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickEnd((int) (scaledMouseX - (context.getInstanceData().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstanceData().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(context.getContext().getState()));
        }
    }

    private static void mouseReleasedScreen(final VoidPipelineElementContext<MouseReleasedContext, IGuiScreen<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getState())))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getClickManager()
              .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getState()));
        }
    }

    private static boolean isMouseOverSlot(final TypedPipelineElementContext<IsMouseOverSlotContext, Boolean, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        return ((!context.getInstanceData().isDrawing() && (!(context.getContext().getSlot() instanceof BlockOutSlotData) || ((BlockOutSlotData) context.getContext().getSlot()).getUiSlotInstance().isEnabled())) || !(context.getContext().getSlot() instanceof BlockOutSlotData))
                 && context.callSuper();
    }

    private static void keyTypedContainer(final VoidPipelineElementContext<KeyTypedContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        final KeyboardKey key = KeyboardKey.getForCode(context.getContext().getKeyCode());
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            ProxyHolder.getInstance().getGuiController().closeUI(IGameEngine.getInstance().getSinglePlayerPlayerEntity());
            return;
        }

        if (!context.getInstanceData().getRoot().getUiManager().getClientSideKeyManager().onKeyPressed(context.getContext().getTypedChar(), key))
        {
            context.getInstanceData().getRoot().getUiManager().getNetworkManager().onKeyPressed(context.getContext().getTypedChar(), key);
        }
    }

    private static void keyTypedScreen(final VoidPipelineElementContext<KeyTypedContext, IGuiScreen<BlockOutGuiData>, BlockOutGuiData> context)
    {
        final KeyboardKey key = KeyboardKey.getForCode(context.getContext().getKeyCode());
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            ProxyHolder.getInstance().getClientSideOnlyGuiController().closeUI(IGameEngine.getInstance().getSinglePlayerPlayerEntity());
            return;
        }

        if (!context.getInstanceData().getRoot().getUiManager().getClientSideKeyManager().onKeyPressed(context.getContext().getTypedChar(), key))
        {
            context.getInstanceData().getRoot().getUiManager().getKeyManager().onKeyPressed(context.getContext().getTypedChar(), key);
        }
    }

    private static <G extends IGuiScreen<BlockOutGuiData>> void updateScreen(final VoidPipelineElementContext<UpdateScreenContext, G, BlockOutGuiData> context)
    {
        if (context.getInstance() instanceof IGuiContainer)
        {
            context.getInstanceData().setGuiOffset(((IGuiContainer) context.getInstance()).getGuiTop(), ((IGuiContainer) context.getInstance()).getGuiLeft());
            context.getInstanceData().setGuiSize(((IGuiContainer) context.getInstance()).getXSize(), ((IGuiContainer) context.getInstance()).getYSize());
        }
    }
}
