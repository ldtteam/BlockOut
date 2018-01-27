package com.minecolonies.blockout.core.factory;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import org.jetbrains.annotations.NotNull;

public interface IUIElementFactory<U extends IUIElement>
{

    @NotNull
    U readFromElementData(@NotNull final IUIElementData paneParams);

    void writeToElementData(@NotNull final U element, @NotNull final IUIElementData data);
}
