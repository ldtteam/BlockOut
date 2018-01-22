package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IInputManager;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractInputManager<T extends IUIElement> implements IInputManager
{

    private final IUIManager manager;
    private       boolean    shouldRemoveFocusOnAcceptanceFailure;

    protected AbstractInputManager(final IUIManager manager) {this.manager = manager;}

    @Override
    public boolean shouldRemoveFocusOnAcceptanceFailure()
    {
        return shouldRemoveFocusOnAcceptanceFailure;
    }

    @Override
    public void setShouldRemoveFocusOnAcceptanceFailure(final boolean shouldRemoveFocusOnAcceptanceFailure)
    {
        this.shouldRemoveFocusOnAcceptanceFailure = shouldRemoveFocusOnAcceptanceFailure;
    }

    protected void attemptInputInteraction(
      final int localX,
      final int localY,
      final IMouseInterActionAcceptanceCallback<T> acceptanceCallback,
      final IMouseInterActionExecutionCallback<T> executionCallback,
      final Class<T> targetClass)
    {
        @Nullable final IUIElement currentFocus = getManager().getFocusManager().getFocusedElement();

        if (targetClass.isInstance(currentFocus))
        {
            if (attemptMouseInteractionWith(currentFocus, localX, localY, acceptanceCallback, executionCallback))
            {
                return;
            }
        }

        onAcceptanceFailure();

        final Optional<IUIElement> target = getManager().getHost().searchElementByCoord(localX, localY);
        if (target.isPresent() && targetClass.isInstance(target.get()))
        {
            if (attemptMouseInteractionWith(target.get(), localX, localY, acceptanceCallback, executionCallback))
            {
                getManager().getFocusManager().setFocusedElement(target.get());
            }
        }
    }

    public IUIManager getManager()
    {
        return manager;
    }

    private boolean attemptMouseInteractionWith(
      @NotNull final IUIElement target,
      final int localX,
      final int localY,
      final IMouseInterActionAcceptanceCallback<T> acceptanceCallback,
      final IMouseInterActionExecutionCallback<T> executionCallback)
    {
        @NotNull final T t = (T) target;
        @NotNull final BoundingBox absoluteTarget = convertToBoundingBox(localX, localY);

        if (t.getAbsoluteBoundingBox().includes(absoluteTarget))
        {
            @NotNull final BoundingBox localTarget = new BoundingBox(absoluteTarget.getLocalOrigin().move(t.getAbsoluteBoundingBox().getLocalOrigin().invert()), new Vector2d());
            if (acceptanceCallback.apply(t, (int) localTarget.getLocalOrigin().getX(), (int) localTarget.getLocalOrigin().getY()))
            {
                executionCallback.apply(t, (int) localTarget.getLocalOrigin().getX(), (int) localTarget.getLocalOrigin().getY());
                return true;
            }
        }

        return false;
    }

    protected void onAcceptanceFailure()
    {
        if (shouldRemoveFocusOnAcceptanceFailure)
        {
            getManager().getFocusManager().setFocusedElement(null);
        }
    }

    private BoundingBox convertToBoundingBox(final int localX, final int localY)
    {
        return new BoundingBox(new Vector2d(localX, localY), new Vector2d());
    }

    @FunctionalInterface
    protected interface IMouseInterActionAcceptanceCallback<T extends IUIElement>
    {
        boolean apply(@NotNull final T target, final int localX, final int localY);
    }

    @FunctionalInterface
    protected interface IMouseInterActionExecutionCallback<T extends IUIElement>
    {
        void apply(@NotNull final T target, final int localX, final int localY);
    }
}
