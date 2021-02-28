package com.ldtteam.blockout.font;

import com.ldtteam.blockout.util.color.Color;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
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

    public static final class ColorGlyph extends TexturedGlyph implements IGlyph {

        private final Color color;

        public ColorGlyph(final Color color) {
            super(RenderType.getText(new ResourceLocation("default/0")), RenderType.getTextSeeThrough(new ResourceLocation("default/0")), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.color = color;
        }

        @Override
        public void render(final boolean p_225595_1_, final float p_225595_2_, final float p_225595_3_, final Matrix4f p_225595_4_, final IVertexBuilder p_225595_5_, final float p_225595_6_, final float p_225595_7_, final float p_225595_8_, final float p_225595_9_, final int p_225595_10_) {
            //NOOP
        }

        @Override
        public void renderEffect(final Effect p_228162_1_, final Matrix4f p_228162_2_, final IVertexBuilder p_228162_3_, final int p_228162_4_) {
            //NOOP
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
