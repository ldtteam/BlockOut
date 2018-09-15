package com.ldtteam.blockout.element.advanced;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.core.element.IUIElementHost;
import com.ldtteam.blockout.core.element.values.Alignment;
import com.ldtteam.blockout.core.element.values.AxisDistance;
import com.ldtteam.blockout.core.element.values.Dock;
import com.ldtteam.blockout.core.factory.IUIElementFactory;
import com.ldtteam.blockout.core.management.update.IUpdateManager;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.loader.IUIElementData;
import com.ldtteam.blockout.loader.IUIElementDataBuilder;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Function;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.TemplateInstance.CONST_TEMPLATE;
import static com.ldtteam.blockout.util.Constants.Controls.TemplateInstance.KEY_TEMPLATE_INSTANCE;

public class TemplateInstance extends AbstractChildrenContainingUIElement
{

    public static final class TemplateInstanceConstructionDataBuilder
      extends AbstractChildrenContainingUIElement.SimpleControlConstructionDataBuilder<TemplateInstanceConstructionDataBuilder, TemplateInstance>
    {

        public TemplateInstanceConstructionDataBuilder(
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

            //Client side additional check for loading incase we get send a instance that has already resolved.
            if (elementData.hasChildren())
            {
                //nullify the hasChanged.
                templateResource.get(templateInstance);

                elementData.getChildren(templateInstance).forEach(childData -> {
                    IUIElement child = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(childData);
                    templateInstance.put(child.getId(), child);
                });
            }

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

        getParent().getUiManager().getProfiler().startSection("Template instantiation");
        if (templateResource.hasChanged(getDataContext()))
        {
            getParent().getUiManager().getProfiler().startSection("Template instance creation.");
            updateManager.markDirty();
            this.clear();

            final ResourceLocation resolvedTemplateResource = getTemplateResource();

            final IUIElement element = BlockOut.getBlockOut().getProxy().getTemplateEngine().generateFromTemplate(
              this,
              DependencyObjectHelper.createFromGetterOnly(Function.identity(), new Object()),
              resolvedTemplateResource,
              getId() + "_instance");

            put(element.getId(), element);
            getParent().getUiManager().getProfiler().endSection();
        }
        getParent().getUiManager().getProfiler().endSection();
    }

    public ResourceLocation getTemplateResource()
    {
        return templateResource.get(this);
    }

    public void setTemplateResource(@NotNull final ResourceLocation templateResource)
    {
        this.templateResource.set(this, templateResource);
    }

    @Override
    public Vector2d  getMinimalContentSize()
    {
        getParent().getUiManager().getProfiler().startSection("Template minimal content size: " + getId());
        final Vector2d currentElementSize = getElementSize();
        if (currentElementSize.getX() != 0d && currentElementSize.getY() != 0d)
        {
            getParent().getUiManager().getProfiler().endSection();
            return currentElementSize;
        }

        getParent().getUiManager().getProfiler().startSection("Template instantiation for content size.");
        update(getParent().getUiManager().getUpdateManager());
        getParent().getUiManager().getProfiler().endSection();
        getParent().getUiManager().getProfiler().endSection();

        return super.getMinimalContentSize();
    }
}
