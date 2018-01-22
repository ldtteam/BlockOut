package com.minecolonies.blockout.core.element;

import com.minecolonies.blockout.util.math.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface IUIElement
{

    /**
     * Method to get the local {@link BoundingBox} of this {@link IUIElement}
     */
    @NotNull
    BoundingBox getLocalBoundingBox();

    /**
     * Method to set the local {@link BoundingBox} of this {@link IUIElement}
     *
     * @param box The new {@link BoundingBox}
     */
    void setLocalBoundingBox(@NotNull final BoundingBox box);

    /**
     * Gets the absolute {@link BoundingBox} in terms of screen coordinates.
     *
     * @return The absolute {@link BoundingBox}.
     */
    @NotNull
    default BoundingBox getAbsoluteBoundingBox()
    {
        if (getParent() == this)
        {
            return getLocalBoundingBox();
        }

        final BoundingBox parentAbsoluteBindingBox = getParent().getAbsoluteBoundingBox();
        return new BoundingBox(parentAbsoluteBindingBox.getLocalOrigin().move(getLocalBoundingBox().getLocalOrigin()), getLocalBoundingBox().getSize());
    }

    /**
     * Method to get the parent of this {@link IUIElement}
     *
     * @return The parent.
     */
    @NotNull
    IUIElementHost getParent();

    /**
     * Method to set the parent of this {@link IUIElement}
     *
     * @param parent The new parent.
     */
    void setParent(@NotNull final IUIElementHost parent);

    /**
     * Indicates if this {@link IUIElement} ius visible or not.
     *
     * @return {@code true} when this {@link IUIElement} is visible or not.
     */
    boolean isVisible();

    /**
     * Sets the visibility of this {@link IUIElement}
     *
     * @param visible {@code true} to make this element visible, {@code false} to hide.
     */
    void setVisible(final boolean visible);

    /**
     * Indicates if this {@link IUIElement} ius visible or not.
     *
     * @return {@code true} when this {@link IUIElement} is visible or not.
     */
    boolean isEnabled();

    /**
     * Sets the visibility of this {@link IUIElement}
     *
     * @param enabled {@code true} to enable this element, {@code false} to disable.
     */
    void setEnabled(final boolean enabled);
}
