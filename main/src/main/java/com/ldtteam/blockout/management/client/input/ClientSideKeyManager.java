package com.ldtteam.blockout.management.client.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.input.client.IClientSideKeyAcceptingUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.input.client.IClientSideKeyManager;
import com.ldtteam.blockout.util.keyboard.KeyboardKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientSideKeyManager implements IClientSideKeyManager
{
    private final IUIManager manager;

    public ClientSideKeyManager(final IUIManager manager) {this.manager = manager;}

    @Override
    public boolean onKeyPressed(final int character, final KeyboardKey key)
    {
        return attemptInputInteraction(
          t -> t.isEnabled() && t.canAcceptKeyInputClient(character, key),
          t -> t.onKeyPressedClient(character, key)
        );
    }

    protected boolean attemptInputInteraction(
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @Nullable final IUIElement currentFocus = getManager().getFocusManager().getFocusedElement();

        if (currentFocus instanceof IClientSideKeyAcceptingUIElement)
        {
            final Optional<Boolean> currentFocusInteractionResult = attemptMouseInteractionWith(currentFocus, acceptanceCallback, executionCallback);

            if (currentFocusInteractionResult.isPresent())
            {
                return currentFocusInteractionResult.get();
            }
        }

        return false;
    }

    private IUIManager getManager()
    {
        return manager;
    }

    private Optional<Boolean> attemptMouseInteractionWith(
      @NotNull final IUIElement target,
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @NotNull final IClientSideKeyAcceptingUIElement t = (IClientSideKeyAcceptingUIElement) target;

        if (acceptanceCallback.apply(t))
        {
            return Optional.of(executionCallback.apply(t));
        }

        return Optional.empty();
    }

    @FunctionalInterface
    protected interface IInteractionAcceptanceCallback
    {
        boolean apply(@NotNull final IClientSideKeyAcceptingUIElement target);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        boolean apply(@NotNull final IClientSideKeyAcceptingUIElement target);
    }
}
