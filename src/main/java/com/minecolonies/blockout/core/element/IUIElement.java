package com.minecolonies.blockout.core.element;

import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface IUIElement
{

    /**
     * The unique identifier of the element in the UI.
     * @return The id of the element.
     */
    @NotNull
    String getId();

    /**
     * Sets the visibility of this {@link IUIElement}
     *
     * @param enabled {@code true} to enable this element, {@code false} to disable.
     */
    void setEnabled(final boolean enabled);

    /**
     * Method to get the current {@link Alignment) of the {@link IUIElement}
     *
     * @return The current {@link Alignment}
     */
    EnumSet<Alignment> getAlignment();

    /**
     * Method to set the current {@link Alignment} of the {@link IUIElement}
     *
     * @param alignment The new  {@link Alignment}
     */
    void setAlignment(@NotNull final EnumSet<Alignment> alignment);

    /**
     * Method to get the current {@link Dock ) of the {@link IUIElement}
     *
     * @return The current {@link Dock}
     */
    Dock getDock();

    /**
     * Method to set the current {@link Dock} of the {@link IUIElement}
     *
     * @param dock The new  {@link Dock}
     */
    void setDock(@NotNull final Dock dock);

    /**
     * Method to get the margin of the {@link IUIElement}
     *
     * @return The margin.
     */
    AxisDistance getMargin();

    /**
     * Method to set the margin of the {@link IUIElement}
     *
     * @param margin The new margin.
     */
    void setMargin(@NotNull final AxisDistance margin);

    /**
     * Method to get the size of the {@link IUIElement}
     *
     * @return The size.
     */
    Vector2d getElementSize();

    /**
     * Method to set the size of the {@link IUIElement}
     *
     * @param elementSize The new size.
     */
    void setElementSize(@NotNull final Vector2d elementSize);

    /**
     * Call this method to update the bounding boxes.
     */
    void updateBoundingBoxes();

    /**
     * Method to get the local {@link BoundingBox} of this {@link IUIElement}
     */
    @NotNull
    BoundingBox getLocalBoundingBox();

    /**
     * Gets the absolute {@link BoundingBox} in terms of gui coordinates.
     *
     * @return The absolute {@link BoundingBox}.
     */
    @NotNull
    BoundingBox getAbsoluteBoundingBox();

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

}
