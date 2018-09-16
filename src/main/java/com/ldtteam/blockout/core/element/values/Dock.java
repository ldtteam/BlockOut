package com.ldtteam.blockout.core.element.values;

import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.set.SetUtils;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.BiFunction;

/**
 * Enum that describes a docking behaviour of a {@link IUIElement}.
 * Using a docking behaviour other then none results in a {@link IUIElement} being moved to a specific side (or fill its parent entirely) depending on
 * the behaviour selected.
 * <p>
 * To achieve the behaviour call the {@link #apply(IUIElement, BoundingBox)} method, given the target {@link IUIElement} and the
 * new local {@link BoundingBox} as parameters it results in a new {@link BoundingBox} that is modified based on the selected Behaviour.
 * If an illegal {@link Alignment} is detected then an exception is thrown.
 * <p>
 * Several methods have been provided to check for a valid alignment. {@link #isAlignmentValid(IUIElement)} and {@link #isAlignmentValid(EnumSet)}
 */
public enum Dock
{
    TOP((parent, original) ->
          new BoundingBox(new Vector2d(0, 0),
            new Vector2d(parent.getSize().getX(), original.getSize().getY())),
      EnumSet.of(Alignment.TOP, Alignment.LEFT, Alignment.RIGHT, Alignment.NONE)),
    LEFT((parent, original) ->
           new BoundingBox(new Vector2d(0, 0),
             new Vector2d(original.getSize().getX(), parent.getSize().getY())),
      EnumSet.of(Alignment.TOP, Alignment.LEFT, Alignment.BOTTOM, Alignment.NONE)),
    BOTTOM((parent, original) ->
             new BoundingBox(new Vector2d(0, parent.getSize().getY() - original.getSize().getY()).nullifyNegatives(),
               new Vector2d(parent.getSize().getX(), original.getSize().getY())),
      EnumSet.of(Alignment.LEFT, Alignment.BOTTOM, Alignment.RIGHT, Alignment.NONE)),
    RIGHT((parent, original) ->
            new BoundingBox(new Vector2d(parent.getSize().getX() - original.getSize().getX(), 0).nullifyNegatives()
              , new Vector2d(original.getSize().getX(), parent.getSize().getY())),
      EnumSet.of(Alignment.TOP, Alignment.RIGHT, Alignment.BOTTOM, Alignment.NONE)),
    FULL((parent, original) -> new BoundingBox(parent),
      EnumSet.of(Alignment.TOP, Alignment.LEFT, Alignment.RIGHT, Alignment.BOTTOM, Alignment.NONE)),
    NONE((parent, original) -> new BoundingBox(original),
      EnumSet.of(Alignment.TOP, Alignment.LEFT, Alignment.RIGHT, Alignment.BOTTOM, Alignment.NONE));

    private final BiFunction<BoundingBox, BoundingBox, BoundingBox> boundingBoxModificationFunction;
    private final EnumSet<Alignment>                                validAlignments;

    Dock(
      final BiFunction<BoundingBox, BoundingBox, BoundingBox> boundingBoxModificationFunction,
      final EnumSet<Alignment> validAlignments)
    {
        this.boundingBoxModificationFunction = boundingBoxModificationFunction;
        this.validAlignments = validAlignments;
    }

    /**
     * Applies the docking behaviour to the new {@link BoundingBox} of the given target.
     *
     * @param target         The target {@link IUIElement} who's local bounding box is being updated.
     * @param newBoundingBox The new local {@link IUIElement} of the target.
     * @return A modified {@link BoundingBox} according to the docking behaviour.
     */
    public BoundingBox apply(@NotNull final IUIElement target, @NotNull final BoundingBox newBoundingBox)
    {
        if (!isAlignmentValid(target))
        {
            throw new IllegalArgumentException("Target element does not contain valid alignment: " + target.getAlignment() + ". Valid: " + validAlignments);
        }

        return boundingBoxModificationFunction.apply(target.getParent().getLocalInternalBoundingBox(), newBoundingBox);
    }

    /**
     * Method to check if a {@link IUIElement} has a valid alignment for this docking behaviour.
     *
     * @param element The {@link IUIElement} to check.
     * @return True when valid, false when not.
     */
    public boolean isAlignmentValid(@NotNull final IUIElement element)
    {
        return isAlignmentValid(element.getAlignment());
    }

    /**
     * Method to check if a given alignment configuration is a valid alignment for this docker behaviour.
     *
     * @param alignments The alignment configuration to check.
     * @return True when valid, false when not.
     */
    public boolean isAlignmentValid(@NotNull final EnumSet<Alignment> alignments)
    {
        return SetUtils.areAllIn(alignments, validAlignments);
    }
}
