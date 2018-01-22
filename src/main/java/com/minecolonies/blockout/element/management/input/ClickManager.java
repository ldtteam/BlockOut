package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.mouse.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClickManager extends AbstractInputManager implements IClickManager
{
    protected ClickManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.canAcceptMouseInput(x, y, button),
          (u, x, y) -> u.onMouseClickBegin(x, y, button)
        );
    }

    @Override
    public void onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.canAcceptMouseInput(x, y, button),
          (u, x, y) -> u.onMouseClickEnd(x, y, button)
        );
    }

    @Override
    public void onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.canAcceptMouseInput(x, y, button),
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
        }

        onAcceptanceFailure();

        final Optional<IUIElement> target = getManager().getHost().searchElementByCoord(localX, localY);
        if (target.isPresent() && target.get() instanceof IClickAcceptingUIElement)
        {
            if (attemptMouseInteractionWith(target.get(), localX, localY, acceptanceCallback, executionCallback))
            {
                getManager().getFocusManager().setFocusedElement(target.get());
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

    private BoundingBox convertToBoundingBox(final int localX, final int localY)
    {
        return new BoundingBox(new Vector2d(localX, localY), new Vector2d());
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
