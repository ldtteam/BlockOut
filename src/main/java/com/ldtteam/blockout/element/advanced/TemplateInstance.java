package com.ldtteam.blockout.element.advanced;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Function;

import static com.ldtteam.blockout.util.Constants.Controls.TemplateInstance.CONST_TEMPLATE;
import static com.ldtteam.blockout.util.Constants.Controls.TemplateInstance.KEY_TEMPLATE_INSTANCE;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

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

    public static final class Factory extends AbstractChildrenContainingUIElementFactory<TemplateInstance>
    {

        public Factory()
        {
            super((elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> {
                final IDependencyObject<ResourceLocation> templateResource = elementData.getFromRawDataWithDefault(CONST_TEMPLATE, engine, MISSING);

                final TemplateInstance element = new TemplateInstance(
                  id,
                  parent,
                  styleId,
                  alignments,
                  dock,
                  margin,
                  padding,
                  elementSize,
                  dataContext,
                  visible,
                  enabled,
                  templateResource
                );

                if (elementData.getMetaData().hasChildren())
                {
                    //Clear out the has changed on the clientside.
                    templateResource.get(element);
                }

                return element;
            }, (element, builder) -> builder.addComponent(CONST_TEMPLATE, element.getTemplateResource()));
        }

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
    }

    private IDependencyObject<ResourceLocation> templateResource;

    public TemplateInstance(
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> styleId,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<AxisDistance> padding,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> templateResource)
    {
        super(KEY_TEMPLATE_INSTANCE, styleId, id, parent, alignments, dock, margin, elementSize, padding, dataContext, visible, enabled);

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