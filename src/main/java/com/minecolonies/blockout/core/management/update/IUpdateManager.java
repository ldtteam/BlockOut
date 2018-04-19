package com.minecolonies.blockout.core.management.update;

import com.minecolonies.blockout.core.element.IUIElement;
import org.jetbrains.annotations.NotNull;

public interface IUpdateManager
{

    /**
     * Updates the given element.
     *
     * @param element The element.
     */
    void updateElement(@NotNull final IUIElement element);

    /**
     * Mark this update manager dirty.
     */
    void markDirty();
}
