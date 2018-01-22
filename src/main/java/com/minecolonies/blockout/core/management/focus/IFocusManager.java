package com.minecolonies.blockout.core.management.focus;

import com.minecolonies.blockout.core.element.IUIElement;
import org.jetbrains.annotations.Nullable;

public interface IFocusManager
{

    @Nullable
    IUIElement getFocusedElement();

    void setFocusedElement(@Nullable final IUIElement focusedElement);
}
