package com.minecolonies.blockout.render.core;

import com.minecolonies.blockout.element.simple.Slot;
import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface describing our rendering controller which is responsible for rendering content in the GUIs.
 */
@SideOnly(Side.CLIENT)
public interface IRenderingController
{
    /**
     * Get the scissoring controller.
     * @return an IScissoringController.
     */
    IScissoringController getScissoringController();

    /**
     * Bind a texture.
     * @param textureAddress the location string of it.
     */
    void bindTexture(@NotNull String textureAddress);

    /**
     * Bind a texture.
     * @param textureLocation the resource location of it.
     */
    void bindTexture(@NotNull ResourceLocation textureLocation);

    /**
     * Draw a modal texture.
     * @param origin from origin.
     * @param size with size.
     * @param inTexturePosition in texture position.
     * @param inTextureSize in texture size.
     * @param textureSize the size.
     */
    void drawTexturedModalRect(
      @NotNull final Vector2d origin,
      @NotNull final Vector2d size,
      @NotNull final Vector2d inTexturePosition,
      @NotNull final Vector2d inTextureSize,
      @NotNull final Vector2d textureSize);

    /**
     * Draw a fluid.
     * @param fluid the fluid.
     * @param x the x pos.
     * @param y the y pos.
     * @param z the z pos.
     * @param w the width.
     * @param h the height.
     */
    void drawFluid(@Nullable FluidStack fluid, int x, int y, int z, int w, int h);

    /**
     * Draw an icon.
     * @param pIcon the icon.
     * @param pX the x pos.
     * @param pY the y pos.
     * @param pZ the z pos.
     * @param pWidth the width.
     * @param pHeight the height.
     * @param pCutOffVertical the vertical cutOff.
     */
    void drawCutIcon(@NotNull TextureAtlasSprite pIcon, int pX, int pY, int pZ, int pWidth, int pHeight, int pCutOffVertical);

    /**
     * Draw a texture from an icon.
     * @param x the x pos.
     * @param y the y pos.
     * @param z the z pos.
     * @param icon the icon.
     * @param w the width.
     * @param h the height.
     */
    void drawTexturedModelRectFromIcon(int x, int y, int z, @NotNull TextureAtlasSprite icon, int w, int h);

    /**
     * Draw a colored rect.
     * @param box the bounding box (left,bottom, width,height)
     * @param z the z.
     * @param c the color.
     */
    void drawColoredRect(@NotNull BoundingBox box, int z, @NotNull Color c);

    /**
     * Draw a gradient rect.
     * @param box the bounding box (left,bottom, width,height)
     * @param z the z.
     * @param colorStart the start color.
     * @param colorEnd the end color.
     */
    void drawGradiendColoredRect(@NotNull BoundingBox box, int z, @NotNull Color colorStart, @NotNull Color colorEnd);

    /**
     * Draw a rect.
     * @param left the left pos.
     * @param top the top pos.
     * @param right the right pos.
     * @param bottom the bottom pos.
     * @param color the color.
     */
    void drawRect(double left, double top, double right, double bottom, @NotNull Color color);

    /**
     * Draw an itemStack.
     * @param stack the stack.
     * @param x the x pos.
     * @param y the y pos.
     */
    void drawItemStack(@NotNull ItemStack stack, int x, int y);

    /**
     * Draw an itemStack with alt text.
     * @param stack the stack.
     * @param x the x pos.
     * @param y the y pos.
     * @param altText the alt text.
     */
    void drawItemStack(@NotNull ItemStack stack, int x, int y, String altText);

    /**
     * Draws the slot contents.
     *
     * @param slot The slot.
     */
    void drawSlotContent(@NotNull final Slot slot);

    /**
     * Draws the slot overlay.
     *
     * @param slot The slot
     */
    void drawSlotMouseOverlay(@NotNull final Slot slot);

    /**
     * Returns the mouse position for rendering.
     *
     * @return The mouse position.
     */
    Vector2d getMousePosition();

    /**
     * Sets the mouse position for rendering.
     *
     * @param mousePosition The mouse position.
     */
    void setMousePosition(@NotNull final Vector2d mousePosition);

    /**
     * Sets the mouse position for rendering from coordinates.
     *
     * @param mouseX The mouse positions x coordinate.
     * @param mouseY The mouse positions y coordinate.
     */
    default void setMousePosition(@NotNull final int mouseX, @NotNull final int mouseY)
    {
        this.setMousePosition((double) mouseX, (double) mouseY);
    }

    /**
     * Sets the mouse position for rendering from accurate coordinates.
     *
     * @param mouseX The mouse positions x coordinate.
     * @param mouseY The mouse positions y coordinate.
     */
    default void setMousePosition(@NotNull final double mouseX, @NotNull final double mouseY)
    {
        this.setMousePosition(new Vector2d(mouseX, mouseY));
    }
}
