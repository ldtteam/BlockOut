package com.ldtteam.blockout.font;

import com.ldtteam.blockout.util.color.Color;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.ldtteam.blockout.util.color.ColorUtils.MARKER;

@OnlyIn(Dist.CLIENT)
public class MultiColoredFont extends Font {

    private final Font wrappedFont;
    private int findGlyphState = 0;
    private Color findGlyphColor = new Color(0);
    private int getGlyphState = 0;
    private Color getGlyphColor = new Color(0);

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
            switch(findGlyphState) {
                case 0:
                    findGlyphColor = new Color(value, findGlyphColor.getGreen(), findGlyphColor.getBlue(), findGlyphColor.getAlpha());
                    break;
                case 1:
                    findGlyphColor = new Color(findGlyphColor.getRed(), value, findGlyphColor.getBlue(), findGlyphColor.getAlpha());
                    break;
                case 2:
                    findGlyphColor = new Color(findGlyphColor.getRed(), findGlyphColor.getGreen(), value, findGlyphColor.getAlpha());
                    break;
                case 3:
                    findGlyphColor = new Color(findGlyphColor.getRed(), findGlyphColor.getGreen(), findGlyphColor.getBlue(), value);
                default:
                    this.findGlyphColor = new Color(0);
                    this.findGlyphState = 0;
                    return new EmptyGlyphReference();
            }

            findGlyphState = ++findGlyphState % 4;
            if (findGlyphState != 0)
                return new EmptyGlyphReference();

            final Color createdColor = new Color(findGlyphColor.getRGB());
            findGlyphColor = new Color(0);
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
    public TexturedGlyph getGlyph(final char letter) {
        if((int) letter >= MARKER && (int) letter <= MARKER + 0xFF) {
            int value = letter & 0xFF;
            switch(getGlyphState) {
                case 0:
                    getGlyphColor = new Color(value, getGlyphColor.getGreen(), getGlyphColor.getBlue(), getGlyphColor.getAlpha());
                    break;
                case 1:
                    getGlyphColor = new Color(getGlyphColor.getRed(), value, getGlyphColor.getBlue(), getGlyphColor.getAlpha());
                    break;
                case 2:
                    getGlyphColor = new Color(getGlyphColor.getRed(), getGlyphColor.getGreen(), value, getGlyphColor.getAlpha());
                    break;
                case 3:
                    getGlyphColor = new Color(getGlyphColor.getRed(), getGlyphColor.getGreen(), getGlyphColor.getBlue(), value);
                default:
                    this.getGlyphColor = new Color(0);
                    this.getGlyphState = 0;
                    return new EmptyGlyph();
            }

            getGlyphState = ++getGlyphState % 4;
            if (getGlyphState != 0)
                return new EmptyGlyph();

            final Color createdColor = new Color(getGlyphColor.getRGB());
            getGlyphColor = new Color(0);
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

        return this.wrappedFont.getGlyph(letter);
    }

    @Override
    public TexturedGlyph obfuscate(final IGlyph glyph) {
        if (glyph instanceof EmptyGlyphReference)
            return new EmptyGlyph();
        
        if (glyph instanceof ColorGlyph)
            return (TexturedGlyph) glyph;

        return this.wrappedFont.obfuscate(glyph);
    }

    @Override
    public TexturedGlyph getWhiteGlyph() {
        return this.wrappedFont.getWhiteGlyph();
    }

    public static final class ColorGlyph extends EmptyGlyph implements IGlyph {

        private final Color color;

        public ColorGlyph(final Color color) {
            super();
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
