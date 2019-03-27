package com.ldtteam.blockout.management.client.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.input.client.IClientSideClickAcceptingUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.input.client.IClientSideClickManager;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import com.ldtteam.blockout.util.mouse.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientSideClickManager implements IClientSideClickManager
{
    private final IUIManager manager;

    public ClientSideClickManager(final IUIManager manager) {this.manager = manager;}

    @Override
    public boolean onMouseClickBegin(final int localX, final int localY, final MouseButton button)
    {
        return attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInputClient(x, y, button),
          (u, x, y) -> u.onMouseClickBeginClient(x, y, button)
        );
    }

    @Override
    public boolean onMouseClickEnd(final int localX, final int localY, final MouseButton button)
    {
        return attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInputClient(x, y, button),
          (u, x, y) -> u.onMouseClickEndClient(x, y, button)
        );
    }

    @Override
    public boolean onMouseClickMove(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        return attemptInputInteraction(
          localX,
          localY,
          (u, x, y) -> u.isEnabled() && u.canAcceptMouseInputClient(x, y, button),
          (u, x, y) -> u.onMouseClickMoveClient(x, y, button, timeElapsed)
        );
    }

    private IUIManager getManager()
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

        if (currentFocus instanceof IClientSideClickAcceptingUIElement)
        {
            final Optional<Boolean> currentFocusInteractionResult = attemptMouseInteractionWith(currentFocus, localX, localY, acceptanceCallback, executionCallback);

            if (currentFocusInteractionResult.isPresent())
            {
                return currentFocusInteractionResult.get();
            }
        }

        final Optional<IUIElement> target =
          getManager().getHost().searchDeepestElementByCoordAndPredicate(new Vector2d(localX, localY), e -> e instanceof IClientSideClickAcceptingUIElement);

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
            if (target.get() instanceof IClientSideClickAcceptingUIElement)
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
        @NotNull final IClientSideClickAcceptingUIElement t = (IClientSideClickAcceptingUIElement) target;
        @NotNull final Vector2d absoluteClickPos = new Vector2d(localX, localY);

        if (t.getAbsoluteBoundingBox().includes(absoluteClickPos))
        {
            @NotNull final BoundingBox localTarget = new BoundingBox(absoluteClickPos.move(t.getAbsoluteBoundingBox().getLocalOrigin().invert()), new Vector2d());
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
        boolean apply(@NotNull final IClientSideClickAcceptingUIElement target, final int localX, final int localY);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        boolean apply(@NotNull final IClientSideClickAcceptingUIElement target, final int localX, final int localY);
    }
}
