package com.minecolonies.blockout.util.template;

import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.wrapped.WrappedUIElementData;
import com.minecolonies.blockout.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class DataWrappingUtils
{

    private DataWrappingUtils()
    {
        throw new IllegalStateException("Tried to initialize: DataWrappingUtils but this is a Utility class.");
    }

    @NotNull
    public static Function<IUIElementData, IUIElementData> generateSizeControllingConverter(@NotNull final IUIElementHost element)
    {
        return iuiElementData -> new WrappedUIElementData(iuiElementData)
        {
            @Override
            public IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(
              @NotNull final String name, final AxisDistance def)
            {
                return super.getBoundAxisDistanceAttribute(name, def);
            }
        };
    }
}
