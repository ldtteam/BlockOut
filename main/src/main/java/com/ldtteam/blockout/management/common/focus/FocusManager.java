package com.ldtteam.blockout.management.common.focus;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.focus.IFocusManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Class implementing the focus manager interface.
 */
public class FocusManager implements IFocusManager
{
    @NotNull
    private final IUIManager manager;

    @Nullable
    private IUIElement focusedElement = null;

    public FocusManager(final IUIManager manager) {this.manager = manager;}

    @Nullable
    @Override
    public IUIElement getFocusedElement()
    {
        return focusedElement;
    }

    @Override
    public void setFocusedElement(@Nullable final IUIElement focusedElement)
    {
        if (Objects.equals(this.focusedElement, focusedElement))
        {
            return;
        }

        this.focusedElement = focusedElement;

        manager.getNetworkManager().onFocusChanged(focusedElement);
    }

    @Override
    public boolean isFocusedElement(@NotNull final IUIElement element)
    {
        if (focusedElement == null)
        {
            return false;
        }
        return element.getId().equals(focusedElement.getId());
    }
}
