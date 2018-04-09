package com.minecolonies.blockout.core.element;

import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface IUIElementHost extends Map<String, IUIElement>, IUIElement
{
    /**
     * Returns the {@link IUIManager} for this host.
     * Redirects to the parent by default.
     *
     * @return The {@link IUIManager} for this {@link IUIElementHost}.
     */
    @NotNull
    default IUIManager getUiManager()
    {
        return getParent().getUiManager();
    }

    /**
     * Method used to get the padding of the child elements.
     *
     * @return The padding of child elements
     */
    AxisDistance getPadding();

    /**
     * Method to set the padding of the child elements.
     *
     * @param padding The new padding.
     */
    void setPadding(@NotNull final AxisDistance padding);

    /**
     * Method to get the local internal bounding box in which the children reside.
     *
     * @return The local internal bounding box.
     */
    BoundingBox getLocalInternalBoundingBox();

    /**
     * Method to get the absolute internal bounding box in which the children reside.
     *
     * @return The absolute internal bounding box.
     */
    BoundingBox getAbsoluteInternalBoundingBox();

    /**
     * Finds a child {@link IUIElement} using recursion that has the given id and is an instance of the given class.
     *
     * @param id  The id that is searched for.
     * @param cls The class of the type that is looked for.
     * @return An {@link Optional} containing the searched element or nothing.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    default <T extends IUIElement> Optional<T> searchExactElementById(@NotNull final String id, @NotNull final Class<T> cls)
    {
        return searchExactElementById(id).filter(cls::isInstance).map(element -> (T) element);
    }

    /**
     * Finds a child {@link IUIElement} using recursion that has the given id.
     *
     * @param id The id that is searched for.
     * @return An {@link Optional} containing the searched element or nothing.
     */
    @NotNull
    default Optional<IUIElement> searchExactElementById(@NotNull final String id)
    {
        return searchFirstElementByPredicate(element -> element.getId().equals(id));
    }

    @NotNull
    default Optional<IUIElement> searchFirstElementByPredicate(@NotNull final Predicate<IUIElement> predicate)
    {
        if (predicate.test(this))
        {
            return Optional.of(this);
        }

        return Optional.ofNullable(values()
                                     .stream()
                                     .filter(element -> !(element instanceof IUIElementHost))
                                     .filter(predicate)
                                     .findFirst()
                                     .orElse(
                                       values()
                                         .stream()
                                         .filter(element -> (element instanceof IUIElementHost))
                                         .filter(predicate)
                                         .findFirst()
                                         .orElse(null)
                                     )
        );
    }

    /**
     * Finds the child {@link IUIElement} that is deepest in the Component-Tree given the LOCAL coord.
     *
     * @param localPoint The local coordinate to probe.
     * @return An {@link Optional} with the deepest {@link IUIElement}. Empty optional is returned if the given coord is not in this {@link IUIElement}.
     */
    @NotNull
    default Optional<IUIElement> searchDeepestElementByCoord(final Vector2d localPoint)
    {
        return searchDeepestElementByCoordAndPredicate(localPoint, element -> true);
    }

    /**
     * Finds the child {@link IUIElement} that is deepest in the Component-Tree given the LOCAL coord that matches the predicate.
     *
     * @param localPoint The local coordinate to probe.
     * @param predicate The predicate to filter the elements with.
     * @return An {@link Optional} with the deepest {@link IUIElement}. Empty optional is returned if the given coord is not in this {@link IUIElement}.
     */
    @NotNull
    default Optional<IUIElement> searchDeepestElementByCoordAndPredicate(final Vector2d localPoint, Predicate<IUIElement> predicate)
    {
        if (!this.getLocalBoundingBox().includes(localPoint))
        {
            return Optional.empty();
        }

        for (IUIElement element : values())
        {
            final Vector2d elementLocalCoord = localPoint.move(element.getLocalBoundingBox().getLocalOrigin().invert());
            if (element instanceof IUIElementHost)
            {
                final IUIElementHost elementHost = (IUIElementHost) element;
                final Optional<IUIElement> elementResult = elementHost.searchDeepestElementByCoordAndPredicate(elementLocalCoord, predicate);
                if (elementResult.isPresent())
                {
                    return elementResult;
                }
            }
            else
            {
                if (element.getLocalBoundingBox().includes(elementLocalCoord) && predicate.test(element))
                {
                    return Optional.of(element);
                }
            }
        }

        if (predicate.test(this))
        {
            return Optional.of(this);
        }
        else
        {
            return Optional.empty();
        }
    }


}
