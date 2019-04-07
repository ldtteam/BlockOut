package com.ldtteam.blockout.render.core;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.color.IColor;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.jvoxelizer.block.state.IBlockState;
import com.ldtteam.jvoxelizer.client.renderer.texture.ISprite;
import com.ldtteam.jvoxelizer.fluid.IFluidStack;
import com.ldtteam.jvoxelizer.item.IItemStack;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface describing our rendering controller which is responsible for rendering content in the GUIs.
 */
public interface IRenderingController
{

    Vector2d getRenderingScalingFactor();

    /**
     * Get the scissoring controller.
     *
     * @return an IScissoringController.
     */
    IScissoringController getScissoringController();

    /**
     * Bind a texture.
     *
     * @param textureAddress the location string of it.
     */
    void bindTexture(@NotNull String textureAddress);

    /**
     * Bind a texture.
     *
     * @param textureLocation the resource location of it.
     */
    void bindTexture(@NotNull IIdentifier textureLocation);

    /**
     * Draw a modal texture.
     *
     * @param origin            from origin.
     * @param size              with size.
     * @param inTexturePosition in texture position.
     * @param inTextureSize     in texture size.
     * @param textureSize       the size.
     */
    void drawTexturedModalRect(
      @NotNull final Vector2d origin,
      @NotNull final Vector2d size,
      @NotNull final Vector2d inTexturePosition,
      @NotNull final Vector2d inTextureSize,
      @NotNull final Vector2d textureSize);

    /**
     * Draw a fluid.
     *
     * @param fluid the fluid.
     * @param x     the x pos.
     * @param y     the y pos.
     * @param z     the z pos.
     * @param w     the width.
     * @param h     the height.
     */
    void drawFluid(@Nullable IFluidStack fluid, int x, int y, int z, int w, int h);

    /**
     * Draw an icon.
     *
     * @param pIcon           the icon.
     * @param pX              the x pos.
     * @param pY              the y pos.
     * @param pZ              the z pos.
     * @param pWidth          the width.
     * @param pHeight         the height.
     * @param pCutOffVertical the vertical cutOff.
     */
    void drawCutIcon(@NotNull ISprite pIcon, int pX, int pY, int pZ, int pWidth, int pHeight, int pCutOffVertical);

    /**
     * Draw a texture from an icon.
     *
     * @param x    the x pos.
     * @param y    the y pos.
     * @param z    the z pos.
     * @param icon the icon.
     * @param w    the width.
     * @param h    the height.
     */
    void drawTexturedModelRectFromIcon(int x, int y, int z, @NotNull ISprite icon, int w, int h);

    /**
     * Draw a colored rect.
     *
     * @param box the bounding box (left,bottom, width,height)
     * @param z   the z.
     * @param c   the color.
     */
    void drawColoredRect(@NotNull BoundingBox box, int z, @NotNull IColor c);

    /**
     * Draw a gradient rect.
     *
     * @param box        the bounding box (left,bottom, width,height)
     * @param z          the z.
     * @param colorStart the start color.
     * @param colorEnd   the end color.
     */
    void drawGradiendColoredRect(@NotNull BoundingBox box, int z, @NotNull IColor colorStart, @NotNull IColor colorEnd);

    /**
     * Draw a rect.
     *
     * @param left   the left pos.
     * @param top    the top pos.
     * @param right  the right pos.
     * @param bottom the bottom pos.
     * @param color  the color.
     */
    void drawRect(double left, double top, double right, double bottom, @NotNull IColor color);

    /**
     * Draw an itemStack.
     *
     * @param stack the stack.
     * @param x     the x pos.
     * @param y     the y pos.
     */
    void drawItemStack(@NotNull IItemStack stack, int x, int y);

    /**
     * Draw an itemStack with alt text.
     *
     * @param stack   the stack.
     * @param x       the x pos.
     * @param y       the y pos.
     * @param altText the alt text.
     */
    void drawItemStack(@NotNull IItemStack stack, int x, int y, String altText);

    /**
     * Draw an BlockState in a given position
     *
     * @param state The blockstate
     * @param x     The x pos
     * @param y     The y pos
     */
    void drawBlockState(@NotNull IBlockState state, int x, int y);

    /**
     * Draws the slot contents.
     *
     * @param element The slot.
     */
    void drawSlotContent(@NotNull final IUIElement element);

    /**
     * Draws the slot overlay.
     *
     * @param element The slot
     */
    void drawSlotMouseOverlay(@NotNull final IUIElement element);

    void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor);

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
