package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.input.IClickAcceptingUIElement;
import com.minecolonies.blockout.core.element.input.IKeyAcceptingUIElement;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.util.keyboard.KeyboardKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KeyManager extends AbstractInputManager implements IKeyManager
{
    protected KeyManager(final IUIManager manager)
    {
        super(manager);
    }

    @Override
    public void onKeyPressed(final int character, final KeyboardKey key)
    {

    }

    protected void attemptInputInteraction(
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @Nullable final IUIElement currentFocus = getManager().getFocusManager().getFocusedElement();

        if (currentFocus instanceof IClickAcceptingUIElement)
        {
            if (attemptMouseInteractionWith(currentFocus, acceptanceCallback, executionCallback))
            {
                return;
            }
        }

        onAcceptanceFailure();


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
      final IInteractionAcceptanceCallback acceptanceCallback,
      final IInteractionExecutionCallback executionCallback)
    {
        @NotNull final IKeyAcceptingUIElement t = (IKeyAcceptingUIElement) target;

        if (acceptanceCallback.apply(t))
        {
            executionCallback.apply(t);
            return true;
        }

        return false;
    }

    @FunctionalInterface
    protected interface IInteractionAcceptanceCallback
    {
        boolean apply(@NotNull final IKeyAcceptingUIElement target);
    }

    @FunctionalInterface
    protected interface IInteractionExecutionCallback
    {
        void apply(@NotNull final IKeyAcceptingUIElement target);
    }
}
