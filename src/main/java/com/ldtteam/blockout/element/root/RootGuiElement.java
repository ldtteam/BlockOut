package com.ldtteam.blockout.element.root;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementBuilder;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.util.Constants;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.Root.KEY_ROOT;

public class RootGuiElement extends AbstractChildrenContainingUIElement
{

    @NotNull
    private IUIManager manager;

    public RootGuiElement(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        super(KEY_ROOT, style, KEY_ROOT.getPath(), null, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

        this.setParent(this);
    }

    public RootGuiElement()
    {
        super(KEY_ROOT, DependencyObjectHelper.createFromValue(Constants.Styles.CONST_DEFAULT), KEY_ROOT.getPath(), null);

        this.setParent(this);
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        return dataContext.get(new Object());
    }

    @NotNull
    @Override
    public IUIManager getUiManager()
    {
        return manager;
    }

    public void setUiManager(@NotNull final IUIManager manager)
    {
        this.manager = manager;
    }

    public static class Factory implements IUIElementFactory<RootGuiElement>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_ROOT;
        }

        @NotNull
        @Override
        public RootGuiElement readFromElementData(@NotNull final IUIElementData elementData, @NotNull final IBindingEngine engine)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getFromRawDataWithDefault(CONST_STYLE_ID, IUIElementDataComponent::isString, engine, Constants.Styles.CONST_DEFAULT);
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getFromRawDataWithDefault(CONST_ALIGNMENT, IUIElementDataComponent::isString, engine, EnumSet.of(Alignment.LEFT, Alignment.RIGHT));
            final IDependencyObject<Dock> dock = elementData.getFromRawDataWithDefault(CONST_DOCK, IUIElementDataComponent::isString, engine, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<AxisDistance> padding = elementData.getBoundAxisDistanceAttribute(CONST_PADDING);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);

            RootGuiElement element = new RootGuiElement(
              style,
              alignments,
              dock,
              margin,
              elementSize,
              padding,
              dataContext,
              visible,
              enabled);

            elementData.getChildren(element).forEach(childData -> {
                IUIElement child = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(childData);
                element.put(child.getId(), child);
            });

            return element;
        }

        @Override
        public void writeToElementData(@NotNull final RootGuiElement element, @NotNull final IUIElementBuilder builder)
        {

        }

        @NotNull
        @Override
        public RootGuiElement readFromElementData(@NotNull final IUIElementData elementData)
        {

        }

        @Override
        public void writeToElementData(@NotNull final RootGuiElement element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addAxisDistance(CONST_PADDING, element.getPadding())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled());

            element.values().forEach(child -> {
                builder.addChild(BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(child));
            });
        }
    }

    public static final class RootGuiConstructionDataBuilder extends SimpleControlConstructionDataBuilder<RootGuiConstructionDataBuilder, RootGuiElement>
    {

        public RootGuiConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, RootGuiElement.class);
        }
    }
}
