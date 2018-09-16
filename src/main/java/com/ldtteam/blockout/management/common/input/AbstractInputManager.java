package com.ldtteam.blockout.management.common.input;

import com.ldtteam.blockout.core.management.IUIManager;
import com.ldtteam.blockout.core.management.input.IInputManager;

public abstract class AbstractInputManager implements IInputManager
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

    protected IUIManager getManager()
    {
        return manager;
    }

    protected void onAcceptanceFailure()
    {
        if (shouldRemoveFocusOnAcceptanceFailure)
        {
            getManager().getFocusManager().setFocusedElement(null);
        }
    }

}
