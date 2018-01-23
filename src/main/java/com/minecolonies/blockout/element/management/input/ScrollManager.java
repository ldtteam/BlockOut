package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.input.IScrollAcceptingUIElement;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IScrollManager;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ScrollManager extends AbstractInputManager implements IScrollManager
{

    protected ScrollManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onMouseScroll(final int localX, final int localY, final int deltaWheel)
    {
        attemptInputInteraction(
          localX,
          localY,
          (u,x,y) -> u.canAcceptMouseInput(x,y,deltaWheel),
          (u,x,y) -> u.onMouseScroll(x,y,deltaWheel)
        );
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

        final Optional<IUIElement> target = getManager().getHost().searchDeepestElementByCoord(localX, localY);
        if (target.isPresent() && target.get() instanceof IScrollAcceptingUIElement)
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
        @NotNull final IScrollAcceptingUIElement t = (IScrollAcceptingUIElement) target;
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
        boolean apply(@NotNull final IScrollAcceptingUIElement target, final int localX, final int localY);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        void apply(@NotNull final IScrollAcceptingUIElement target, final int localX, final int localY);
    }
}
