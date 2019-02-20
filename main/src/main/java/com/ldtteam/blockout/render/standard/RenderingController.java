package com.ldtteam.blockout.render.standard;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.management.render.IRenderManager;
import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.blockout.gui.BlockOutGui;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.render.core.IScissoringController;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderingController implements IRenderingController
{

    @NotNull
    private final static RenderItem ITEMRENDERER = Minecraft.getMinecraft().getRenderItem();

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
        bindTexture(new ResourceLocation(textureAddress));
    }

    /**
     * Convenient helper function to bind the texture using a ResourceLocation
     *
     * @param textureLocation The ResourceLocation of the Texture to bind.
     */
    @Override
    public void bindTexture(@NotNull ResourceLocation textureLocation)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(textureLocation);
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
        IOpenGl.blendFunc(IOpenGl.SourceFactor.SRC_ALPHA, IOpenGl.DestFactor.ONE_MINUS_SRC_ALPHA);
        IOpenGl.color(1, 1, 1, 1);

        double x = origin.getX();
        double y = origin.getY();
        double width = size.getX();
        double height = size.getY();
        double textureX = inTexturePosition.getX();
        double textureY = inTexturePosition.getY();
        double textureWidth = inTextureSize.getX() / textureSize.getX();
        double textureHeight = inTextureSize.getY() / textureSize.getY();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
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
    public void drawFluid(@Nullable FluidStack fluid, int x, int y, int z, int w, int h)
    {
        if (fluid == null || fluid.getFluid() == null)
        {
            return;
        }

        TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());

        if (texture == null)
        {
            texture = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)).getAtlasSprite("missingno");
        }

        final Color fluidColor = new Color(fluid.getFluid().getColor(fluid));

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
    public void drawCutIcon(@NotNull TextureAtlasSprite icon, int x, int y, int z, int w, int h, int cutOffVertical)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

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
    public void drawTexturedModelRectFromIcon(int x, int y, int z, @NotNull TextureAtlasSprite icon, int w, int h)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
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
    public void drawColoredRect(@NotNull BoundingBox box, int z, @NotNull Color c)
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
    public void drawGradiendColoredRect(@NotNull BoundingBox box, int z, @NotNull Color colorStart, @NotNull Color colorEnd)
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
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
    public void drawRect(final double left, final double top, final double right, final double bottom, @NotNull final Color color)
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        IOpenGl.enableBlend();
        IOpenGl.disableTexture2D();
        IOpenGl.tryBlendFuncSeparate(IOpenGl.SourceFactor.SRC_ALPHA,
          IOpenGl.DestFactor.ONE_MINUS_SRC_ALPHA,
          IOpenGl.SourceFactor.ONE,
          IOpenGl.DestFactor.ZERO);
        IOpenGl.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
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
    public void drawItemStack(@NotNull ItemStack stack, int x, int y)
    {
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        FontRenderer font = null;
        if (!stack.isEmpty())
        {
            font = stack.getItem().getFontRenderer(stack);
        }

        if (font == null)
        {
            font = Minecraft.getMinecraft().fontRenderer;
        }

        ITEMRENDERER.renderItemAndEffectIntoGUI(stack, x, y);
        ITEMRENDERER.renderItemOverlayIntoGUI(font, stack, x, y, "");

        RenderHelper.disableStandardItemLighting();
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
    public void drawItemStack(@NotNull ItemStack stack, int x, int y, String altText)
    {
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        FontRenderer font = null;
        if (!stack.isEmpty())
        {
            font = stack.getItem().getFontRenderer(stack);
        }

        if (font == null)
        {
            font = Minecraft.getMinecraft().fontRenderer;
        }

        ITEMRENDERER.renderItemAndEffectIntoGUI(stack, x, y);
        ITEMRENDERER.renderItemOverlayIntoGUI(font, stack, x, y, altText);

        RenderHelper.disableStandardItemLighting();
        IOpenGl.enableDepth();
        IOpenGl.disableLighting();
    }

    @Override
    public void drawBlockState(@NotNull final IBlockState state, final int x, final int y)
    {
        RenderHelper.disableStandardItemLighting();

        final IBlockAccess blockAccess = new IBlockAccess()
        {
            @javax.annotation.Nullable
            @Override
            public TileEntity getTileEntity(final BlockPos pos)
            {
                if (pos.getX() != 0 || pos.getY() != 0 || pos.getX() != 0)
                {
                    return null;
                }

                //TODO: Pull in structurize compatible dummy world. Or make dummy world library.
                return state.getBlock().createTileEntity(null, state);
            }

            @Override
            public int getCombinedLight(final BlockPos pos, final int lightValue)
            {
                int i = 15;
                int j = 15;
                if (j < lightValue)
                {
                    j = lightValue;
                }
                return i << 20 | j << 4;
            }

            @Override
            public IBlockState getBlockState(final BlockPos pos)
            {
                if (pos.getX() != 0 || pos.getY() != 0 || pos.getX() != 0)
                {
                    return Blocks.AIR.getDefaultState();
                }

                return state;
            }

            @Override
            public boolean isAirBlock(final BlockPos pos)
            {
                if (pos.getX() != 0 || pos.getY() != 0 || pos.getX() != 0)
                {
                    return true;
                }

                return state.getBlock() instanceof BlockAir;
            }

            @Override
            public Biome getBiome(final BlockPos pos)
            {
                return Biomes.PLAINS;
            }

            @Override
            public int getStrongPower(final BlockPos pos, final EnumFacing direction)
            {
                return 15;
            }

            @Override
            public WorldType getWorldType()
            {
                return WorldType.DEFAULT;
            }

            @Override
            public boolean isSideSolid(final BlockPos pos, final EnumFacing side, final boolean _default)
            {
                if (pos.getX() != 0 || pos.getY() != 0 || pos.getX() != 0)
                {
                    return Blocks.AIR.getDefaultState().isSideSolid(this, pos, side);
                }

                return state.isSideSolid(this, pos, side);
            }
        };

        IOpenGl.pushMatrix();
        IOpenGl.translate(0f, 0f, 0f);
        IOpenGl.scale(14.0F, 14.0F, -14.0F);
        IOpenGl.rotate(210.0F, 1.0F, 0.0F, 0.0F);
        IOpenGl.rotate(45.0F, 0.0F, 1.0F, 0.0F);

        BufferBuilder buf = Tessellator.getInstance().getBuffer();

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        try
        {
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, BlockPos.ORIGIN, blockAccess, buf);
        }
        catch (Throwable t)
        {
            if (t instanceof VirtualMachineError || t instanceof LinkageError)
            {
                throw (Error) t;
            }
            // TODO: draw something to indicate it's broken
        }
        Tessellator.getInstance().draw();

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

        if (!(renderManager.getGui() instanceof BlockOutGui))
        {
            throw new IllegalArgumentException("Can not draw slot contents on ClientSide Only guitemp.");
        }

        final BlockOutGui gui = (BlockOutGui) renderManager.getGui();

        int x = 1;
        int y = 1;
        final ResourceLocation inventoryId = slot.getInventoryId();
        final int inventoryIndex = slot.getInventoryIndex();
        ItemStack itemstack = gui.getKey().getItemHandlerManager().getItemHandlerFromId(inventoryId).getStackInSlot(inventoryIndex);
        net.minecraft.inventory.Slot slotIn = gui.inventorySlots.getSlot(slot.getSlotIndex());

        boolean flag = false;
        boolean isDraggingStartSlot = slotIn == gui.clickedSlot && !gui.draggedStack.isEmpty() && !gui.isRightMouseClick;
        ItemStack itemstack1 = gui.mc.player.inventory.getItemStack();
        String s = null;

        if (slotIn == gui.clickedSlot && !gui.draggedStack.isEmpty() && gui.isRightMouseClick && !itemstack.isEmpty())
        {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        }
        else if (gui.dragSplitting && gui.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
        {
            if (gui.dragSplittingSlots.size() == 1)
            {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && gui.inventorySlots.canDragIntoSlot(slotIn))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(gui.dragSplittingSlots,
                  gui.dragSplittingLimit,
                  itemstack,
                  slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k)
                {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            }
            else
            {
                gui.dragSplittingSlots.remove(slotIn);
                gui.updateDragSplitting();
            }
        }

        gui.itemRender.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.isEnabled())
        {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

            if (textureatlassprite != null)
            {
                IOpenGl.disableLighting();
                gui.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                gui.drawTexturedModalRect(x, y, textureatlassprite, 16, 16);
                IOpenGl.enableLighting();
                isDraggingStartSlot = true;
            }
        }

        if (!isDraggingStartSlot)
        {
            if (flag)
            {
                drawRect(x, y, x + 16, y + 16, new Color(-2130706433));
            }

            IOpenGl.enableDepth();
            drawItemStack(itemstack, x, y, s);
        }

        gui.itemRender.zLevel = 0.0F;
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

        if (!(renderManager.getGui() instanceof BlockOutGui))
        {
            throw new IllegalArgumentException("Can not draw slot overlay on ClientSide Only guitemp.");
        }

        final BlockOutGui gui = (BlockOutGui) renderManager.getGui();

        if (!slot.getAbsoluteBoundingBox().includes(getMousePosition()))
        {
            return;
        }

        gui.hoveredSlot = gui.inventorySlots.getSlot(slot.getSlotIndex());
        IOpenGl.disableLighting();
        IOpenGl.disableDepth();
        int x = 1;
        int y = 1;
        IOpenGl.colorMask(true, true, true, false);
        gui.drawGradientRect(x, y, x + 16, y + 16, -2130706433, -2130706433);
        IOpenGl.colorMask(true, true, true, true);
        IOpenGl.enableLighting();
        IOpenGl.enableDepth();
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
