package com.ldtteam.blockout.font;

import com.ldtteam.blockout.util.color.Color;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.ldtteam.blockout.util.color.ColorUtils.MARKER;

@OnlyIn(Dist.CLIENT)
public class MultiColoredFont extends Font {

    private final Font wrappedFont;
    private int state = 0;
    private Color currentColor = new Color(0);

    @SuppressWarnings("ConstantConditions")
    public MultiColoredFont(final Font wrappedFont) {
        super(null, null);

        this.wrappedFont = wrappedFont;
    }

    @Override
    public void setGlyphProviders(final List<IGlyphProvider> glyphProvidersIn) {
        this.wrappedFont.setGlyphProviders(glyphProvidersIn);
    }

    @Override
    public void close() {
        this.wrappedFont.close();
    }

    @Override
    public IGlyph findGlyph(final char letter) {
        if((int) letter >= MARKER && (int) letter <= MARKER + 0xFF) {
            int value = letter & 0xFF;
            switch(state) {
                case 0:
                    currentColor = new Color(letter, currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
                    break;
                case 1:
                    currentColor = new Color(currentColor.getRed(), letter, currentColor.getBlue(), currentColor.getAlpha());
                    break;
                case 2:
                    currentColor = new Color(currentColor.getRed(), currentColor.getGreen(), letter, currentColor.getAlpha());
                    break;
                case 3:
                    currentColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), letter);
                default:
                    this.currentColor = new Color(0);
                    return new EmptyGlyphReference();
            }

            state = ++state % 4;

            final Color createdColor = new Color(currentColor.getRGB());
            currentColor = new Color(0);
            return new ColorGlyph(createdColor);

            /*int color = currentColor.getRGB();
            if((color & -67108864) == 0) {
                color |= -16777216;
            }

            if(dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            this.setColor(((color >> 16) & 255) / 255f,
                    ((color >> 8) & 255) / 255f,
                    ((color >> 0) & 255) / 255f,
                    ((color >> 24) & 255) / 255f);
            return 0;*/
        }

        return this.wrappedFont.findGlyph(letter);
    }

    @Override
    public TexturedGlyph getGlyph(final char character) {
         return this.wrappedFont.getGlyph(character);
    }

    @Override
    public TexturedGlyph obfuscate(final IGlyph glyph) {
        return this.wrappedFont.obfuscate(glyph);
    }

    @Override
    public TexturedGlyph getWhiteGlyph() {
        return this.wrappedFont.getWhiteGlyph();
    }

    public static final class ColorGlyph extends TexturedGlyph implements IGlyph {

        private final Color color;

        public ColorGlyph(final Color color) {
            super(RenderType.LINES, RenderType.LINES, 0,0,0,0,0,0,0,0);
            this.color = color;
        }

        @Override
        public float getAdvance() {
            return 0;
        }

        public Color getColor() {
            return color;
        }
    }

    public static final class EmptyGlyphReference implements IGlyph {

        @Override
        public float getAdvance() {
            return 0;
        }
    }
}
