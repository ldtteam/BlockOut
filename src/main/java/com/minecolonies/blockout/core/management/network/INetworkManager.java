package com.minecolonies.blockout.core.management.network;

import com.minecolonies.blockout.core.element.IUIElement;
import org.jetbrains.annotations.Nullable;

public interface INetworkManager
{
    void onFocusChanged(@Nullable final IUIElement oldElement, @Nullable final IUIElement newElement);
}
