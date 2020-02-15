package com.ldtteam.blockout.font;

import com.ldtteam.blockout.util.color.ColorUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.texture.TextureManager;

import javax.annotation.Nonnull;
import java.util.List;

public class MultiColoredFontRenderer extends FontRenderer {

    private boolean dropShadow;
    private int state = 0;
    private int red;
    private int green;
    private int blue;

    public MultiColoredFontRenderer(final TextureManager textureManagerIn, final Font fontIn) {
        super(textureManagerIn, fontIn);
    }

    @Override
    public List<String> listFormattedStringToWidth(final String str, final int wrapWidth) {
        return super.listFormattedStringToWidth(str, wrapWidth);
    }

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
        String s = "";
        int i = 0;
        int j = text.length();

        while((i < j - 1)) {
            char c = text.charAt(i);
            // vanilla formatting
            if(c == 167) {

                char c0 = text.charAt(i + 1);

                if(c0 >= 48 && c0 <= 57 || c0 >= 97 && c0 <= 102 || c0 >= 65 && c0 <= 70) {
                    s = "\u00a7" + c0;
                    i++;
                }
                else if(c0 >= 107 && c0 <= 111 || c0 >= 75 && c0 <= 79 || c0 == 114 || c0 == 82) {
                    s = s + "\u00a7" + c0;
                    i++;
                }
            }
            // custom formatting
            else if((int) c >= ColorUtils.MARKER && (int) c <= ColorUtils.MARKER + 0xFF) {
                s = String.format("%s%s%s", c, text.charAt(i + 1), text.charAt(i + 2));
                i += 2;
            }
            i++;
        }

        return s;
    }

    @Override
    public int renderString(final String text, final float x, final float y, final int color, final boolean dropShadow, final Matrix4f matrix, final IRenderTypeBuffer buffer, final boolean p_228079_8_, final int p_228079_9_, final int p_228079_10_) {
        this.dropShadow = dropShadow;
        return super.renderString(text, x, y, color, dropShadow, matrix, buffer, p_228079_8_, p_228079_9_, p_228079_10_);
    }


}
