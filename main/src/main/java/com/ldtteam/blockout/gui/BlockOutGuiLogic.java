package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.inventory.BlockOutContainerData;
import com.ldtteam.blockout.inventory.slot.BlockOutSlotData;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;

public class BlockOutGuiLogic
{
    public static IGuiContainer<BlockOutGuiData> create(IGuiKey key, IUIElementHost host, IContainer<BlockOutContainerData> container)
    {
        final IGuiContainerBuilder<?, BlockOutGuiData, IGuiContainer<BlockOutGuiData>> builder = IGuiContainerBuilder.create(new BlockOutGuiData(key, host), container);

        final IGuiContainer<BlockOutGuiData> gui = builder
                 .HandleMouseInput(BlockOutGuiLogic::handleMouseInput)
                 .InitGui(BlockOutGuiLogic::initGui)
                 .DrawScreen(BlockOutGuiLogic::drawScreen)
                 .DrawGuiContainerForegroundLayer(BlockOutGuiLogic::drawGuiContainerForegroundLayer)
                 .DrawGuiContainerBackgroundLayer(BlockOutGuiLogic::drawGuiContainerBackgroundLayer)
                 .DrawSlot(BlockOutGuiLogic::drawSlot)
                 .MouseClicked(BlockOutGuiLogic::mouseClicked)
                 .MouseClickMove(BlockOutGuiLogic::mouseClickMove)
                 .MouseReleased(BlockOutGuiLogic::mouseReleased)
                 .IsMouseOverSlot(BlockOutGuiLogic::isMouseOverSlot)
                 .KeyTyped(BlockOutGuiLogic::keyTyped)
                 .build();

        host.getUiManager().getRenderManager().setGui(gui.getInstanceData());

        return gui;
    }

    private static void handleMouseInput(final VoidPipelineElementContext<HandleMouseInputContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) ((IMouse.getX() * context.getInstance().getWidth() / IGameEngine.getInstance().getDisplayWidth()) * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) ((context.getInstance().getHeight() - IMouse.getY() * context.getInstance().getHeight() / IGameEngine.getInstance().getDisplayHeight() - 1) * context.getInstanceData().getScaleFactor().getY());

