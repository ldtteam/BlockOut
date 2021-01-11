package com.ldtteam.blockout.font;

import com.ldtteam.blockout.util.color.Color;
import com.ldtteam.blockout.util.color.ColorUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiColoredFontRenderer extends FontRenderer {

    private static final Color WHITE_COLOR = new Color(255,255,255,255);
    private static final int WHITE_COLOR_INT = WHITE_COLOR.getRGB();

    private Color drawingColor = new Color(255, 255, 255, 255);


    public MultiColoredFontRenderer(final TextureManager textureManagerIn, final Font fontIn) {
        super(textureManagerIn, new MultiColoredFont(fontIn));
    }

    @NotNull
    @Override
    public List<String> listFormattedStringToWidth(@NotNull final String str, final int wrapWidth) {
        return super.listFormattedStringToWidth(str, wrapWidth);
    }

    @NotNull
    @Override
    public String wrapFormattedStringToWidth(final String str, final int wrapWidth) {
        int i = this.sizeStringToWidth(str, wrapWidth);

        if(str.length() <= i) {
            return str;
        }
        else {
            String s = str.substring(0, i);
            char c0 = str.charAt(i);
            boolean flag = c0 == 32 || c0 == 10;
            String s1 = getCustomFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
            return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
        }
    }

    public static String getCustomFormatFromString(String text) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        int j = text.length();

        while((i < j - 1)) {
            char c = text.charAt(i);
            // vanilla formatting
            if(c == 167) {

                char c0 = text.charAt(i + 1);

                if(c0 >= 48 && c0 <= 57 || c0 >= 97 && c0 <= 102 || c0 >= 65 && c0 <= 70) {
                    s = new StringBuilder("\u00a7" + c0);
                    i++;
                }
                else if(c0 >= 107 && c0 <= 111 || c0 >= 75 && c0 <= 79 || c0 == 114 || c0 == 82) {
                    s.append("\u00a7").append(c0);
                    i++;
                }
            }
            // custom formatting
            else if((int) c >= ColorUtils.MARKER && (int) c <= ColorUtils.MARKER + 0xFF) {
                s = new StringBuilder(String.format("%s%s%s", c, text.charAt(i + 1), text.charAt(i + 2)));
                i += 2;
            }
            i++;
        }

        return s.toString();
    }

    @Override
    public int renderString(@NotNull final String text, final float x, final float y, final int color, final boolean dropShadow, final Matrix4f matrix, @NotNull final IRenderTypeBuffer buffer, final boolean p_228079_8_, final int p_228079_9_, final int p_228079_10_) {
        this.drawingColor = new Color(color);
        return super.renderString(this.preProcessString(text, this.drawingColor), x, y, WHITE_COLOR_INT, dropShadow, matrix, buffer, p_228079_8_, p_228079_9_, p_228079_10_);
    }

    @Override
    public void drawGlyph(final TexturedGlyph p_228077_1_, final boolean p_228077_2_, final boolean p_228077_3_, final float p_228077_4_, final float p_228077_5_, final float p_228077_6_, @NotNull final Matrix4f p_228077_7_, @NotNull final IVertexBuilder p_228077_8_, final float p_228077_9_, final float p_228077_10_, final float p_228077_11_, final float p_228077_12_, final int p_228077_13_) {
        if (p_228077_1_ instanceof MultiColoredFont.ColorGlyph)
            this.drawingColor = ((MultiColoredFont.ColorGlyph) p_228077_1_).getColor();

        super.drawGlyph(p_228077_1_, p_228077_2_, p_228077_3_, p_228077_4_, p_228077_5_, p_228077_6_, p_228077_7_, p_228077_8_, this.drawingColor.getRedFloat(), this.drawingColor.getGreenFloat(), this.drawingColor.getBlueFloat(), this.drawingColor.getAlphaFloat(), p_228077_13_);
    }

    private String preProcessString(final String text, final Color defaultColor)
    {
        StringBuilder workingString = new StringBuilder();
        for (int index = 0; index < text.length(); index++) {
            final char charInString = text.charAt(index);

            if (charInString == 167 && index + 1 < text.length()) {
                TextFormatting textFormatting = TextFormatting.fromFormattingCode(text.charAt(index + 1));
                if (textFormatting != null) {
                    if (textFormatting == TextFormatting.RESET)
                    {
                        workingString.append(defaultColor.encodeColor());
                        workingString.append(textFormatting.toString());
                    }
                    else if (textFormatting.isNormalStyle()) {
                        if (textFormatting.getColor() != null)
                        {
                            int formattingColor = textFormatting.getColor();
                            workingString.append(new Color(formattingColor, 255).encodeColor());
                        }
                        else
                        {
                            workingString.append(textFormatting.toString());
                        }
                    } else if (textFormatting.getColor() != null) {
                        int formattingColor = textFormatting.getColor();
                        workingString.append(new Color(formattingColor, 255).encodeColor());
                    }
                }

                ++index;
            } else {
                workingString.append(charInString);
            }
        }

        workingString.append(WHITE_COLOR.encodeColor());
        workingString.append(TextFormatting.RESET.toString());

        return workingString.toString();
    }
}
