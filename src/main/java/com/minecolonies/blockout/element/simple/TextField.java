package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.input.IKeyAcceptingUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.style.core.resources.core.IResource;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.TextField.*;

public class TextField extends AbstractSimpleUIElement implements IDrawableUIElement, IClickAcceptingUIElement, IKeyAcceptingUIElement
{
    @NotNull
    private IDependencyObject<String> contents;

    public TextField(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent)
    {
        super(KEY_TEXT_FIELD, style, id, parent);

        this.contents = DependencyObjectHelper.createFromValue("");
    }

    public TextField(
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
        super(KEY_TEXT_FIELD, style, id, parent, alignments, dock, margin, elementSize, dataContext, visible, enabled);
        this.contents = contents;
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
          .drawSplitString(getContents(), 0, 0, (int) getElementSize().getX(), 0);

        BoundingBox box = getLocalBoundingBox();

        if (this.isVisible())
        {

            //controller.drawTexturedModalRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
            //controller.drawTexturedModalRect(this.x, this.y, this.x + this.w, this.y + this.height, -16777216);


        }



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

    public String getContents()
    {
        return contents.get(getDataContext());
    }

    public void setContents(String contents)
    {
        this.contents = DependencyObjectHelper.createFromValue(contents);
    }

    @Override
    public boolean canAcceptMouseInput(final int localX, final int localY, final MouseButton button)
    {
        return true;
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {

    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {

    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {

    }

    @Override
    public boolean canAcceptKeyInput(final int character, final KeyboardKey key)
    {
        return true;
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {
        this.setContents(getContents() + (char) character);
    }

    @NotNull
    @Override
    public <T extends IResource> T getResource(final ResourceLocation resourceId) throws IllegalArgumentException
    {
        return null;
    }

    public static class TextFieldConstructionDataBuilder extends SimpleControlConstructionDataBuilder<TextFieldConstructionDataBuilder, TextField>
    {

        protected TextFieldConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, TextField.class);
        }

        @NotNull
        public TextFieldConstructionDataBuilder withDependentContents(@NotNull final IDependencyObject<String> contents)
        {
            return withDependency("contents", contents);
        }
    }

    public static class Factory implements IUIElementFactory<TextField>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_TEXT_FIELD;
        }

        @NotNull
        @Override
        public TextField readFromElementData(@NotNull final IUIElementData elementData)
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

            return new TextField(
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
        public void writeToElementData(@NotNull final TextField element, @NotNull final IUIElementDataBuilder builder)
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
