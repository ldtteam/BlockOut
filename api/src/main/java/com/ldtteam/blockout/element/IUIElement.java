package com.ldtteam.blockout.element;

import com.ldtteam.blockout.binding.dependency.IDependencyReceiver;
import com.ldtteam.blockout.element.values.Alignment;
import com.ldtteam.blockout.element.values.AxisDistance;
import com.ldtteam.blockout.element.values.Dock;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.style.core.resources.core.IResource;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public interface IUIElement extends IDependencyReceiver
{

    /**
     * Unique identifier for the control.
     * Allows for the identification of during debugging.
     *
     * @return The unique identifier.
     */
    UUID getUniqueIdentifier();

    /**
     * Returns the type of this IUIElement.
     *
     * @return The type of the element.
     */
    @NotNull
    ResourceLocation getType();

    /**
     * Gets a resource form the style manager.
     * Handy method that accesses the current style id and grabs the resource from the style manager.
     *
     * @param resourceId The id of the resource to get.
     * @param <T>        The type of the resource.
     * @return The resource requested.
     *
     * @throws IllegalArgumentException when no resource can be found in ANY style that matches the given id.
     * @see com.ldtteam.blockout.style.core.IStyleManager#getResource(ResourceLocation, ResourceLocation)
     */
    @NotNull
    default <T extends IResource> T getResource(final ResourceLocation resourceId) throws IllegalArgumentException
    {
        return ProxyHolder.getInstance().getStyleManager().getResource(getStyleId(), resourceId);
    }

    /**
     * Returns the style of this IUIElement.
     *
     * @return The style of the element.
     */
    @NotNull
    ResourceLocation getStyleId();

    /**
     * Called before the drawing of the UI.
     * Can be used to update bounding boxes from bound values, and animation etc.
     *
     * @param manager The update inventory that is updating this Element. Usefull to mark the UI as changed and sync data between client and server.
     */
    void update(@NotNull final IUpdateManager manager);

    /**
     * Method to set the new data context of this {@link IUIElement}
     *
     * @param dataContext The new data context.
     */
    void setDataContext(@Nullable final Object dataContext);

    /**
     * Method to get the current {@link Alignment} of the {@link IUIElement}
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
     * Method to get the current {@link Dock} of the {@link IUIElement}
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

    /**
     * Returns the size the internal bounding box of the parent needs to have at least, to display this control correctly.
     * Basically inverses the boundingbox calculation and makes an assumption on the minimal correct size for this element.
     *
     * @return The minimal size of the parent required to display this control properly.
     */
    default Vector2d getMinimalInternalSizeOfParent()
    {
        getParent().getUiManager().getProfiler().startSection("Minimal internal size: " + getId());
        Optional<Double> marginLeft = getMargin().getLeft();
        Optional<Double> marginTop = getMargin().getTop();
        Optional<Double> marginRight = getMargin().getRight();
        Optional<Double> marginBottom = getMargin().getBottom();

        double width = getElementSize().getX();
        double height = getElementSize().getY();

        getParent().getUiManager().getProfiler().startSection("Minimal Content size: " + getId());
        final Vector2d minimalContentSize = getMinimalContentSize();
        getParent().getUiManager().getProfiler().endSection();

        if (Alignment.LEFT.isActive(this) && Alignment.RIGHT.isActive(this))
        {
            if (!marginLeft.isPresent() && !marginRight.isPresent())
            {
                marginLeft = Optional.of(0d);
                marginRight = Optional.of(0d);
            }
            else if (!marginLeft.isPresent())
            {
                marginLeft = Optional.of(0d);
            }
            else if (!marginRight.isPresent())
            {
                marginRight = Optional.of(0d);
            }
        }
        else if (Alignment.RIGHT.isActive(this))
        {
            marginLeft = Optional.of(0d);
        }

        if (width == 0d)
        {
            width = minimalContentSize.getX();
        }

        if (Alignment.TOP.isActive(this) && Alignment.BOTTOM.isActive(this))
        {
            if (!marginTop.isPresent() && !marginBottom.isPresent())
            {
                marginTop = Optional.of(0d);
                marginBottom = Optional.of(0d);
            }
            else if (!marginTop.isPresent())
            {
                marginTop = Optional.of(0d);
            }
            else if (!marginBottom.isPresent())
            {
                marginBottom = Optional.of(0d);
            }
        }
        else if (Alignment.BOTTOM.isActive(this))
        {
            marginTop = Optional.of(0d);
        }

        if (height == 0d)
        {
            height = minimalContentSize.getY();
        }

        getParent().getUiManager().getProfiler().endSection();

        return new Vector2d(width + marginLeft.orElse(0d) + marginRight.orElse(0d), height + marginTop.orElse(0d) + marginBottom.orElse(0d)).nullifyNegatives();
    }

    /**
     * Method to get the parent of this {@link IUIElement}
     *
     * @return The parent.
     */
    @NotNull
    IUIElementHost getParent();

    /**
     * The unique identifier of the element in the UI.
     *
     * @return The id of the element.
     */
    @NotNull
    String getId();

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
     * Used during assumptive calculations of minimal parent sizes, when a size is required to display this control properly.
     *
     * @return THe minimal size of this control that is required to display it properly.
     */
    default Vector2d getMinimalContentSize()
    {
        return getElementSize();
    }

    /**
     * Method to set the parent of this {@link IUIElement}
     *
     * @param parent The new parent.
     */
    void setParent(@NotNull final IUIElementHost parent);

    /**
     * Indicates if this control is focused.
     *
     * @return True when focused, false when not.
     */
    default boolean isFocused()
    {
        return getParent().getUiManager().getFocusManager().isFocusedElement(this);
    }

}
