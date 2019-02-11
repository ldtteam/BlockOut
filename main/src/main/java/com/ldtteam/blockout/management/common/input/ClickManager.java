package com.ldtteam.blockout.management.common.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.input.IClickAcceptingUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.input.IClickManager;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClickManager extends AbstractInputManager implements IClickManager
{
    public ClickManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInput(x, y, button),
          (u, x, y) -> u.onMouseClickBegin(x, y, button)
        );
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInput(x, y, button),
          (u, x, y) -> u.onMouseClickEnd(x, y, button)
        );
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInput(x, y, button),
          (u, x, y) -> u.onMouseClickMove(x, y, button, timeElapsed)
        );
    }

    protected void attemptInputInteraction(
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @Nullable final IUIElement currentFocus = getManager().getFocusManager().getFocusedElement();

        if (currentFocus instanceof IClickAcceptingUIElement)
        {
            if (attemptMouseInteractionWith(currentFocus, localX, localY, acceptanceCallback, executionCallback))
            {
                return;
            }
            else
            {
                ((IClickAcceptingUIElement) currentFocus).onMouseLeave();
            }
        }

        onAcceptanceFailure();

        final Optional<IUIElement> target =
          getManager().getHost().searchDeepestElementByCoordAndPredicate(new Vector2d(localX, localY), e -> e instanceof IClickAcceptingUIElement);
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
            if (target.get() instanceof IClickAcceptingUIElement && attemptMouseInteractionWith(target.get(), localX, localY, acceptanceCallback, executionCallback))
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
        @NotNull final IClickAcceptingUIElement t = (IClickAcceptingUIElement) target;
        @NotNull final Vector2d absoluteClickPos = new Vector2d(localX, localY);

        if (t.getAbsoluteBoundingBox().includes(absoluteClickPos))
        {
            @NotNull final BoundingBox localTarget = new BoundingBox(absoluteClickPos.move(t.getAbsoluteBoundingBox().getLocalOrigin().invert()), new Vector2d());
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
        boolean apply(@NotNull final IClickAcceptingUIElement target, final int localX, final int localY);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        void apply(@NotNull final IClickAcceptingUIElement target, final int localX, final int localY);
    }
}
