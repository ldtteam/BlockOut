package com.minecolonies.blockout.element.template;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.binding.property.Property;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.loader.wrapped.WrappedUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.minecolonies.blockout.util.Constants.Controls.General.CONST_ID;
import static com.minecolonies.blockout.util.Constants.Controls.Template.KEY_TEMPLATE;

public class Template extends AbstractChildrenContainingUIElement
{

    public static final class Factory implements IUIElementFactory<Template>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_TEMPLATE;
        }

        @NotNull
        @Override
        public Template readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String templateId = elementData.getElementId();

            return new Template(style, templateId, elementData);
        }

        @Override
        public void writeToElementData(@NotNull final Template element, @NotNull final IUIElementDataBuilder builder)
        {
            throw new IllegalArgumentException("Cannot serialize a template to disk.");
        }
    }

    private final IUIElementData ownData;

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
     * @param instanceParent  The instances parent.
     * @param contextProperty The property for the context retrieval.
     * @param controlId       The id of the new instance.
     * @return The instance from the parent.
     */
    public IUIElement generateInstance(@NotNull final IUIElementHost instanceParent, @NotNull final Property<Object> contextProperty, @NotNull final String controlId)
    {
        final List<IUIElementData> childDatas = ownData.getChildren(instanceParent);
        if (childDatas.size() != 1)
        {
            throw new IllegalStateException(String.format("Template: %s needs to have exactly one child.", getId()));
        }

        final IUIElementData childData = childDatas.get(0);

        //Generate a wrapped element so we can overload the values for id and datacontext.
        final WrappedUIElementData wrappedUIElementData = new WrappedUIElementData(childData)
        {
            /**
             * Get the String attribute from the name and definition.
             *
             * @param name the name.
             * @param def  the definition.
             * @return the String.
             */
            @NotNull
            @Override
            public String getStringAttribute(@NotNull final String name, @NotNull final String def)
            {
                if (name.equals(CONST_ID))
                {
                    return controlId;
                }

                return super.getStringAttribute(name, def);
            }

            /**
             * Returns the bound datacontext for the ui element this data belongs to.
             * Only returns a meaningfull value on the server side.
             * On the client side this value will always be bound to a plain Object.
             *
             * @return The bound datacontext.
             */
            @Override
            public IDependencyObject<Object> getBoundDataContext()
            {
                return DependencyObjectHelper.createFromProperty(contextProperty, new Object());
            }
        };

        return BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(wrappedUIElementData);
    }
}
