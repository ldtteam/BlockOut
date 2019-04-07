package com.ldtteam.blockout.render.standard;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.blockout.gui.BlockOutGuiData;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.core.IScissoringController;
import com.ldtteam.blockout.util.color.IColor;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.IGameEngine;
import com.ldtteam.jvoxelizer.biome.IBiome;
import com.ldtteam.jvoxelizer.block.state.IBlockState;
import com.ldtteam.jvoxelizer.client.gui.IGuiContainer;
import com.ldtteam.jvoxelizer.client.renderer.bufferbuilder.IBufferBuilder;
import com.ldtteam.jvoxelizer.client.renderer.font.IFontRenderer;
import com.ldtteam.jvoxelizer.client.renderer.item.IItemRenderer;
import com.ldtteam.jvoxelizer.client.renderer.opengl.IOpenGl;
import com.ldtteam.jvoxelizer.client.renderer.opengl.util.DestinationFactor;
import com.ldtteam.jvoxelizer.client.renderer.opengl.util.SourceFactor;
import com.ldtteam.jvoxelizer.client.renderer.opengl.util.vertexformat.IVertexFormat;
import com.ldtteam.jvoxelizer.client.renderer.tessellator.ITessellator;
import com.ldtteam.jvoxelizer.client.renderer.texture.ISprite;
import com.ldtteam.jvoxelizer.client.renderer.texture.ISpriteMap;
import com.ldtteam.jvoxelizer.core.logic.DummyInstanceData;
import com.ldtteam.jvoxelizer.dimension.IDimensionReader;
import com.ldtteam.jvoxelizer.dimension.IDimensionType;
import com.ldtteam.jvoxelizer.dimension.logic.builder.IDimensionReaderBuilder;
import com.ldtteam.jvoxelizer.fluid.IFluidStack;
import com.ldtteam.jvoxelizer.inventory.IContainer;
import com.ldtteam.jvoxelizer.inventory.slot.ISlot;
import com.ldtteam.jvoxelizer.item.IItemStack;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.jvoxelizer.util.math.coordinate.block.IBlockCoordinate;
import com.ldtteam.jvoxelizer.util.textformatting.ITextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenderingController implements IRenderingController
{

    @NotNull
    private final static IItemRenderer ITEMRENDERER = IGameEngine.getInstance().getItemRenderer();

    @NotNull
    private final IRenderManager        renderManager;
    @NotNull
    private final IScissoringController scissoringController = new ScissoringController(this);

    @NotNull
    private Vector2d lastKnownMousePosition = new Vector2d();

    public RenderingController(@NotNull final IRenderManager renderManager) {this.renderManager = renderManager;}

    @Override
    public Vector2d getRenderingScalingFactor()
    {
        return renderManager.getRenderingScalingFactor();
    }

    @Override
    public IScissoringController getScissoringController()
    {
        return scissoringController;
    }

    /**
     * Convenient helper function to bind the texture to a StringConverter adress
     *
     * @param textureAddress The address of the Texture to bind.
     */
    @Override
    public void bindTexture(@NotNull String textureAddress)
    {
        bindTexture(IIdentifier.create(textureAddress));
    }

    /**
     * Convenient helper function to bind the texture using a IIdentifier
     *
     * @param textureLocation The IIdentifier of the Texture to bind.
     */
    @Override
    public void bindTexture(@NotNull IIdentifier textureLocation)
    {
        IGameEngine.getInstance().getTextureManager().bindTexture(textureLocation);
    }

    @Override
    public void drawTexturedModalRect(
      @NotNull final Vector2d origin,
      @NotNull final Vector2d size,
      @NotNull final Vector2d inTexturePosition,
      @NotNull final Vector2d inTextureSize,
      @NotNull final Vector2d textureSize)
    {
        IOpenGl.pushMatrix();
        IOpenGl.enableBlend();
        IOpenGl.disableAlpha();
        IOpenGl.disableLighting();
        IOpenGl.blendFunc(SourceFactor.SRC_ALPHA, DestinationFactor.ONE_MINUS_SRC_ALPHA);
        IOpenGl.color(1, 1, 1, 1);

        double x = origin.getX();
        double y = origin.getY();
        double width = size.getX();
        double height = size.getY();
        double textureX = inTexturePosition.getX();
        double textureY = inTexturePosition.getY();
        double textureWidth = inTextureSize.getX() / textureSize.getX();
        double textureHeight = inTextureSize.getY() / textureSize.getY();

        ITessellator tessellator = ITessellator.getInstance();
        IBufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, IVertexFormat.positionTex());
        bufferbuilder
          .pos(x + 0, y + height, 0)
          .tex(textureX, textureY + textureHeight)
          .endVertex();
        bufferbuilder
          .pos(x + width, y + height, 0)
          .tex(textureX + textureWidth, textureY + textureHeight)
          .endVertex();
        bufferbuilder
          .pos(x + width, y + 0, 0)
          .tex(textureX + textureWidth, textureY)
          .endVertex();
        bufferbuilder
          .pos(x + 0, y + 0, 0)
          .tex(textureX, textureY)
          .endVertex();
        tessellator.draw();

        IOpenGl.disableBlend();
        IOpenGl.enableAlpha();
        IOpenGl.enableLighting();
        IOpenGl.popMatrix();
    }

    /**
     * Draws a given FluidStack on the Screen.
     * <p>
     * This function comes with regards to the BuildCraft Team
     *
     * @param fluid The Stack to render
     * @param x     The x offset
     * @param y     The y offset
     * @param z     The z offset
     * @param w     The total Width
     * @param h     The total Height
     */
    @Override
    public void drawFluid(@Nullable IFluidStack fluid, int x, int y, int z, int w, int h)
    {
        if (fluid == null || fluid.getFluid() == null)
        {
            return;
        }

        ISprite texture = IGameEngine.getInstance().getTextureMapBlocks().getSrite(fluid.getFluid().getStill(fluid).toString());

        if (texture == null)
        {
            texture = IGameEngine.getInstance().getTextureManager().getSpriteMap(ISpriteMap.getLocationOfBlocksTexture()).getSrite("missingno");
        }

        final IColor fluidColor = IColor.create(fluid.getFluid().getColor(fluid));

        bindTexture(ISpriteMap.getLocationOfBlocksTexture());
        fluidColor.performOpenGLColoring();

        int fullX = w / 16 + 1;
        int fullY = h / 16 + 1;
        for (int i = 0; i < fullX; i++)
        {
            for (int j = 0; j < fullY; j++)
            {
                drawCutIcon(texture, x + i * 16, y + j * 16, z, 16, 16, 0);
            }
        }
    }

    /**
     * Draws a cut IIcon on the Screen
     * This function comes with regards to the BuildCraft Team
     *
     * @param x              The x offset
     * @param y              The y offset
     * @param z              The z offset
     * @param w              The total Width
     * @param h              The total Height
     * @param cutOffVertical The vertical distance to cut of.
     */
    @Override
    public void drawCutIcon(@NotNull ISprite icon, int x, int y, int z, int w, int h, int cutOffVertical)
    {
        ITessellator tessellator = ITessellator.getInstance();
        IBufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, IVertexFormat.positionTex());

        worldrenderer.pos((double) (x + 0), (double) (y + h), (double) z).tex((double) icon.getMinU(), (double) icon.getInterpolatedV(h)).endVertex();
        worldrenderer.pos((double) (x + w), (double) (y + h), (double) z)
          .tex((double) icon.getInterpolatedU(w), (double) icon.getInterpolatedV(h))
          .endVertex();
        worldrenderer.pos((double) (x + w), (double) (y + 0), (double) z)
          .tex((double) icon.getInterpolatedU(w), (double) icon.getInterpolatedV(cutOffVertical))
          .endVertex();
        worldrenderer.pos((double) (x + 0), (double) (y + 0), (double) z).tex((double) icon.getMinU(), (double) icon.getInterpolatedV(cutOffVertical)).endVertex();

        tessellator.draw();
    }

    /**
     * Helper function copied from the guitemp class to make it possible to use it outside of a guitemp class.
     * <p>
     * TRhe function comes with regards to the Minecraft Team
     *
     * @param x    The x offset
     * @param y    The y offset
     * @param z    The z offset
     * @param w    The total Width
     * @param h    The total Height
     * @param icon The IIcon describing the Texture
     */
    @Override
    public void drawTexturedModelRectFromIcon(int x, int y, int z, @NotNull ISprite icon, int w, int h)
    {
        ITessellator tessellator = ITessellator.getInstance();
        IBufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, IVertexFormat.positionTex());
        worldrenderer.pos((double) (x + 0), (double) (y + h), (double) z).tex((double) icon.getMinU(), (double) icon.getMaxV()).endVertex();
        worldrenderer.pos((double) (x + w), (double) (y + h), (double) z).tex((double) icon.getMaxU(), (double) icon.getMaxV()).endVertex();
        worldrenderer.pos((double) (x + w), (double) (y + 0), (double) z).tex((double) icon.getMaxU(), (double) icon.getMinV()).endVertex();
        worldrenderer.pos((double) (x + 0), (double) (y + 0), (double) z).tex((double) icon.getMinU(), (double) icon.getMinV()).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a colored rectangle over the given plane.
     *
     * @param box The plane to cover in the color on the Screen
     * @param z   The Z-Level to render on.
     * @param c   The color to render.
     */
    @Override
    public void drawColoredRect(@NotNull BoundingBox box, int z, @NotNull IColor c)
    {
        drawGradiendColoredRect(box, z, c, c);
    }

    /**
     * Draws a vertical gradient rectangle in the given position.
     *
     * @param box        The plane to fill on the guitemp
     * @param z          The Z-Level to render on.
     * @param colorStart The left color
     * @param colorEnd   The right color.
     */
    @Override
    public void drawGradiendColoredRect(@NotNull BoundingBox box, int z, @NotNull IColor colorStart, @NotNull IColor colorEnd)
    {
        float f = colorStart.getAlphaFloat();
        float f1 = colorStart.getBlueFloat();
        float f2 = colorStart.getGreenFloat();
        float f3 = colorStart.getRedFloat();
        float f4 = colorEnd.getAlphaFloat();
        float f5 = colorEnd.getBlueFloat();
        float f6 = colorEnd.getGreenFloat();
        float f7 = colorEnd.getRedFloat();
        IOpenGl.disableTexture2D();
        IOpenGl.enableBlend();
        IOpenGl.disableAlpha();
        IOpenGl.tryBlendFuncSeparate(770, 771, 1, 0);
        IOpenGl.shadeModel(7425);
        ITessellator tessellator = ITessellator.getInstance();
        IBufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, IVertexFormat.positionColor());
        worldrenderer.pos(box.getLowerRightCoordinate().getX(), box.getUpperLeftCoordinate().getY(), (double) z).color(f3, f2, f1, f).endVertex();
        worldrenderer.pos(box.getUpperLeftCoordinate().getX(), box.getUpperLeftCoordinate().getY(), (double) z).color(f3, f2, f1, f).endVertex();
        worldrenderer.pos(box.getUpperLeftCoordinate().getX(), box.getLowerRightCoordinate().getY(), (double) z).color(f7, f6, f5, f4).endVertex();
        worldrenderer.pos(box.getLowerRightCoordinate().getX(), box.getLowerRightCoordinate().getY(), (double) z).color(f7, f6, f5, f4).endVertex();
        tessellator.draw();
        IOpenGl.shadeModel(7424);
        IOpenGl.disableBlend();
        IOpenGl.enableAlpha();
        IOpenGl.enableTexture2D();
    }

    @Override
    public void drawRect(final double left, final double top, final double right, final double bottom, @NotNull final IColor color)
    {
        double relativeLeft = left;
        double relativeRight = right;
        double relativeTop = top;
        double relativeBottom = bottom;
        double j;
        if (left < right)
        {
            j = left;
            relativeLeft = right;
            relativeRight = j;
        }

        if (top < bottom)
        {
            j = top;
            relativeTop = bottom;
            relativeBottom = j;
        }

        float f = color.getRedFloat();
        float f1 = color.getGreenFloat();
        float f2 = color.getBlueFloat();
        float f3 = color.getAlphaFloat();
        ITessellator tessellator = ITessellator.getInstance();
        IBufferBuilder bufferbuilder = tessellator.getBuffer();
        IOpenGl.enableBlend();
        IOpenGl.disableTexture2D();
        IOpenGl.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA,
          DestinationFactor.ONE_MINUS_SRC_ALPHA,
          SourceFactor.ONE,
          DestinationFactor.ZERO);
        IOpenGl.color(f, f1, f2, f3);
        bufferbuilder.begin(7, IVertexFormat.position());
        bufferbuilder.pos(relativeLeft, relativeBottom, 0.0D).endVertex();
        bufferbuilder.pos(relativeRight, relativeBottom, 0.0D).endVertex();
        bufferbuilder.pos(relativeRight, relativeTop, 0.0D).endVertex();
        bufferbuilder.pos(relativeLeft, relativeTop, 0.0D).endVertex();
        tessellator.draw();
        IOpenGl.enableTexture2D();
        IOpenGl.disableBlend();
    }

    /**
     * Helper function to draw an ItemStack on the given location
     *
     * @param stack The Stack to render
     * @param x     The X Coordinate to render on
     * @param y     The Y Coordinate to render on
     */
    @Override
    public void drawItemStack(@NotNull IItemStack stack, int x, int y)
    {
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
        IOpenGl.enableStandardItemLighting();

        IFontRenderer font = null;
        if (!stack.isEmpty())
        {
            font = stack.getItem().getFontRenderer(stack);
        }

        if (font == null)
        {
            font = IGameEngine.getInstance().getDefaultFontRenderer();
        }

        ITEMRENDERER.renderItemAndEffectIntoGUI(stack, x, y);
        ITEMRENDERER.renderItemOverlayIntoGUI(font, stack, x, y, "");

        IOpenGl.disableStandardItemLighting();
        IOpenGl.enableDepth();
        IOpenGl.disableLighting();
    }

    /**
     * Helper function to draw an ItemStack with an Overlaytext
     *
     * @param stack   The Stack to Render
     * @param x       The X Coordinate to render on
     * @param y       The Y Coordinate to render on
     * @param altText The overlay text to render.
     */
    @Override
    public void drawItemStack(@NotNull IItemStack stack, int x, int y, String altText)
    {
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
        IOpenGl.enableStandardItemLighting();

        IFontRenderer font = null;
        if (!stack.isEmpty())
        {
            font = stack.getItem().getFontRenderer(stack);
        }

        if (font == null)
        {
            font = IGameEngine.getInstance().getDefaultFontRenderer();
        }

        ITEMRENDERER.renderItemAndEffectIntoGUI(stack, x, y);
        ITEMRENDERER.renderItemOverlayIntoGUI(font, stack, x, y, altText);

        IOpenGl.disableStandardItemLighting();
        IOpenGl.enableDepth();
        IOpenGl.disableLighting();
    }

    @Override
    public void drawBlockState(@NotNull final IBlockState state, final int x, final int y)
    {
        IOpenGl.disableStandardItemLighting();

        final IDimensionReaderBuilder<?, DummyInstanceData, ? extends IDimensionReader<DummyInstanceData>> dimensionReaderBuilder = IDimensionReaderBuilder.create();
        final IDimensionReader<?> dimensionReader = dimensionReaderBuilder
          .GetBlockEntity(context -> {
              if (context.getContext().getPos().getX() != 0 || context.getContext().getPos().getY() != 0 || context.getContext().getPos().getZ() != 0)
              {
                  return null;
              }

              //TODO: Pull in structurize compatible dummy dimension. Or make dummy dimension library.
              return state.getBlock().createBlockEntity(null, state);
          }).GetCombinedLight(context -> {
              int i = 15;
              int j = 15;
              if (j < context.getContext().getLightValue())
              {
                  j = context.getContext().getLightValue();
              }
              return i << 20 | j << 4;
          }).GetBlockState(context -> {
            if (context.getContext().getPos().getX() != 0 || context.getContext().getPos().getY() != 0 || context.getContext().getPos().getZ() != 0)
            {
                return IBlockState.defaultState();
            }

            return state;
          })
          .IsAirBlock(context -> context.getInstance().getBlockState(context.getContext().getPos()).getBlock().isAir())
                                                      .GetBiome(context -> IBiome.getPlains())
          .GetStrongPower(context -> 15)
          .GetWorldType(context -> IDimensionType.getDefault())
          .IsSideSolid(context -> context.getInstance().getBlockState(context.getContext().getPos()).isSideSolid(context.getInstance(), context.getContext().getPos(), context.getContext().getSide()))
          .build(new DummyInstanceData());

        IOpenGl.pushMatrix();
        IOpenGl.translate(0f, 0f, 0f);
        IOpenGl.scale(14.0F, 14.0F, -14.0F);
        IOpenGl.rotate(210.0F, 1.0F, 0.0F, 0.0F);
        IOpenGl.rotate(45.0F, 0.0F, 1.0F, 0.0F);

        IBufferBuilder buf = ITessellator.getInstance().getBuffer();

        buf.begin(IOpenGl.getOpenGlQuadsRenderMode(), IVertexFormat.block());

        try
        {
            IGameEngine.getInstance().getBlockRendererDispatcher().renderBlock(state, IBlockCoordinate.create(0,0,0), dimensionReader, buf);
        }
        catch (Throwable t)
        {
            if (t instanceof VirtualMachineError || t instanceof LinkageError)
            {
                throw (Error) t;
            }
            // TODO: draw something to indicate it's broken
        }
        ITessellator.getInstance().draw();

        IOpenGl.popMatrix();
    }

    @Override
    public void drawSlotContent(@NotNull final IUIElement element)
    {
        if (!(element instanceof Slot))
        {
            return;
        }

        final Slot slot = (Slot) element;

        final IGuiContainer<BlockOutGuiData> gui = (IGuiContainer<BlockOutGuiData>) IGameEngine.getInstance().getCurrentGui();

        int x = 1;
        int y = 1;
        final IIdentifier inventoryId = slot.getInventoryId();
        final int inventoryIndex = slot.getInventoryIndex();
        IItemStack itemstack = gui.getInstanceData().getKey().getItemHandlerManager().getItemHandlerFromId(inventoryId).getStackInSlot(inventoryIndex);
        ISlot<?> slotIn = gui.getContainer().getSlotById(slot.getSlotIndex());

        boolean flag = false;
        boolean isDraggingStartSlot = slotIn == gui.getClickedSlot() && !gui.getDraggedStack().isEmpty() && !gui.isRightMouseClicked();
        IItemStack itemstack1 = gui.getGameEngine().getSinglePlayerPlayerEntity().getInventory().getItemStack();
        String s = null;

        if (slotIn == gui.getClickedSlot() && !gui.getDraggedStack().isEmpty() && gui.isRightMouseClicked() && !itemstack.isEmpty())
        {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        }
        else if (gui.isDragSplitting() && gui.getSlotsUsedInDragSplitting().contains(slotIn) && !itemstack1.isEmpty())
        {
            if (gui.getSlotsUsedInDragSplitting().size() == 1)
            {
                return;
            }

            if (IContainer.canAddItemToSlot(slotIn, itemstack1, true) && gui.getContainer().canDragIntoSlot(slotIn))
            {
                itemstack = itemstack1.copy();
                flag = true;
                IContainer.computeStackSize(gui.getSlotsUsedInDragSplitting(),
                  gui.getDragSplittingLimit(),
                  itemstack,
                  slotIn.getContainedStack().isEmpty() ? 0 : slotIn.getContainedStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k)
                {
                    s = ITextFormatting.yellow().toString() + k;
                    itemstack.setCount(k);
                }
            }
            else
            {
                gui.getSlotsUsedInDragSplitting().remove(slotIn);
                gui.updateDragSplitting();
            }
        }

        gui.getItemRenderer().setZLevel(100.0f);

        if (itemstack.isEmpty() && slotIn.isEnabled())
        {
            ISprite textureatlassprite = slotIn.getBackgroundTexture();

            if (textureatlassprite != null)
            {
                IOpenGl.disableLighting();
                gui.getGameEngine().getTextureManager().bindTexture(slotIn.getIdentifierOfBackgroundLocation());
                gui.drawTexturedModalRect(x, y, textureatlassprite, 16, 16);
                IOpenGl.enableLighting();
                isDraggingStartSlot = true;
            }
        }

        if (!isDraggingStartSlot)
        {
            if (flag)
            {
                drawRect(x, y, x + 16, y + 16, IColor.create(-2130706433));
            }

            IOpenGl.enableDepth();
            drawItemStack(itemstack, x, y, s);
        }

        gui.getItemRenderer().setZLevel(0f);
    }

    /**
     * Draws the slot overlay.
     *
     * @param element The slot
     */
    @Override
    public void drawSlotMouseOverlay(@NotNull final IUIElement element)
    {
        if (!(element instanceof Slot))
        {
            return;
        }

        final Slot slot = (Slot) element;
        
        final IGuiContainer<BlockOutGuiData> gui = (IGuiContainer<BlockOutGuiData>) IGameEngine.getInstance().getCurrentGui();

        if (!slot.getAbsoluteBoundingBox().includes(getMousePosition()))
        {
            return;
        }

        gui.setHoveredSlot(gui.getContainer().getSlotById(slot.getSlotIndex()));
        IOpenGl.disableLighting();
        IOpenGl.disableDepth();
        int x = 1;
        int y = 1;
        IOpenGl.colorMask(true, true, true, false);
        this.drawGradientRect(0, x, y, x + 16, y + 16, -2130706433, -2130706433);
        IOpenGl.colorMask(true, true, true, true);
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
    }

    @Override
    public void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        IOpenGl.disableTexture2D();
        IOpenGl.enableBlend();
        IOpenGl.disableAlpha();
        IOpenGl.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestinationFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestinationFactor.ZERO);
        IOpenGl.shadeModel(7425);
        ITessellator tessellator = ITessellator.getInstance();
        IBufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, IVertexFormat.positionColor());
        bufferbuilder.pos((double) right, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        IOpenGl.shadeModel(7424);
        IOpenGl.disableBlend();
        IOpenGl.enableAlpha();
        IOpenGl.enableTexture2D();
    }
    
    /**
     * Returns the mouse position for rendering.
     *
     * @return The mouse position.
     */
    @Override
    public Vector2d getMousePosition()
    {
        return lastKnownMousePosition;
    }

    /**
     * Sets the mouse position for rendering.
     *
     * @param mousePosition The mouse position.
     */
    @Override
    public void setMousePosition(@NotNull final Vector2d mousePosition)
    {
        this.lastKnownMousePosition = mousePosition;
    }
}