        int delta = IMouse.getDWheel();
        if (delta != 0)
        {
            if (!context.getInstanceData().getRoot().getUiManager()
                   .getClientSideScrollManager()
                   .onMouseWheel((int) (scaledMouseX - (context.getInstance().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstance().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), delta))
            {
                context.getInstanceData().getRoot().getUiManager()
                  .getNetworkManager()
                  .onMouseWheel((int) (scaledMouseX - (context.getInstance().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstance().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), delta);
            }
        }

        context.callSuper();
    }

    private static void initGui(final VoidPipelineElementContext<InitGuiContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        context.getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(context.getInstanceData().getRoot());
        context.getInstance().setXSize((int) context.getInstanceData().getRoot().getLocalBoundingBox().getSize().getX());
        context.getInstance().setYSize((int) context.getInstanceData().getRoot().getLocalBoundingBox().getSize().getY());

        context.getInstanceData().setScaleFactor(new Vector2d(1, 1));

        //Check if we need to scale the guitemp
        if (context.getInstance().getXSize() > context.getInstance().getWidth() || context.getInstance().getYSize() > context.getInstance().getHeight())
        {
            double xScalingFactor = Math.ceil(context.getInstanceData().getRoot().getLocalBoundingBox().getSize().getX() / context.getInstance().getWidth());
            double yScalingFactor = Math.ceil(context.getInstanceData().getRoot().getLocalBoundingBox().getSize().getY() / context.getInstance().getHeight());

            //Equalise the scaling.
            xScalingFactor = Math.max(xScalingFactor, yScalingFactor);
            yScalingFactor = xScalingFactor;

            context.getInstanceData().setScaleFactor(new Vector2d(xScalingFactor, yScalingFactor));

            context.getInstance().setXSize((int) (context.getInstanceData().getRoot().getLocalBoundingBox().getSize().getX() / xScalingFactor));
            context.getInstance().setYSize((int) (context.getInstanceData().getRoot().getLocalBoundingBox().getSize().getY() / yScalingFactor));
        }

        context.getInstanceData().getRoot().getUiManager().getRenderManager().setRenderingScalingFactor(context.getInstanceData().getScaleFactor());

        context.callSuper();

        //Update the margins to be scaled now.
        int scaledGuiLeft = (int) (context.getInstance().getGuiLeft() * context.getInstanceData().getScaleFactor().getX());
        int scaledGuiTop = (int) (context.getInstance().getGuiTop() * context.getInstanceData().getScaleFactor().getY());

        context.getInstanceData().getRoot().setMargin(new AxisDistance(scaledGuiLeft, scaledGuiTop, scaledGuiLeft, scaledGuiTop));
        context.getInstanceData().getRoot().getUiManager().getUpdateManager().updateElement(context.getInstanceData().getRoot());
    }

    private static void drawScreen(final VoidPipelineElementContext<DrawScreenContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        context.getInstanceData().setDrawing(true);

        int scaledMouseX = (int) (context.getContext().getMouseX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (context.getContext().getMouseY() * context.getInstanceData().getScaleFactor().getY());

        //Can be done here since both fore and background methods are called by the super
        context.getInstanceData().getRoot().getUiManager().getRenderManager().getRenderingController().setMousePosition(scaledMouseX, scaledMouseY);

        IOpenGl.pushMatrix();

        IOpenGl.scale(1 / context.getInstanceData().getScaleFactor().getX(), 1 / context.getInstanceData().getScaleFactor().getY(), 1f);

        IOpenGl.pushMatrix();

        context.callSuper();

        IOpenGl.popMatrix();
        IOpenGl.popMatrix();

        context.getInstanceData().setDrawing(false);
    }

    private static void drawGuiContainerForegroundLayer(final VoidPipelineElementContext<DrawGuiContainerForegroundLayerContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        context.getInstanceData().getRoot().getUiManager().getRenderManager().drawForeground(context.getInstanceData().getRoot());
    }

    private static void drawGuiContainerBackgroundLayer(final VoidPipelineElementContext<DrawGuiContainerBackgroundLayerContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        context.getInstanceData().getRoot().getUiManager().getRenderManager().drawBackground(context.getInstanceData().getRoot());
    }

    private static void drawSlot(final VoidPipelineElementContext<DrawSlotContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        if (context.getInstanceData().isDrawing() && context.getContext().getSlot() instanceof BlockOutSlotData)
        {
            return;
        }

        context.callSuper();
    }

    private static void mouseClicked(final VoidPipelineElementContext<MouseClickedContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (IMouse.getX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (IMouse.getY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickBegin(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getMouseButton())))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickBegin((int) (scaledMouseX - (context.getInstance().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstance().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(context.getContext().getMouseButton()));
        }
    }

    private static void mouseClickMove(final VoidPipelineElementContext<MouseClickMoveContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (IMouse.getX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (IMouse.getY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickMove(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getClickedMouseButton()), context.getContext().getTimeSinceLastClick()))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickMove((int) (scaledMouseX - (context.getInstance().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstance().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(context.getContext().getClickedMouseButton()), context.getContext().getTimeSinceLastClick());
        }
    }

    private static void mouseReleased(final VoidPipelineElementContext<MouseReleasedContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        int scaledMouseX = (int) (IMouse.getX() * context.getInstanceData().getScaleFactor().getX());
        int scaledMouseY = (int) (IMouse.getY() * context.getInstanceData().getScaleFactor().getY());

        context.getContext().setMouseX(scaledMouseX);
        context.getContext().setMouseY(scaledMouseY);

        context.callSuper();

        if (!context.getInstanceData().getRoot().getUiManager()
               .getClientSideClickManager()
               .onMouseClickEnd(scaledMouseX, scaledMouseY, MouseButton.getForCode(context.getContext().getState())))
        {
            context.getInstanceData().getRoot().getUiManager()
              .getNetworkManager()
              .onMouseClickEnd((int) (scaledMouseX - (context.getInstance().getGuiLeft() * context.getInstanceData().getScaleFactor().getX())), (int) (scaledMouseY - (context.getInstance().getGuiTop() * context.getInstanceData().getScaleFactor().getY())), MouseButton.getForCode(context.getContext().getState()));
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

    private static void keyTyped(final VoidPipelineElementContext<KeyTypedContext, IGuiContainer<BlockOutGuiData>, BlockOutGuiData> context)
    {
        final KeyboardKey key = KeyboardKey.getForCode(context.getContext().getKeyCode());
        if (key == KeyboardKey.KEY_ESCAPE)
        {
            BlockOut.getBlockOut().getProxy().getGuiController().closeUI(IGameEngine.getInstance().getSinglePlayerPlayerEntity());
            return;
        }

        if (!context.getInstanceData().getRoot().getUiManager().getClientSideKeyManager().onKeyPressed(context.getContext().getTypedChar(), key))
        {
            context.getInstanceData().getRoot().getUiManager().getNetworkManager().onKeyPressed(context.getContext().getTypedChar(), key);
        }
    }
}
