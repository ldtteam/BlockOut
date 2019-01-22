package com.ldtteam.blockout.element.simple;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.drawable.IDrawableUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementDataBuilder;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.element.core.AbstractSimpleUIElement;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.Label.CONST_CONTENT;
import static com.ldtteam.blockout.util.Constants.Controls.Label.KEY_LABEL;

public class Label extends AbstractSimpleUIElement implements IDrawableUIElement
{
    private final Pattern TRANSLATION_RAW_PATTERN = Pattern.compile("(?<key>(\\$\\{(?<keydata>.*?)\\}))|(?<value>(.*?))");

    @NotNull
    private IDependencyObject<String> contents;

    public Label(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_LABEL, style, id, parent);

        this.contents = DependencyObjectHelper.createFromValue("");
    }

    public Label(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<String> contents)
    {
        super(KEY_LABEL, styleId, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.contents = contents;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (contents.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        controller.getScissoringController().focus(this);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        BlockOut.getBlockOut()
          .getProxy()
          .getFontRenderer()
          .drawSplitString(getTranslatedContents(), 0, 0, (int) getLocalBoundingBox().getSize().getX(), 0);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();

        controller.getScissoringController().pop();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    public String getTranslatedContents()
    {
        String rawContents = getContents();
        Matcher contentMatcher = TRANSLATION_RAW_PATTERN.matcher(rawContents);

        while (contentMatcher.find())
        {
            final String keyGroupMatching = contentMatcher.group("keydata");
            if (keyGroupMatching == null)
            {
                continue;
            }

            rawContents = rawContents.replace("${" + keyGroupMatching + "}", I18n.translateToLocal(keyGroupMatching));
            contentMatcher = TRANSLATION_RAW_PATTERN.matcher(rawContents);
        }

        return rawContents;
    }

    public String getContents()
    {
        return contents.get(this);
    }

    public void setContents(String contents)
    {
        this.contents.set(this, contents);
    }

    public static class LabelConstructionDataBuilder extends AbstractSimpleUIElement.SimpleControlConstructionDataBuilder<LabelConstructionDataBuilder, Label>
    {

        protected LabelConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, Label.class);
        }

        @NotNull
        public Label.LabelConstructionDataBuilder withDependentContents(@NotNull final IDependencyObject<String> contents)
        {
            return withDependency("contents", contents);
        }
    }

    public static class Factory extends AbstractSimpleUIElementFactory<Label>
    {

        protected Factory()
        {
            super((elementData, engine, id, parent, styleId, alignments, dock, margin, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<String> contents = elementData.getFromRawDataWithDefault(CONST_CONTENT, engine, "<UNKNOWN>");

                final Label element = new Label(
                  id,
                  parent,
                  styleId,
                  alignments,
                  dock,
                  margin,
                  elementSize,
                  dataContext,
                  visible,
                  enabled,
                  contents
                );

                return element;
            }, (element, builder) -> builder.addComponent(CONST_CONTENT, element.getContents()));
        }

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_LABEL;
        }
    }
}
