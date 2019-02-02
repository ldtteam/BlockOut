package com.ldtteam.blockout.management.client.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.input.IScrollAcceptingUIElement;
import com.ldtteam.blockout.element.input.client.IClientSideScrollAcceptingUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.input.client.IClientSideScrollManager;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientSideScrollManager implements IClientSideScrollManager
{

    private final IUIManager manager;

    public ClientSideScrollManager(final IUIManager manager) {this.manager = manager;}

    @Override
    public boolean onMouseWheel(final int localX, final int localY, final int deltaWheel)
    {
        return attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInputClient(x, y, deltaWheel),
          (u, x, y) -> u.onMouseScrollClient(x, y, deltaWheel));
    }

    public IUIManager getManager()
    {
        return manager;
    }

    protected boolean attemptInputInteraction(
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @Nullable final IUIElement currentFocus = getManager().getFocusManager().getFocusedElement();

        if (currentFocus instanceof IClientSideScrollAcceptingUIElement)
        {
            final Optional<Boolean> currentFocusInteractionResult = attemptMouseInteractionWith(currentFocus, localX, localY, acceptanceCallback, executionCallback);

            if (currentFocusInteractionResult.isPresent())
            {
                return currentFocusInteractionResult.get();
            }
        }

        final Optional<IUIElement> target = getManager().getHost().searchDeepestElementByCoord(new Vector2d(localX, localY));

        return attemptFailedInputInteraction(localX, localY, acceptanceCallback, executionCallback, target).orElse(false);
    }

    private Optional<Boolean> attemptFailedInputInteraction(
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback,
      final Optional<IUIElement> target
    )
    {
        if (target.isPresent())
        {
            if (target.get() instanceof IClientSideScrollAcceptingUIElement)
            {
                return attemptMouseInteractionWith(target.get(), localX, localY, acceptanceCallback, executionCallback);
            }
            else if (target.get().getParent() != null && target.get().getParent() != target.get())
            {
                return attemptFailedInputInteraction(localX, localY, acceptanceCallback, executionCallback, target.map(IUIElement::getParent));
            }
        }

        return Optional.empty();
    }

    private Optional<Boolean> attemptMouseInteractionWith(
      @NotNull final IUIElement target,
      final int localX,
      final int localY,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @NotNull final IClientSideScrollAcceptingUIElement t = (IClientSideScrollAcceptingUIElement) target;
        @NotNull final Vector2d absoluteTarget = new Vector2d(localX, localY);

        if (t.getAbsoluteBoundingBox().includes(absoluteTarget))
        {
            @NotNull final BoundingBox localTarget = new BoundingBox(absoluteTarget.move(t.getAbsoluteBoundingBox().getLocalOrigin().invert()), new Vector2d());
            if (acceptanceCallback.apply(t, (int) localTarget.getLocalOrigin().getX(), (int) localTarget.getLocalOrigin().getY()))
            {
                return Optional.of(executionCallback.apply(t, (int) localTarget.getLocalOrigin().getX(), (int) localTarget.getLocalOrigin().getY()));
            }
        }

        return Optional.empty();
    }

    @FunctionalInterface
    protected interface IInteractionAcceptanceCallback
    {
        boolean apply(@NotNull final IClientSideScrollAcceptingUIElement target, final int localX, final int localY);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        boolean apply(@NotNull final IClientSideScrollAcceptingUIElement target, final int localX, final int localY);
    }
}
