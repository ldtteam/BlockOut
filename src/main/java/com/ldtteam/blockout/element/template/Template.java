package com.ldtteam.blockout.element.template;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.factory.IUIElementFactory;
import com.ldtteam.blockout.element.core.AbstractChildrenContainingUIElement;
import com.ldtteam.blockout.json.loader.wrapped.WrappedUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

import static com.ldtteam.blockout.util.Constants.Controls.General.CONST_ID;
import static com.ldtteam.blockout.util.Constants.Controls.Template.KEY_TEMPLATE;

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
     * @param boundContext The property for the context retrieval.
     * @param controlId       The id of the new instance.
     * @return The instance from the parent.
     */
    public IUIElement generateInstance(@NotNull final IUIElementHost instanceParent, @NotNull final IDependencyObject<Object> boundContext, @NotNull final String controlId, @NotNull final
    Function<IUIElementData, IUIElementData> dataOverrideCallback)
    {
        final List<IUIElementData> childDatas = ownData.getChildren(instanceParent);
        if (childDatas.size() != 1)
        {
            throw new IllegalStateException(String.format("Template: %s needs to have exactly one child.", getId()));
        }

        final IUIElementData childData = childDatas.get(0);
        final IUIElementData convertedChildData = dataOverrideCallback.apply(childData);

        //Generate a wrapped element so we can overload the values for id and datacontext.
        final WrappedUIElementData wrappedUIElementData = new WrappedUIElementData(convertedChildData)
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
                return boundContext;
            }
        };

        return BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(wrappedUIElementData);
    }
}
