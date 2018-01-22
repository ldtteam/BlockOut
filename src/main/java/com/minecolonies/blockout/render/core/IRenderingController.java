package com.minecolonies.blockout.render.core;

import com.minecolonies.blockout.util.color.Color;
import com.minecolonies.blockout.util.math.BoundingBox;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public interface IRenderingController
{
    IScissoringController getScissoringController();

    void bindTexture(@NotNull String textureAddress);

    void bindTexture(@NotNull ResourceLocation textureLocation);

    void drawTexturedModalRect(int x, int y, int z, int u, int v, int w, int h);

    void drawFluid(@Nullable FluidStack fluid, int x, int y, int z, int w, int h);

    void drawCutIcon(@NotNull TextureAtlasSprite pIcon, int pX, int pY, int pZ, int pWidth, int pHeight, int pCutOffVertical);

    void drawTexturedModelRectFromIcon(int x, int y, int z, @NotNull TextureAtlasSprite icon, int w, int h);

    void drawColoredRect(@NotNull BoundingBox box, int z, @NotNull Color c);

    void drawGradiendColoredRect(@NotNull BoundingBox box, int z, @NotNull Color colorStart, @NotNull Color colorEnd);

    void drawItemStack(@NotNull ItemStack stack, int x, int y);

    void drawItemStack(@NotNull ItemStack stack, int x, int y, String altText);
}
