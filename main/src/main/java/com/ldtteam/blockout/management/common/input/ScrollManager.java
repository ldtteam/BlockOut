package com.ldtteam.blockout.management.common.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.input.IScrollAcceptingUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.input.IScrollManager;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ScrollManager extends AbstractInputManager implements IScrollManager
{
    public ScrollManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onMouseWheel(final int localX, final int localY, final int deltaWheel)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInput(x, y, deltaWheel),
          (u, x, y) -> u.onMouseScroll(x, y, deltaWheel));
    }

    protected void attemptInputInteraction(
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @Nullable final IUIElement currentFocus = getManager().getFocusManager().getFocusedElement();

        if (currentFocus instanceof IScrollAcceptingUIElement)
        {
            if (attemptMouseInteractionWith(currentFocus, localX, localY, acceptanceCallback, executionCallback))
            {
                return;
            }
        }

        onAcceptanceFailure();

        final Optional<IUIElement> target = getManager().getHost().searchDeepestElementByCoord(new Vector2d(localX, localY));
        attemptFailedInputInteraction(localX, localY, acceptanceCallback, executionCallback, target);
    }

    private void attemptFailedInputInteraction(
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback,
      final Optional<IUIElement> target
    )
    {
        if (target.isPresent())
        {
            if (target.get() instanceof IScrollAcceptingUIElement && attemptMouseInteractionWith(target.get(), localX, localY, acceptanceCallback, executionCallback))
            {
                getManager().getFocusManager().setFocusedElement(target.get());
            }
            else if (target.get().getParent() != null && target.get().getParent() != target.get())
            {
                attemptFailedInputInteraction(localX, localY, acceptanceCallback, executionCallback, target.map(IUIElement::getParent));
            }
        }
    }

    private boolean attemptMouseInteractionWith(
      @NotNull final IUIElement target,
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @NotNull final IScrollAcceptingUIElement t = (IScrollAcceptingUIElement) target;
        @NotNull final Vector2d absoluteTarget = new Vector2d(localX, localY);

        if (t.getAbsoluteBoundingBox().includes(absoluteTarget))
        {
            @NotNull final BoundingBox localTarget = new BoundingBox(absoluteTarget.move(t.getAbsoluteBoundingBox().getLocalOrigin().invert()), new Vector2d());
            if (acceptanceCallback.apply(t, (int) localTarget.getLocalOrigin().getX(), (int) localTarget.getLocalOrigin().getY()))
            {
                executionCallback.apply(t, (int) localTarget.getLocalOrigin().getX(), (int) localTarget.getLocalOrigin().getY());
                return true;
            }
        }

        return false;
    }

    @FunctionalInterface
    protected interface IInteractionAcceptanceCallback
    {
        boolean apply(@NotNull final IScrollAcceptingUIElement target, final int localX, final int localY);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        void apply(@NotNull final IScrollAcceptingUIElement target, final int localX, final int localY);
    }
}
