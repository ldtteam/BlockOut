package com.ldtteam.blockout.element.template;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.binding.property.Property;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.utils.controlconstruction.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.loader.binding.core.IBindingEngine;
import com.ldtteam.blockout.loader.binding.engine.SimpleBindingEngine;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementDataBuilder;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.wrapped.WrappedUIElementData;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;
import static com.ldtteam.blockout.util.Constants.Controls.Template.KEY_TEMPLATE;
import static com.ldtteam.blockout.util.Constants.ConverterTypes.CHILDREN_LIST_TYPE;
import static com.ldtteam.blockout.util.Constants.Resources.MISSING;

public class Template extends AbstractChildrenContainingUIElement
{

    public static final class Factory implements IUIElementFactory<Template>
    {

        @NotNull
        @Override
        public Class<Template> getProducedElementClass()
        {
            return Template.class;
        }

        @NotNull
        @Override
        public String getTypeName()
        {
            return KEY_TEMPLATE;
        }

        @NotNull
        @Override
        public Template readFromElementData(@NotNull final IUIElementData<?> elementData, @NotNull final IBindingEngine engine)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getFromRawDataWithDefault(CONST_STYLE_ID, engine, IIdentifier.create(MISSING), IIdentifier.class);
            final String templateId = elementData.getMetaData().getId();

            return new Template(style, templateId, elementData);
        }

        @Override
        public void writeToElementData(@NotNull final Template element, @NotNull final IUIElementDataBuilder<?> builder)
        {
            builder.copyFrom(element.ownData);
        }
    }

    private final IUIElementData<?> ownData;

    public Template(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementData ownData)
    {
        super(KEY_TEMPLATE, style, id, null);
        this.ownData = ownData;

        this.setParent(this);
    }

    /**
     * Generates a new instance of the template.
     *
     * @param instanceParent The instances parent.
     * @param boundContext   The property for the context retrieval.
     * @param controlId      The id of the new instance.
     * @return The instance from the parent.
     */
    public IUIElement generateInstance(
      @NotNull final IUIElementHost instanceParent,
      @NotNull final IDependencyObject<Object> boundContext,
      @NotNull final String controlId,
      @NotNull final Function<IUIElementData, IUIElementData> dataOverrideCallback)
    {
        final IDependencyObject<List<IUIElementData<?>>> childrenData = ownData.getFromRawDataWithDefault(CONST_CHILDREN,
          SimpleBindingEngine.getInstance(),
          Lists.newArrayList(),
          CHILDREN_LIST_TYPE,
          instanceParent
        );

        final List<IUIElementData<?>> childDatas = childrenData.get(instanceParent);
        if (childDatas.size() != 1)
        {
            throw new IllegalStateException(String.format("Template: %s needs to have exactly one child.", getId()));
        }

        final IUIElementData<?> childData = childDatas.get(0);
        final IUIElementData<?> convertedChildData = dataOverrideCallback.apply(childData);

        final WrappedUIElementData wrappedUIElementData = new WrappedUIElementData(convertedChildData)
        {
            @NotNull
            @Override
            public IUIElementMetaData getMetaData()
            {
                return new IUIElementMetaData()
                {
                    @Override
                    public String getType()
                    {
                        return convertedChildData.getMetaData().getType();
                    }

                    @Override
                    public String getId()
                    {
                        return controlId;
                    }

                    @Override
                    public Optional<IUIElementHost> getParent()
                    {
                        return Optional.ofNullable(instanceParent);
                    }

                    @Override
                    public boolean hasChildren()
                    {
                        return convertedChildData.getMetaData().hasChildren();
                    }
                };
            }

            @Override
            public IDependencyObject getFromRawDataWithProperty(
              @NotNull final String name,
              @NotNull final IBindingEngine engine,
              @NotNull final Property defaultProperty,
              @NotNull final Object defaultValue,
              @NotNull final Type targetType,
              @NotNull final Object... params)
            {
                if (name.equals(CONST_DATACONTEXT))
                {
                    return boundContext;
                }

                return convertedChildData.getFromRawDataWithProperty(name, engine, defaultProperty, defaultValue, targetType, params);
            }

            @Override
            public Object getRawWithoutBinding(
              @NotNull final String name, @Nullable final Object defaultValue, @NotNull final Type targetType, @NotNull final Object... params)
            {
                if (name.equals(CONST_DATACONTEXT))
                {
                    return boundContext;
                }

                return convertedChildData.getRawWithoutBinding(name, defaultValue, targetType, params);
            }
        };

        return ProxyHolder.getInstance().getFactoryController().getElementFromData(wrappedUIElementData);
    }
}
