package com.ldtteam.blockout.loader.factory;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.component.ComponentType;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ElementDataComponentConverters
{

    public static final class ElementDataConverter implements IUIElementDataComponentConverter<IUIElementData<?>>
    {
        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isComplex();
        }

        @NotNull
        @Override
        public IUIElementData<?> readFromElement(
          @NotNull final IUIElementDataComponent component, @Nullable final IUIElementData<?> sourceData, @NotNull final Object... params)
        {
            return component.toIUIElementData((IUIElementHost) params[0]);
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final IUIElementData<?> value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C compound = newComponentInstanceProducer.apply(ComponentType.COMPLEX);
            return value.toDataComponent(compound);
        }
    }

    public static final class ListElementDataConverter implements IUIElementDataComponentConverter<List<IUIElementData<?>>>
    {

        @Override
        public boolean matchesInputTypes(@NotNull final IUIElementDataComponent component)
        {
            return component.isList();
        }

        @NotNull
        @Override
        public List<IUIElementData<?>> readFromElement(
          @NotNull final IUIElementDataComponent component, @Nullable final IUIElementData<?> sourceData, @NotNull final Object... params)
        {
            return component.getAsList().stream().map(iuiElementDataComponent -> iuiElementDataComponent.toIUIElementData((IUIElementHost) params[0])).collect(Collectors.toList());
        }

        @Override
        public <C extends IUIElementDataComponent> C writeToElement(
          @NotNull final List<IUIElementData<?>> value, @NotNull final Function<ComponentType, C> newComponentInstanceProducer)
        {
            final C listCompound = newComponentInstanceProducer.apply(ComponentType.LIST);
            listCompound.setList(value.stream().map(elementData -> {
                final C compound = newComponentInstanceProducer.apply(ComponentType.COMPLEX);
                return elementData.toDataComponent(compound);
            }).collect(Collectors.toList()));

            return listCompound;
        }
    }

    private ElementDataComponentConverters()
    {
        throw new IllegalStateException("Tried to initialize: ElementDataComponentConverters but this is a Utility class.");
    }
}
