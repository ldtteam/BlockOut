package com.minecolonies.blockout.render.standard;

import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.render.core.IScissoringController;
import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.math.BoundingBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public class RenderingController implements IRenderingController
{

    @NotNull
    protected static RenderItem ITEMRENDERER = Minecraft.getMinecraft().getRenderItem();

    private final IScissoringController scissoringController = new ScissoringController(this);

    @Override
    public IScissoringController getScissoringController()
    {
        return scissoringController;
    }

    /**
     * Convenient helper function to bind the texture to a String adress
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

    /**
     * Helper function copied from the gui class to make it possible to use it outside of a gui class.
     * <p>
     * TRhe function comes with regards to the Minecraft Team
     *
     * @param x The x offset
     * @param y The y offset
     * @param z The z offset
     * @param w The total Width
     * @param h The total Height
     * @param u The X Offset in the currently loaded GL Image.
     * @param v The Y Offset in the currently loaded GL Iamge
     */
    @Override
    public void drawTexturedModalRect(int x, int y, int z, int u, int v, int w, int h)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) (x + 0), (double) (y + h), (double) z)
          .tex((double) ((float) (u + 0) * f), (double) ((float) (v + h) * f1))
          .endVertex();
        worldrenderer.pos((double) (x + w), (double) (y + h), (double) z)
          .tex((double) ((float) (u + w) * f), (double) ((float) (v + h) * f1))
          .endVertex();
        worldrenderer.pos((double) (x + w), (double) (y + 0), (double) z)
          .tex((double) ((float) (u + w) * f), (double) ((float) (v + 0) * f1))
          .endVertex();
        worldrenderer.pos((double) (x + 0), (double) (y + 0), (double) z).tex((double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1)).endVertex();
        tessellator.draw();
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
     * <p/>
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
     * Helper function copied from the gui class to make it possible to use it outside of a gui class.
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
     * @param box        The plane to fill on the screen
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
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(box.getLowerRightCoordinate().getX(), box.getUpperLeftCoordinate().getY(), (double) z).color(f3, f2, f1, f).endVertex();
        worldrenderer.pos(box.getUpperLeftCoordinate().getX(), box.getUpperLeftCoordinate().getY(), (double) z).color(f3, f2, f1, f).endVertex();
        worldrenderer.pos(box.getUpperLeftCoordinate().getX(), box.getLowerRightCoordinate().getY(), (double) z).color(f7, f6, f5, f4).endVertex();
        worldrenderer.pos(box.getLowerRightCoordinate().getX(), box.getLowerRightCoordinate().getY(), (double) z).color(f7, f6, f5, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
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
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
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
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
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
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
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
        ITEMRENDERER.renderItemOverlayIntoGUI(font, stack, x, y - 8, altText);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}
