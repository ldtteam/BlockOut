package com.minecolonies.blockout.element.advanced;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.binding.property.PropertyCreationHelper;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.TemplateInstance.CONST_TEMPLATE;
import static com.minecolonies.blockout.util.Constants.Controls.TemplateInstance.KEY_TEMPLATE_INSTANCE;

public class TemplateInstance extends AbstractChildrenContainingUIElement
{

    public static final class TemplateInstanceConstructionDataBuilder
      extends AbstractChildrenContainingUIElement.SimpleControlConstructionDataBuilder<TemplateInstanceConstructionDataBuilder, TemplateInstance>
    {

        protected TemplateInstanceConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data)
        {
            super(controlId, data, TemplateInstance.class);
        }

        @NotNull
        public TemplateInstanceConstructionDataBuilder withDependentTemplateResource(@NotNull final IDependencyObject<ResourceLocation> iconResource)
        {
            return withDependency("templateResource", iconResource);
        }

        @NotNull
        public TemplateInstanceConstructionDataBuilder withTemplateResource(@NotNull final ResourceLocation iconResource)
        {
            return withDependency("templateResource", DependencyObjectHelper.createFromValue(iconResource));
        }
    }

    public static final class Factory implements IUIElementFactory<TemplateInstance>
    {

        /**
         * Returns the type that this factory builds.
         *
         * @return The type.
         */
        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_TEMPLATE_INSTANCE;
        }

        /**
         * Creates a new {@link List} from the given {@link IUIElementData}.
         *
         * @param elementData The {@link IUIElementData} which contains the data that is to be constructed.
         * @return The {@link List} that is stored in the {@link IUIElementData}.
         */
        @NotNull
        @Override
        public TemplateInstance readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getElementId();
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<AxisDistance> padding = elementData.getBoundAxisDistanceAttribute(CONST_PADDING);
            final IDependencyObject<Vector2d> elementSize = elementData.getBoundVector2dAttribute(CONST_ELEMENT_SIZE);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> templateResource = elementData.getBoundResourceLocationAttribute(CONST_TEMPLATE);

            final TemplateInstance templateInstance = new TemplateInstance(
              style,
              id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              elementSize,
              padding,
              dataContext,
              visible,
              enabled,
              templateResource
            );

            return templateInstance;
        }

        /**
         * Populates the given {@link IUIElementDataBuilder} with the data from {@link List} so that
         * the given {@link List} can be reconstructed with {@link #readFromElementData(IUIElementData)} created by
         * the given {@link IUIElementDataBuilder}.
         *
         * @param element The {@link List} to write into the {@link IUIElementDataBuilder}
         * @param builder The {@link IUIElementDataBuilder} to write the {@link List} into.
         */
        @Override
        public void writeToElementData(@NotNull final TemplateInstance element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addAxisDistance(CONST_PADDING, element.getPadding())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_TEMPLATE, element.getTemplateResource());

            element.values().forEach(child -> {
                builder.addChild(BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(child));
            });
        }
    }

    private ResourceLocation                    resolvedTemplateResource;
    private IDependencyObject<ResourceLocation> templateResource;

    public TemplateInstance(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> templateResource)
    {
        super(KEY_TEMPLATE_INSTANCE, style, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

        this.templateResource = templateResource;
    }

    public TemplateInstance(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent)
    {
        super(KEY_TEMPLATE_INSTANCE, style, id, parent);

        this.templateResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (resolvedTemplateResource != getTemplateResource())
        {
            updateManager.markDirty();
            this.clear();

            final IUIElement element = BlockOut.getBlockOut().getProxy().getTemplateEngine().generateFromTemplate(
              this,
              PropertyCreationHelper.create(
                parentContext -> Optional.of(parentContext),
                null
              ),
              resolvedTemplateResource,
              getId() + "_instance");

            put(element.getId(), element);
        }
    }

    public ResourceLocation getTemplateResource()
    {
        return templateResource.get(getDataContext());
    }

    public void setTemplateResource(@NotNull final ResourceLocation templateResource)
    {
        this.templateResource = DependencyObjectHelper.createFromValue(templateResource);
    }
}
