package com.ldtteam.blockout.element.advanced;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.dependency.injection.DependencyObjectInjector;
import com.ldtteam.blockout.builder.core.IBlockOutGuiConstructionData;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.data.BlockOutGuiConstructionData;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.event.injector.EventHandlerInjector;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.proxy.ProxyHolder;
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

    private static final long serialVersionUID = 2800081688752470813L;

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
            super(TemplateInstance.class,
              KEY_TEMPLATE_INSTANCE,
              (elementData, engine, id, parent, styleId, alignments, dock, margin, padding, elementSize, dataContext, visible, enabled) -> {
                  final IDependencyObject<ResourceLocation> templateResource =
                    elementData.getFromRawDataWithDefault(CONST_TEMPLATE, engine, new ResourceLocation(MISSING), ResourceLocation.class);

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
              }, (element, builder) -> builder.addComponent(CONST_TEMPLATE, element.getTemplateResource(), ResourceLocation.class));
        }
    }

    public IDependencyObject<ResourceLocation>                  templateResource;
    public IDependencyObject<IBlockOutGuiConstructionData> templateConstructionData;

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
        this.templateConstructionData = DependencyObjectHelper.createFromValue(new BlockOutGuiConstructionData());
    }

    public TemplateInstance(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent,
      @NotNull final IDependencyObject<IBlockOutGuiConstructionData> templateConstructionData)
    {
        super(KEY_TEMPLATE_INSTANCE, style, id, parent);

        this.templateResource = DependencyObjectHelper.createFromValue(new ResourceLocation("minecraft:missingno"));
        this.templateConstructionData = DependencyObjectHelper.createFromValue(new BlockOutGuiConstructionData());
    }

    @Override
    public Vector2d getMinimalContentSize()
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

            final IUIElement element = ProxyHolder.getInstance().getTemplateEngine().generateFromTemplate(
              this,
              DependencyObjectHelper.createFromGetterOnly(Function.identity(), new Object()),
              resolvedTemplateResource,
              getId() + "_instance");

            if (getTemplateConstructionData() != null)
            {
                @NotNull final IBlockOutGuiConstructionData data = getTemplateConstructionData();
                DependencyObjectInjector.inject(element, data);
                EventHandlerInjector.inject(element, data);

                if (element instanceof IUIElementHost)
                {
                    IUIElementHost iuiElementHost = (IUIElementHost) element;
                    iuiElementHost.getAllCombinedChildElements().values().forEach(c -> {
                        DependencyObjectInjector.inject(c, data);
                        EventHandlerInjector.inject(c, data);
                    });
                }
            }

            put(element.getId(), element);
            getParent().getUiManager().getProfiler().endSection();
        }
        getParent().getUiManager().getProfiler().endSection();
    }

    public IBlockOutGuiConstructionData getTemplateConstructionData()
    {
        return templateConstructionData.get(this);
    }

    public void setTemplateConstructionData(final @NotNull IBlockOutGuiConstructionData templateConstructionData)
    {
        this.templateConstructionData.set(this, templateConstructionData);
    }

    public ResourceLocation getTemplateResource()
    {
        return templateResource.get(this);
    }

    public void setTemplateResource(@NotNull final ResourceLocation templateResource)
    {
        this.templateResource.set(this, templateResource);
    }
}
