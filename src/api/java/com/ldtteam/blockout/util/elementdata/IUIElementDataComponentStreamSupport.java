package com.ldtteam.blockout.util.elementdata;

import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

public final class IUIElementDataComponentStreamSupport
{

    private IUIElementDataComponentStreamSupport()
    {
        throw new IllegalStateException("Tried to initialize: IUIElementDataComponentStreamSupport but this is a Utility class.");
    }

    public static Stream<IUIElementDataComponent> streamList(@NotNull final IUIElementDataComponent element)
    {
        if (element.isList())
        {
            return element.getAsList().stream();
        }

        return Stream.<IUIElementDataComponent>builder().build();
    }

    public static Stream<Map.Entry<String, IUIElementDataComponent>> streamMap(@NotNull final IUIElementDataComponent element)
    {
        if (element.isList())
        {
            return element.getAsMap().entrySet().stream();
        }

        return Stream.<Map.Entry<String, IUIElementDataComponent>>builder().build();
    }
}
