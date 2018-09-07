package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.Label.CONST_CONTENT;
import static com.minecolonies.blockout.util.Constants.Controls.Label.KEY_LABEL;

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
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled, final IDependencyObject<String> contents)
    {
        super(KEY_LABEL, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
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
          .drawSplitString(getTranslatedContents(), 0, 0, (int) getElementSize().getX(), 0);

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
        return contents.get(getDataContext());
    }

    public void setContents(String contents)
    {
        this.contents.set(getDataContext(), contents);
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

    public static class Factory implements IUIElementFactory<Label>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_LABEL;
        }

        @NotNull
        @Override
        public Label readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getElementId();
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<String> contents = elementData.getBoundStringAttribute(CONST_CONTENT);

            return new Label(
              style,
              id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              dataContext,
              visible,
              enabled,
              contents
            );
        }

        @Override
        public void writeToElementData(@NotNull final Label element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addString(CONST_CONTENT, element.getContents());
        }
    }
}
