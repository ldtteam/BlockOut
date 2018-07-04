package com.minecolonies.blockout.element.template;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.element.core.AbstractChildrenContainingUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
            return null;
        }

        @Override
        public void writeToElementData(@NotNull final Template element, @NotNull final IUIElementDataBuilder builder)
        {

        }
    }

    private final IUIElementData ownData;

    public Template(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id, final IUIElementData ownData)
    {
        super(KEY_TEMPLATE, style, id, null);
        this.ownData = ownData;

        this.setParent(this);
    }

    public IUIElement generateInstance(@NotNull final IUIElementHost instanceParent, @NotNull final Object context)
    {
        final List<IUIElementData> childDatas = ownData.getChildren(instanceParent);
        if (childDatas.size() != 1)
        {
            throw new IllegalStateException(String.format("Template: %s needs to have exactly one child.", getId()));
        }

        final IUIElementData childData = childDatas.get(0);
        final IUIElement instance = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(childData);
        instance.setDataContext(context);

        return instance;
    }
}
