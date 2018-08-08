package com.minecolonies.blockout.core.management.focus;

import com.minecolonies.blockout.core.element.IUIElement;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for GUI focus management.
 */
public interface IFocusManager
{
    /**
     * Gets the currently focused element.
     * @return the IUIElement.
     */
    @Nullable
    IUIElement getFocusedElement();

    /**
     * Check if an element is the focused element by compared both.
     * @param element the element to compare.
     * @return true if so.
     */
    boolean isFocusedElement(IUIElement element);

    /**
     * Set the focused element.
     * @param focusedElement the element to set as focused.
     */
    void setFocusedElement(@Nullable final IUIElement focusedElement);
}
